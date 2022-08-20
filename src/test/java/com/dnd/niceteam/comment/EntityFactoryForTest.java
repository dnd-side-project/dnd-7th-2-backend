package com.dnd.niceteam.comment;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureTime;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.recruiting.ActivityDayTime;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.university.University;

import java.time.LocalDate;
import java.time.LocalTime;
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
                .name("컴퓨터공학과")
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
                .interestingFields(Set.of(Field.IT_SW_GAME, Field.DESIGN))
                .introduction("")
                .introductionUrl("")
                .build();
    }
    public static LectureProject createLectureProject(Department department) {
        return LectureProject.builder()
                .name("project-name")
                .department(department)
                .lectureTimes(Set.of(
                        LectureTime.builder().dayOfWeek(DayOfWeek.MON).startTime(LocalTime.of(9, 0)).build(),
                        LectureTime.builder().dayOfWeek(DayOfWeek.WED).startTime(LocalTime.of(9, 30)).build()))
                .professor("test-professor")
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build();
    }
    public static SideProject createSideProject() {
        return SideProject.builder()
                .name("side")
                .field(Field.PLANNING_IDEA)
                .fieldCategory(FieldCategory.CLUB)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build();
    }
    public static Recruiting createRecruiting(Member member, Project project, Type type) {
        return Recruiting.builder()
                .member(member)
                .project(project)
                .title("test-title")
                .content("test-content")
                .recruitingMemberCount(4)
                .recruitingType(type)
                .activityDayTimes(createActivityDayTime())
                .activityArea(ActivityArea.ONLINE)
                .status(ProgressStatus.IN_PROGRESS)
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL, Personality.Adjective.GOAL_ORIENTED))
                .personalityNouns(Set.of(Personality.Noun.PERFECTIONIST, Personality.Noun.INVENTOR))
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("test-introLink")
                .build();
    }

    // TODO: 2022-08-18 중복 제거 리팩토링 필요
    public static Set<ActivityDayTime> createActivityDayTime() {
        return Set.of(ActivityDayTime.builder()
                .dayOfWeek(DayOfWeek.SAT)
                .startTime(LocalTime.of(17, 30))
                .endTime(LocalTime.of(20, 30))
                .build());
    }
}
