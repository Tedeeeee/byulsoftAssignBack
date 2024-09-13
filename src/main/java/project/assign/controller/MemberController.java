package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.dto.MemberDTO;
import project.assign.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody @Valid MemberDTO memberDTO) {
        int result = memberService.registerMember(memberDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<String> checkNickName(@RequestParam(name = "nickname") String nickname) {
        memberService.checkNickname(nickname);
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email) {
        memberService.checkEmail(email);
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    // 로그아웃을 하면 시큐리티의 contextHolder 에 있는 정보를 삭제해야한다.
    @PatchMapping("")
    public ResponseEntity<Integer> deleteRefreshToken() {
        int result = memberService.deleteRefreshToken();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck() {
        System.out.println("들어오는가?");
        return ResponseEntity.ok("헬스 체크 완료");
    }
}
