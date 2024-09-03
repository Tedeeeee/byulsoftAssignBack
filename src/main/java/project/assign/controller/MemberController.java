package project.assign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.assign.dto.MemberDTO;
import project.assign.service.MemberService;

@RestController
@RequestMapping(("/member"))
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody MemberDTO memberDTO) {
        int N = memberService.registerMember(memberDTO);
        return ResponseEntity.ok().build();
    }

}
