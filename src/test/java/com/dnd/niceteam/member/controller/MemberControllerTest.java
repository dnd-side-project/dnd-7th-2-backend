package com.dnd.niceteam.member.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.code.ReviewTag;
import com.dnd.niceteam.member.dto.DupCheck;
import com.dnd.niceteam.member.dto.MemberCreation;
import com.dnd.niceteam.member.dto.MemberDetail;
import com.dnd.niceteam.member.dto.MemberUpdate;
import com.dnd.niceteam.member.service.MemberService;
import com.dnd.niceteam.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = MemberController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService mockMemberService;

    @Test
    @WithMockUser
    @DisplayName("이메일 중복 확인 API")
    void memberEmailDupCheck() throws Exception {
        // given
        DupCheck.ResponseDto responseDto = new DupCheck.ResponseDto();
        responseDto.setDuplicated(false);
        given(mockMemberService.checkEmailDuplicate("test@email.com")).willReturn(responseDto);

        // expected
        mockMvc.perform(get("/members/dup-check/email")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "test@email.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.duplicated").value(false))
                .andDo(document("member-email-dup-check",
                        requestParameters(
                                parameterWithName("email").description("중복 확인 하려는 이메일")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("duplicated").description("중복 여부. 중복이 있을 경우 true.")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("닉네임 중복 확인 API")
    void memberNicknameDupCheck() throws Exception {
        // given
        DupCheck.ResponseDto responseDto = new DupCheck.ResponseDto();
        responseDto.setDuplicated(false);
        given(mockMemberService.checkNicknameDuplicate("테스트닉네임")).willReturn(responseDto);

        // expected
        mockMvc.perform(get("/members/dup-check/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("nickname", "테스트닉네임"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.duplicated").value(false))
                .andDo(document("member-nickname-dup-check",
                        requestParameters(
                                parameterWithName("nickname").description("중복 확인 하려는 이메일")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("duplicated").description("중복 여부. 중복이 있을 경우 true.")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("회원 가입 API")
    void memberJoin() throws Exception {
        // given
        MemberCreation.RequestDto requestDto = new MemberCreation.RequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("Password123!@#");
        requestDto.setNickname("테스트닉네임");
        requestDto.setPersonalityAdjective(Personality.Adjective.LOGICAL);
        requestDto.setPersonalityNoun(Personality.Noun.LEADER);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setDepartmentId(1L);
        requestDto.setAdmissionYear(2017);
        requestDto.setIntroduction("자기소개");
        requestDto.setIntroductionUrl("http://테스트-자기소개-링크.com");

        MemberCreation.ResponseDto responseDto = new MemberCreation.ResponseDto();
        responseDto.setId(1L);
        responseDto.setEmail("test@email.com");
        given(mockMemberService.createMember(requestDto)).willReturn(responseDto);

        // expected
        mockMvc.perform(post("/members").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("member-join",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("회원 비밀번호")
                                        .attributes(key("constraint")
                                                .value("8~16자 영문 대/소문자, 숫자 사용")),
                                fieldWithPath("nickname").description("회원 닉네임")
                                        .attributes(key("constraint").value("1~10자 공백없이 한글만 사용")),
                                fieldWithPath("personalityAdjective.code").description("성향 형용사 코드")
                                        .attributes(key("constraint").value(Personality.Adjective.values())),
                                fieldWithPath("personalityAdjective.title").description("성향 형용사"),
                                fieldWithPath("personalityNoun.code").description("성향 명사 코드")
                                        .attributes(key("constraint").value(Personality.Noun.values())),
                                fieldWithPath("personalityNoun.title").description("성향 명사"),
                                fieldWithPath("interestingFields[].code").description("관심 분야 코드")
                                        .attributes(key("constraint")
                                        .value(Arrays.toString(Field.values()) + " 이 중 최대 3개")),
                                fieldWithPath("interestingFields[].title").description("관심 분야"),
                                fieldWithPath("departmentId").description("회원 학과 -> 이를 통해 대학교도 알 수 있음"),
                                fieldWithPath("admissionYear").description("입학 년도 (학번)"),
                                fieldWithPath("introduction").description("자기소개")
                                        .attributes(key("constraint").value("없을 경우 null 이 아닌 \"\" 로 처리")),
                                fieldWithPath("introductionUrl").description("자기소개 링크")
                                        .attributes(key("constraint").value("없을 경우 null 이 아닌 \"\" 로 처리"))
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("회원 DB ID"),
                                fieldWithPath("email").description("회원 이메일")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("회원 수정 API")
    void memberUpdate() throws Exception {
        // given
        MemberUpdate.RequestDto requestDto = new MemberUpdate.RequestDto();
        requestDto.setNickname("테스트닉네임");
        requestDto.setPersonalityAdjective(Personality.Adjective.LOGICAL);
        requestDto.setPersonalityNoun(Personality.Noun.LEADER);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setIntroduction("자기소개");
        requestDto.setIntroductionUrl("http://테스트-자기소개-링크.com");

        MemberUpdate.ResponseDto responseDto = new MemberUpdate.ResponseDto();
        responseDto.setId(1L);
        given(mockMemberService.updateMember(anyString(), eq(requestDto))).willReturn(responseDto);

        // expected
        mockMvc.perform(put("/members").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("member-update",
                        requestFields(
                                fieldWithPath("nickname").description("회원 닉네임")
                                        .attributes(key("constraint").value("1~10자 공백없이 한글만 사용")),
                                fieldWithPath("personalityAdjective.code").description("성향 형용사 코드")
                                        .attributes(key("constraint").value(Personality.Adjective.values())),
                                fieldWithPath("personalityAdjective.title").description("성향 형용사"),
                                fieldWithPath("personalityNoun.code").description("성향 명사 코드")
                                        .attributes(key("constraint").value(Personality.Noun.values())),
                                fieldWithPath("personalityNoun.title").description("성향 명사"),
                                fieldWithPath("interestingFields[].code").description("관심 분야 코드")
                                        .attributes(key("constraint")
                                                .value(Arrays.toString(Field.values()) + " 이 중 최대 3개")),
                                fieldWithPath("interestingFields[].title").description("관심 분야"),
                                fieldWithPath("introduction").description("자기소개")
                                        .attributes(key("constraint").value("없을 경우 null 이 아닌 \"\" 로 처리")),
                                fieldWithPath("introductionUrl").description("자기소개 링크")
                                        .attributes(key("constraint").value("없을 경우 null 이 아닌 \"\" 로 처리"))
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("회원 DB ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("회원 상세 조회 API")
    void memberDetail() throws Exception {

        // given
        MemberDetail.ResponseDto responseDto = new MemberDetail.ResponseDto();
        responseDto.setId(1L);
        responseDto.setNickname("테스트닉네임");
        responseDto.setPersonality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.ANALYST));
        responseDto.setDepartmentName("테스트학과");
        responseDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.DESIGN));
        responseDto.setAdmissionYear(2017);
        responseDto.setIntroduction("테스트자기소개");
        responseDto.setIntroductionUrl("test.com");
        responseDto.setLevel(1);
        responseDto.setParticipationPct(100.0);
        responseDto.setReviewTagToNums(new HashMap<>(
                IntStream.range(0, ReviewTag.values().length).boxed()
                        .collect(Collectors.toMap(i -> ReviewTag.values()[i], i -> 0))
        ));
        responseDto.setNumTotalEndProject(0);
        responseDto.setNumCompleteProject(0);
        given(mockMemberService.getMemberDetail(anyLong())).willReturn(responseDto);

        // expected
        mockMvc.perform(get("/members/{memberId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Access token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("member-detail",
                        pathParameters(
                                parameterWithName("memberId").description("조회하려는 회원 ID")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("회원 DB ID"),
                                fieldWithPath("nickname").description("회원 닉네임"),
                                subsectionWithPath("personality").description("회원 성향"),
                                fieldWithPath("departmentName").description("학과 명"),
                                fieldWithPath("interestingFields[].code").description("관심 분야 코드"),
                                fieldWithPath("interestingFields[].title").description("관심 분야"),
                                fieldWithPath("admissionYear").description("입학년도(학번)"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("introductionUrl").description("자기소개 링크"),
                                fieldWithPath("level").description("레벨"),
                                fieldWithPath("participationPct").description("참여율"),
                                subsectionWithPath("reviewTagToNums").description("리뷰 개수"),
                                fieldWithPath("numTotalEndProject").description("전체 종료된 프로젝트 수"),
                                fieldWithPath("numCompleteProject").description("완주한 프로젝트 수")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("본인 회원 상세 조회 API")
    void memberMyDetail() throws Exception {

        // given
        MemberDetail.ResponseDto responseDto = new MemberDetail.ResponseDto();
        responseDto.setId(1L);
        responseDto.setNickname("테스트닉네임");
        responseDto.setPersonality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.ANALYST));
        responseDto.setDepartmentName("테스트학과");
        responseDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.DESIGN));
        responseDto.setAdmissionYear(2017);
        responseDto.setIntroduction("테스트자기소개");
        responseDto.setIntroductionUrl("test.com");
        responseDto.setLevel(1);
        responseDto.setParticipationPct(100.0);
        responseDto.setReviewTagToNums(new HashMap<>(
                IntStream.range(0, ReviewTag.values().length).boxed()
                        .collect(Collectors.toMap(i -> ReviewTag.values()[i], i -> 0))
        ));
        responseDto.setNumTotalEndProject(0);
        responseDto.setNumCompleteProject(0);
        given(mockMemberService.getMyMemberDetail(anyString())).willReturn(responseDto);

        // expected
        mockMvc.perform(get("/members/me")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Access token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("member-my-detail",
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("회원 DB ID"),
                                fieldWithPath("nickname").description("회원 닉네임"),
                                subsectionWithPath("personality").description("회원 성향"),
                                fieldWithPath("departmentName").description("학과 명"),
                                fieldWithPath("interestingFields[].code").description("관심 분야 코드"),
                                fieldWithPath("interestingFields[].title").description("관심 분야"),
                                fieldWithPath("admissionYear").description("입학년도(학번)"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("introductionUrl").description("자기소개 링크"),
                                fieldWithPath("level").description("레벨"),
                                fieldWithPath("participationPct").description("참여율"),
                                subsectionWithPath("reviewTagToNums").description("리뷰 개수"),
                                fieldWithPath("numTotalEndProject").description("전체 종료된 프로젝트 수"),
                                fieldWithPath("numCompleteProject").description("완주한 프로젝트 수")
                        )
                ));
    }
}