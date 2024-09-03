package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.dto.MemberDTO;
import project.assign.service.MemberService;

@RestController
@RequestMapping(("/member"))
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody @Valid MemberDTO memberDTO) {
        int result = memberService.registerMember(memberDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkNickName")
    public ResponseEntity<String> checkNickName(@RequestParam(name = "nickName") String nickName) {
        memberService.checkNickName(nickName);
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email) {
        memberService.checkEmail(email);
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

}
