package project.assign.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.assign.dto.SearchConditionDTO;
import project.assign.entity.BoardStar;

import java.util.List;

@Mapper
public interface BoardStarMapper {
    void boardStarSave(BoardStar boardStar);
    void boardStarSaveAll(List<BoardStar> boardStars);
    void deleteBoardStarByBoardId(int boardId);
    List<Integer>sortBoardIdByStarType(SearchConditionDTO searchConditionDTO);
}
