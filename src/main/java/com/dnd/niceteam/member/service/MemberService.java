package com.dnd.niceteam.member.service;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.department.exception.DepartmentNotFoundException;
import com.dnd.niceteam.domain.emailauth.EmailAuth;
import com.dnd.niceteam.domain.emailauth.EmailAuthRepository;
import com.dnd.niceteam.domain.emailauth.exception.NotAuthenticatedEmail;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberEditor;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.DuplicateEmailException;
import com.dnd.niceteam.domain.member.exception.DuplicateNicknameException;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectMemberRepository;
import com.dnd.niceteam.member.dto.DupCheck;
import com.dnd.niceteam.member.dto.MemberCreation;
import com.dnd.niceteam.member.dto.MemberDetail;
import com.dnd.niceteam.member.dto.MemberUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final AccountRepository accountRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final DepartmentRepository departmentRepository;

    private final PasswordEncoder passwordEncoder;

    private final MemberScoreRepository memberScoreRepository;

    private final ProjectMemberRepository projectMemberRepository;

    public DupCheck.ResponseDto checkEmailDuplicate(String email) {
        DupCheck.ResponseDto responseDto = new DupCheck.ResponseDto();
        responseDto.setDuplicated(accountRepository.existsByEmail(email));
        return responseDto;
    }

    public DupCheck.ResponseDto checkNicknameDuplicate(String nickname) {
        DupCheck.ResponseDto responseDto = new DupCheck.ResponseDto();
        responseDto.setDuplicated(memberRepository.existsByNickname(nickname));
        return responseDto;
    }

    @Transactional
    public MemberCreation.ResponseDto createMember(MemberCreation.RequestDto requestDto) {
        if (isNotAuthenticatedEmail(requestDto.getEmail())) {
            throw new NotAuthenticatedEmail("email = " + requestDto.getEmail());
        }
        if (accountRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateEmailException("email = " + requestDto.getEmail());
        }
        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new DuplicateNicknameException("nickname = " + requestDto.getNickname());
        }
        Department department = getDepartmentEntity(requestDto.getDepartmentId());
        Account account = accountRepository.save(Account.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build());
        MemberScore memberScore = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .participationSum(0)
                .rematchingSum(0)
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(department.getUniversity())
                .department(department)
                .memberScore(memberScore)
                .nickname(requestDto.getNickname())
                .admissionYear(requestDto.getAdmissionYear())
                .personality(new Personality(
                        requestDto.getPersonalityAdjective(), requestDto.getPersonalityNoun()))
                .interestingFields(requestDto.getInterestingFields())
                .introduction(requestDto.getIntroduction())
                .introductionUrl(requestDto.getIntroductionUrl())
                .build());

        MemberCreation.ResponseDto responseDto = new MemberCreation.ResponseDto();
        responseDto.setEmail(member.getAccount().getEmail());
        responseDto.setId(member.getId());
        return responseDto;
    }

    private boolean isNotAuthenticatedEmail(String email) {
        return !isAuthenticatedEmail(email);
    }

    private boolean isAuthenticatedEmail(String email) {
        return emailAuthRepository.findLatestByEmail(email)
                .map(EmailAuth::getAuthenticated)
                .orElse(false);
    }

    private Department getDepartmentEntity(long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("departmentId = " + departmentId));
    }

    @Transactional
    public MemberUpdate.ResponseDto updateMember(String username, MemberUpdate.RequestDto requestDto) {
        Member member = getMemberEntityByEmail(username);
        MemberEditor memberEditor = member.toEditor()
                .nickname(requestDto.getNickname())
                .personalityAdjective(requestDto.getPersonalityAdjective())
                .personalityNoun(requestDto.getPersonalityNoun())
                .interestingFields(requestDto.getInterestingFields())
                .introduction(requestDto.getIntroduction())
                .introductionUrl(requestDto.getIntroductionUrl())
                .build();
        MemberUpdate.ResponseDto responseDto = new MemberUpdate.ResponseDto();
        responseDto.setId(member.edit(memberEditor));
        return responseDto;
    }

    private Member getMemberEntityByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("email = " + email));
    }

    public MemberDetail.ResponseDto getMemberDetail(long memberId) {
        Member member = getMemberEntityById(memberId);
        MemberScore memberScore = member.getMemberScore();
        List<ProjectMember> projectMembers = projectMemberRepository.findDoneProjectMemberByMember(member);
        MemberDetail.ResponseDto responseDto = new MemberDetail.ResponseDto();
        responseDto.setId(member.getId());
        responseDto.setNickname(member.getNickname());
        responseDto.setPersonality(member.getPersonality());
        responseDto.setDepartmentName(member.getDepartment().getName());
        responseDto.setInterestingFields(member.getInterestingFields());
        responseDto.setAdmissionYear(member.getAdmissionYear());
        responseDto.setIntroduction(member.getIntroduction());
        responseDto.setIntroductionUrl(member.getIntroductionUrl());
        responseDto.setLevel(memberScore.getLevel());
        responseDto.setParticipationPct(memberScore.participationPct());
        responseDto.setReviewTagToNums(memberScore.getReviewTagToNums());
        responseDto.setNumTotalEndProject(projectMembers.size());
        responseDto.setNumCompleteProject((int) projectMembers.stream()
                .filter(projectMember -> !projectMember.getExpelled())
                .count());
        return responseDto;
    }

    private Member getMemberEntityById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("memberId = " + memberId));
    }
}