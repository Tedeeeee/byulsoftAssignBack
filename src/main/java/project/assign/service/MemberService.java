package project.assign.service;


import project.assign.Entity.Member;
import project.assign.dto.MemberDTO;

public interface MemberService {

    int registerMember(MemberDTO memberDto);
}
