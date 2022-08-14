package com.dnd.niceteam.comment;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureTime;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.university.University;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

// service test 시 필요한 Entity 생성 util (임시로 Comment 패키지에)
public class EntityFactoryForTest {
    public static University createUniversity() {
        return University.builder()
                .name("테스트대학교")
                .emailDomain("email.com")
                .build();
    }

    public static Department createDepartment(University university){
        return Department.builder()
                .university(university)
                .collegeName("단과대학")
                .name("학과")
                .region("서울")
                .mainBranchType("본교")
                .build();
    }

    public static MemberScore createMemberScore() {
        return MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build();
    }
    public static Account createAccount() {
        return Account.builder()
                .email("test@email.com")
                .password("testPassword123!@#")
                .build();
    }

    public static Member createMember(Account account, University univ, Department department, MemberScore memberScore) {
        return Member.builder()
                .account(account)
                .university(univ)
                .department(department)
                .memberScore(memberScore)
                .nickname("테스트닉네임")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build();
    }
    public static LectureProject createLectureProject(Department department) {
        return LectureProject.builder()
                .name("project-name")
                .department(department)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .lectureTimes(Set.of(
                        LectureTime.builder().dayOfWeek(DayOfWeek.MON).startTime(LocalTime.of(9, 0)).build(),
                        LectureTime.builder().dayOfWeek(DayOfWeek.WED).startTime(LocalTime.of(9, 30)).build()))
                .professor("test-professor")
                .build();
    }

    public static Recruiting createRecruiting(Member member, Project project) {
        return Recruiting.builder()
                .member(member)
                .project(project)
                .title("test-title")
                .content("test-content")
                .recruitingMemberCount(4)
                .recruitingType(Type.LECTURE)
                .activityArea(ActivityArea.ONLINE)
                .status(ProgressStatus.IN_PROGRESS)
                .personalities(createPersonalities())
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("test-introLink")
                .build();
    }

    // TODO: 2022-08-17 짝이 맞지 않게 들어오는 경우 생각 필요
    private static Set<Personality> createPersonalities() {
        Set<Personality> personalities = new HashSet<>();
        personalities.add(Personality.builder().adjective(Personality.Adjective.LOGICAL).noun(Personality.Noun.PERFECTIONIST).build());
        personalities.add(Personality.builder().adjective(Personality.Adjective.GOAL_ORIENTED).noun(Personality.Noun.INVENTOR).build());
        return personalities;
    }

}