package project.assign.mapper;

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
    Optional<Member> findMemberByEmail(String memberEmail);
    void saveRefreshToken(@Param("memberRefreshToken") String memberRefreshToken,@Param("memberEmail") String memberEmail);
    Optional<Member> findMemberByRefreshToken(String memberRefreshToken);
    void deleteRefreshToken(String memberEmail);
}
