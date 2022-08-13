package com.dnd.niceteam.domain.member;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestJpaConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MemberScoreRepository memberScoreRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findByEmail() {
        // given
        University university = universityRepository.save(University.builder()
                .name("테스트대학교")
                .emailDomain("email.com")
                .build());
        Department department = departmentRepository.save(Department.builder()
                .university(university)
                .collegeName("테스트단과대학")
                .name("테스트학과")
                .region("서울")
                .mainBranchType("본교")
                .build());
        MemberScore memberScore = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .rematchingSum(0)
                .build());
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("test-password")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
                .memberScore(memberScore)
                .nickname("test-nickname")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());
        em.flush();
        em.clear();

        // when
        Member foundMember = memberRepository.findByEmail("test@email.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        assertThat(foundMember.getAccount().getEmail()).isEqualTo("test@email.com");
        assertThat(foundMember.getUniversity().getName()).isEqualTo("테스트대학교");
        assertThat(foundMember.getDepartment().getName()).isEqualTo("테스트학과");
        assertThat(foundMember.getNickname()).isEqualTo("test-nickname");
        assertThat(foundMember.getPersonality().getAdjective()).isEqualTo(Personality.Adjective.LOGICAL);
        assertThat(foundMember.getPersonality().getNoun()).isEqualTo(Personality.Noun.LEADER);
    }
}