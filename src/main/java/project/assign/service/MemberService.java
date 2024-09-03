package project.assign.service;


import project.assign.dto.MemberDTO;

public interface MemberService {

    int registerMember(MemberDTO memberDto);
    void checkNickName(String nickName);
    void checkEmail(String email);

}
