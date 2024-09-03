package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import project.assign.dto.MemberDTO;

@Mapper
public interface MemberMapper {
    void save(MemberDTO memberDTO);
}
