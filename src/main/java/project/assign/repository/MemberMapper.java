package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.Entity.Member;
import project.assign.dto.MemberDTO;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    boolean checkNickName(String nickName);
    boolean checkEmail(String email);
    Optional<Member> findByEmail(String email);
    void saveRefreshToken(@Param("refreshToken") String refreshToken,@Param("email") String email);
    Optional<String> findByRefreshToken(String refreshToken);
}
