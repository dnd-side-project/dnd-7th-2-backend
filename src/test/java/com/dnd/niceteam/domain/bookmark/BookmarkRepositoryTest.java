package com.dnd.niceteam.domain.bookmark;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.bookmark.dto.LectureBookmarkDto;
import com.dnd.niceteam.domain.bookmark.dto.LectureTimeDto;
import com.dnd.niceteam.domain.bookmark.dto.SideBookmarkDto;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestJpaConfig.class)
class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private LectureProjectRepository lectureProjectRepository;

    @Autowired
    private SideProjectRepository sideProjectRepository;

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
    private RecruitingRepository recruitingRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findLectureBookmarkDtoPageByMember() {
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
        MemberScore memberScore1 = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account1 = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("test-password")
                .build());
        Member member1 = memberRepository.save(Member.builder()
                .account(account1)
                .university(university)
                .department(department)
                .memberScore(memberScore1)
                .nickname("test-nickname")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());
        MemberScore memberScore2 = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account2 = accountRepository.save(Account.builder()
                .email("test2@email.com")
                .password("test-password2")
                .build());
        Member member2 = memberRepository.save(Member.builder()
                .account(account2)
                .university(university)
                .department(department)
                .memberScore(memberScore2)
                .nickname("test-nickname2")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());
        LectureProject doneProject = lectureProjectRepository.save(LectureProject.builder()
                .name("테스트 프로젝트 1")
                .startDate(LocalDate.of(2022, 3, 2))
                .endDate(LocalDate.of(2022, 6, 30))
                .department(department)
                .professor("교수1")
                .lectureTimes(Set.of())
                .build());
        doneProject.end();
        LectureProject notStartedProject = lectureProjectRepository.save(LectureProject.builder()
                .name("테스트 프로젝트 2")
                .startDate(LocalDate.of(2022, 3, 2))
                .endDate(LocalDate.of(2022, 6, 30))
                .department(department)
                .professor("교수1")
                .lectureTimes(Set.of(
                        LectureTime.builder()
                                .dayOfWeek(DayOfWeek.WED)
                                .startTime(LocalTime.of(3, 30))
                                .build(),
                        LectureTime.builder()
                                .dayOfWeek(DayOfWeek.THUR)
                                .startTime(LocalTime.of(4, 30))
                                .build()))
                .build());
        Recruiting recruiting1 = recruitingRepository.save(Recruiting.builder()
                .project(doneProject)
                .member(member1)
                .title("title1")
                .content("content1")
                .recruitingMemberCount(4)
                .recruitingType(Type.LECTURE)
                .activityArea(ActivityArea.ONLINE)
                .recruitingEndDate(LocalDate.of(2022, 3, 1))
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("")
                .build());
        Recruiting recruiting2 = recruitingRepository.save(Recruiting.builder()
                .project(notStartedProject)
                .member(member2)
                .title("title2")
                .content("content2")
                .recruitingMemberCount(2)
                .recruitingType(Type.LECTURE)
                .activityArea(ActivityArea.ONLINE)
                .recruitingEndDate(LocalDate.of(2022, 4, 1))
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("")
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .recruiting(recruiting1)
                .member(member1)
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .recruiting(recruiting2)
                .member(member1)
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .recruiting(recruiting1)
                .member(member2)
                .build());
        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<LectureBookmarkDto> lectureBookmarkDtoPage = bookmarkRepository.findLectureBookmarkDtoPageByMember(
                pageable, member1);

        // then
        for (LectureTimeDto lectureTime : lectureBookmarkDtoPage.getContent().get(0).getLectureTimes()) {
            System.out.println("lectureTime = " + lectureTime.getDayOfWeek());
        }
        assertThat(lectureBookmarkDtoPage.getTotalElements()).isEqualTo(2);
        assertThat(lectureBookmarkDtoPage.getContent()).hasSize(2);
        assertThat(lectureBookmarkDtoPage.getContent()).extracting("title")
                .containsExactly("title1", "title2");
        assertThat(lectureBookmarkDtoPage.getContent().get(0).getLectureTimes())
                .isEmpty();
        assertThat(lectureBookmarkDtoPage.getContent().get(1).getLectureTimes())
                .extracting("dayOfWeek")
                .containsOnly(DayOfWeek.WED, DayOfWeek.THUR);
    }

    @Test
    void findSideBookmarkDtoPageByMember() {
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
        MemberScore memberScore1 = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account1 = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("test-password")
                .build());
        Member member1 = memberRepository.save(Member.builder()
                .account(account1)
                .university(university)
                .department(department)
                .memberScore(memberScore1)
                .nickname("test-nickname")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());
        MemberScore memberScore2 = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account2 = accountRepository.save(Account.builder()
                .email("test2@email.com")
                .password("test-password2")
                .build());
        Member member2 = memberRepository.save(Member.builder()
                .account(account2)
                .university(university)
                .department(department)
                .memberScore(memberScore2)
                .nickname("test-nickname2")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());
        SideProject doneProject = sideProjectRepository.save(SideProject.builder()
                .name("테스트 프로젝트 1")
                .startDate(LocalDate.of(2022, 3, 2))
                .endDate(LocalDate.of(2022, 6, 30))
                .field(Field.AD_MARKETING)
                .fieldCategory(FieldCategory.CONTEST)
                .build());
        doneProject.end();
        SideProject notStartedProject = sideProjectRepository.save(SideProject.builder()
                .name("테스트 프로젝트 2")
                .startDate(LocalDate.of(2022, 9, 2))
                .endDate(LocalDate.of(2022, 12, 30))
                .field(Field.IT_SW_GAME)
                .fieldCategory(FieldCategory.EXTRA_ACTIVITY)
                .build());
        Recruiting recruiting1 = recruitingRepository.save(Recruiting.builder()
                .project(doneProject)
                .member(member1)
                .title("title1")
                .content("content1")
                .recruitingMemberCount(4)
                .recruitingType(Type.LECTURE)
                .activityArea(ActivityArea.ONLINE)
                .recruitingEndDate(LocalDate.of(2022, 3, 1))
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("")
                .build());
        Recruiting recruiting2 = recruitingRepository.save(Recruiting.builder()
                .project(notStartedProject)
                .member(member2)
                .title("title2")
                .content("content2")
                .recruitingMemberCount(2)
                .recruitingType(Type.LECTURE)
                .activityArea(ActivityArea.ONLINE)
                .recruitingEndDate(LocalDate.of(2022, 4, 1))
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("")
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .recruiting(recruiting1)
                .member(member1)
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .recruiting(recruiting2)
                .member(member1)
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .recruiting(recruiting1)
                .member(member2)
                .build());
        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<SideBookmarkDto> page = bookmarkRepository.findSideBookmarkDtoPageByMember(
                pageable, member1);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent()).extracting("title")
                .containsExactly("title1", "title2");
    }
}