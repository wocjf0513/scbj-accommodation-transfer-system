package com.yanolja.scbj.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yanolja.scbj.domain.member.dto.request.MemberSignInRequest;
import com.yanolja.scbj.domain.member.dto.request.MemberSignUpRequest;
import com.yanolja.scbj.domain.member.dto.request.MemberUpdateAccountRequest;
import com.yanolja.scbj.domain.member.dto.request.MemberUpdatePasswordRequest;
import com.yanolja.scbj.domain.member.dto.response.MemberResponse;
import com.yanolja.scbj.domain.member.dto.response.MemberSignInResponse;
import com.yanolja.scbj.domain.member.entity.Member;
import com.yanolja.scbj.domain.member.repository.MemberRepository;
import com.yanolja.scbj.domain.member.util.MemberMapper;
import com.yanolja.scbj.global.config.jwt.JwtUtil;
import com.yanolja.scbj.global.util.SecurityUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityUtil securityUtil;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTests {

        private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        private String testRawPassword = "test1234@";
        private Member testMember = Member.builder()
            .id(1L)
            .email("test@gmail.com")
            .password(bCryptPasswordEncoder.encode(testRawPassword))
            .name("test")
            .phone("010-1234-5678")
            .build();

        @Test
        void signUp() {
            //given
            MemberSignUpRequest memberSignUpRequest = MemberSignUpRequest.builder()
                .email(testMember.getEmail())
                .name(testMember.getName())
                .password(testRawPassword)
                .phone(testMember.getPhone())
                .build();

            MemberResponse expectedMemberResponse = MemberResponse.builder()
                .email(testMember.getEmail())
                .name(testMember.getName())
                .phone(testMember.getPhone())
                .id(testMember.getId())
                .build();

            given(memberRepository.existsByEmail(any(String.class))).willReturn(false);
            given(memberRepository.save(any(Member.class))).willReturn(testMember);
            //when & then
            assertThat(expectedMemberResponse).usingRecursiveComparison()
                .isEqualTo(memberService.signUp(memberSignUpRequest));

        }

        @Test
        void signIn() {
            //given
            MemberSignInRequest memberSignInRequest = MemberSignInRequest.builder()
                .email(testMember.getEmail())
                .password(testRawPassword)
                .build();

            MemberSignInResponse memberSignInResponse = MemberSignInResponse.builder()
                .memberResponse(MemberMapper.toMemberResponse(testMember))
                .tokenResponse(MemberMapper.toTokenResponse("", ""))
                .build();
            given(memberRepository.findByEmail(any())).willReturn(Optional.of(testMember));
            given(passwordEncoder.matches(any(), any())).willReturn(true);
            given(jwtUtil.generateToken(any())).willReturn("");
            given(jwtUtil.generateRefreshToken(any())).willReturn("");
            //when & then
            assertThat(memberSignInResponse).usingRecursiveComparison()
                .isEqualTo(memberService.signIn(memberSignInRequest));

        }

        @Test
        void updateMemberPassword() {
            //given
            String changedPassword = "test1234!";
            MemberUpdatePasswordRequest memberUpdatePasswordRequest = MemberUpdatePasswordRequest.builder()
                .password(changedPassword).build();
            given(memberRepository.findById(any())).willReturn(Optional.of(testMember));
            given(passwordEncoder.encode(changedPassword)).willReturn(
                bCryptPasswordEncoder.encode(changedPassword));
            //when
            memberService.updateMemberPassword(memberUpdatePasswordRequest);
            //then
            assertEquals(testMember.getPassword(), bCryptPasswordEncoder.encode(changedPassword));
        }

        @Test
        void updateMemberAccount() {
            //given
            MemberUpdateAccountRequest memberUpdateAccountRequest = MemberUpdateAccountRequest.builder()
                .accountNumber("123-456-789")
                .bank("농협")
                .build();

            given(memberRepository.findById(any())).willReturn(Optional.of(testMember));
            //when
            memberService.updateMemberAccount(memberUpdateAccountRequest);
            //then
            assertEquals(testMember.getAccountNumber(), memberUpdateAccountRequest.accountNumber());
            assertEquals(testMember.getBank(), memberUpdateAccountRequest.bank());

        }
    }
}