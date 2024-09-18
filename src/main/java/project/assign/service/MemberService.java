package project.assign.service;


import project.assign.dto.MemberDTO;

public interface MemberService {
    int registerMember(MemberDTO memberDto);

    // 닉네임 중복 체크

    /**
     *
     * 설명 :
     * @since : 2024.09.06
     * @author : T.S YUN
     * @param nickname
     */
    void checkNickname(String nickname);

    /**
     * 
     * 설명 :
     * @since : 2024.09.10
     * @author : T.S YUN
     * @param email
     */
    void checkEmail(String email);

    /**
     * 
     * 설명 :
     * @since : 2024.09.10
     * @author : T.S YUN
     * @return
     */
    int deleteRefreshToken();
}
