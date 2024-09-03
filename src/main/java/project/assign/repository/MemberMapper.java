package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import project.assign.Entity.Member;
import project.assign.dto.MemberDTO;

@Mapper
public interface MemberMapper {
    void save(Member member);
    boolean checkNickName(String nickName);
}
