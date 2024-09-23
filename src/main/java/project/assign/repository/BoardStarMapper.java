package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.dto.SearchConditionDTO;
import project.assign.entity.BoardStar;

import java.util.List;

@Mapper
public interface BoardStarMapper {
    void boardStarSave(BoardStar boardStar);
    void deleteBoardStarByBoardId(int boardId);

    List<Integer>sortBoardIdByStarType(SearchConditionDTO searchConditionDTO);

}
