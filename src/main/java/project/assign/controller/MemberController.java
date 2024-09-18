package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.dto.MemberRequestDTO;
import project.assign.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody @Valid MemberRequestDTO memberRequestDTO) {
        int result = memberService.registerMember(memberRequestDTO);
        return ResponseEntity.ok(result);
    }

    // 닉네임 체크
    @GetMapping("/nicknames/check")
    public ResponseEntity<String> checkNicknameAvailability(@RequestParam(name = "nickname") String nickname) {
        memberService.checkNickname(nickname);
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    // 이메일 체크
    @GetMapping("/emails/check")
    public ResponseEntity<String> checkEmailAvailability(@RequestParam(name = "email") String email) {
        memberService.checkEmail(email);
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    // 로그아웃시 사용자 DB의 refreshToken삭제
    // 추후 logout으로 변경
    @PatchMapping("/logout")
    public ResponseEntity<Integer> removeRefreshToken() {
        int result = memberService.deleteRefreshToken();
        return ResponseEntity.ok(result);
    }

}
