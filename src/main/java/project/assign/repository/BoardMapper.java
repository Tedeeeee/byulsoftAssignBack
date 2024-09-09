package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import project.assign.entity.Board;

import java.util.List;

@Mapper
public interface BoardMapper {
    int boardSave(Board board);

    List<Board> getAllBoard();

    Board findByBoardId(int id);
}
