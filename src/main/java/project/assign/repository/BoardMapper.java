package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.entity.Board;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void boardSave(Board board);
    void boardUpdate(Board board);
    void deleteBoardById(int boardId);
    int countBoards(@Param("searchType") String searchType,
                    @Param("searchText") String searchText);
    Optional<Board> findByBoardId(int boardId);
    List<Integer> getBasicBoardList(@Param("searchType") String searchType,
                                    @Param("searchText") String searchText,
                                    @Param("offset")int offset);
    Board getBoardListBySort(int boardId);
    List<Board> getBoardListBySortTest(List<Integer> boards);
}
