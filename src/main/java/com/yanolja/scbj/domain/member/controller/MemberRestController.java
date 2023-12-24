package com.yanolja.scbj.domain.member.controller;

import com.yanolja.scbj.domain.member.dto.request.MemberSignInRequest;
import com.yanolja.scbj.domain.member.dto.request.MemberSignUpRequest;
import com.yanolja.scbj.domain.member.dto.request.MemberUpdatePasswordRequest;
import com.yanolja.scbj.domain.member.dto.response.MemberResponse;
import com.yanolja.scbj.domain.member.dto.response.MemberSignInResponse;
import com.yanolja.scbj.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("api/member")
public class MemberRestController {

    private final MemberService memberService;

    MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signUp(
        @Valid @RequestBody MemberSignUpRequest memberSignUpRequest) {
        log.info("email:{}, password:{}, name:{}, phone:{}", memberSignUpRequest.email(),
            memberSignUpRequest.password(),
            memberSignUpRequest.name(), memberSignUpRequest.phone());
        return ResponseEntity.ok().body(memberService.signUp(memberSignUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<MemberSignInResponse> signIn(
        @Valid @RequestBody MemberSignInRequest memberSignInRequest) {
        log.info("email:{}, password:{}", memberSignInRequest.email(),
            memberSignInRequest.password());
        return ResponseEntity.ok().body(memberService.signIn(memberSignInRequest));
    }

    @PatchMapping("/password")
    public ResponseEntity<String> updateMemberPassword(@Valid @RequestBody
    MemberUpdatePasswordRequest memberUpdatePasswordRequest) {
        log.info("password:{}", memberUpdatePasswordRequest.password());
        memberService.updateMemberPassword(memberUpdatePasswordRequest);

        return ResponseEntity.ok().body("성공적으로 비밀번호를 변경했습니다.");
    }


}
