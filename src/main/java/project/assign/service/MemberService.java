package project.assign.service;


import jakarta.servlet.http.HttpServletResponse;
import project.assign.dto.MemberRequestDTO;

public interface MemberService {

    /**
     * 설명 : 회원가입
     * @since : 2024.09.06
     * @author : T.S YUN
     * @param memberRequestDto
     */
    void registerMember(MemberRequestDTO memberRequestDto);

    /**
     * 설명 : 닉네임 중복 체크
     * @since : 2024.09.06
     * @author : T.S YUN
     * @param nickname
     */
    void checkNickname(String nickname);

    /**
     * 설명 : 이메일 체크
     * @since : 2024.09.10
     * @author : T.S YUN
     * @param email
     */
    void checkEmail(String email);

    /**
     * 설명 : 로그아웃 시 refreshToken 삭제
     * @since : 2024.09.10
     * @author : T.S YUN
     */
    void deleteRefreshToken(HttpServletResponse response);
}
