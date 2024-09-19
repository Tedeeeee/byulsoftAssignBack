package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.entity.Member;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    boolean checkNickName(String memberNickName);
    boolean checkEmail(String memberEmail);
    Optional<String> findNicknameById(int memberId);
    Optional<Integer> findMemberIdByNickname(String memberNickName);
    Optional<Member> findMemberByEmail(String memberEmail);
    void saveRefreshToken(@Param("memberRefreshToken") String memberRefreshToken,@Param("memberEmail") String memberEmail);
    Optional<String> findMemberByRefreshToken(String memberRefreshToken);
    void deleteRefreshToken(String memberEmail);
}
