package project.assign.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.dto.SearchConditionDTO;
import project.assign.entity.Board;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    boolean existBoard(int boardId);
    void boardSave(Board board);
    void boardUpdate(Board board);
    void deleteBoardById(int boardId);
    int countBoards(@Param("searchType") String searchType,
                    @Param("searchText") String searchText);
    Optional<Board> findByBoardId(int boardId);
    List<Integer> getBasicBoardIdList(SearchConditionDTO searchConditionDTO);
    List<Board> getBoardListBySort(List<Integer> boards);
}
