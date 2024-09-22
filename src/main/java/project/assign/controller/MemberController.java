package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.commonApi.CommonResponse;
import project.assign.dto.MemberRequestDTO;
import project.assign.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/register")
    public CommonResponse<Integer> register(@RequestBody @Valid MemberRequestDTO memberRequestDTO) {
        int result = memberService.registerMember(memberRequestDTO);
        return new CommonResponse<>(200, result, "회원가입이 완료되었습니다");
    }

    // 이메일 체크
    @GetMapping("/emails/check")
    public CommonResponse<Integer> checkEmailAvailability(@RequestParam(name = "email") String email) {
        int result = memberService.checkEmail(email);
        return new CommonResponse<>(200, result, "사용 가능한 이메일입니다");
    }

    // 닉네임 체크
    @GetMapping("/nicknames/check")
    public CommonResponse<Integer> checkNicknameAvailability(@RequestParam(name = "nickname") String nickname) {
        int result = memberService.checkNickname(nickname);
        return new CommonResponse<>(200, result, "사용 가능한 닉네임입니다");
    }

    // 로그아웃시 사용자 DB의 refreshToken삭제
    @PatchMapping("/logout")
    public CommonResponse<Integer> removeRefreshToken() {
        int result = memberService.deleteRefreshToken();
        return new CommonResponse<>(200, result);
    }
}
