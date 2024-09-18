package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.dto.BoardResponseDTO;
import project.assign.entity.Board;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void boardSave(Board board);
    void boardUpdate(Board board);

    int countBoards();

    List<Integer> boardPagination(int offset);

    Optional<Board> findByBoardId(int id);

    void deleteBoardById(int id);

    Board getBoardWithBoardStar(int boardId);
    List<Board> getTest(List<Integer> boards);

    List<Board> temp(@Param("typeName") String typeName,@Param("pageOffset") int pageOffset);
}
