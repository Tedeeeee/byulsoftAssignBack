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
        memberService.registerMember(memberRequestDTO);
        return CommonResponse.createSuccess("회원가입에 성공하였습니다");
    }

    // 이메일 체크
    @GetMapping("/emails/check")
    public CommonResponse<Integer> checkEmailAvailability(@RequestParam(name = "email") String email) {
        memberService.checkEmail(email);
        return CommonResponse.createSuccess("사용 가능한 이메일입니다");
    }

    // 닉네임 체크
    @GetMapping("/nicknames/check")
    public CommonResponse<Integer> checkNicknameAvailability(@RequestParam(name = "nickname") String nickname) {
        memberService.checkNickname(nickname);
        return CommonResponse.createSuccess("사용 가능한 닉네임입니다");
    }

    // 로그아웃시 사용자 DB의 refreshToken삭제
    @DeleteMapping("/logout")
    public CommonResponse<Integer> removeRefreshToken() {
        memberService.deleteRefreshToken();
        return CommonResponse.createSuccess("로그아웃 되었습니다");
    }
}
