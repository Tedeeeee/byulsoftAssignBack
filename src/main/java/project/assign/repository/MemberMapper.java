package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.entity.Member;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    boolean checkNickName(String nickName);
    boolean checkEmail(String email);
    Optional<String> findNicknameById(int id);
    Optional<Integer> findMemberIdByNickname(String nickName);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberById(int memberId);

    void saveRefreshToken(@Param("refreshToken") String refreshToken,@Param("email") String email);
    Optional<String> findMemberByRefreshToken(String refreshToken);
    void deleteRefreshToken(String memberEmail);
}
