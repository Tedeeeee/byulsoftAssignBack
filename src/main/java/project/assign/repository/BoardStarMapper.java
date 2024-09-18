package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.entity.BoardStar;

import java.util.List;

@Mapper
public interface BoardStarMapper {
    void boardStarSave(BoardStar boardStar);
    void deleteBoardStarByBoardId(int boardId);
    List<Integer>sortASCBoardIdByStarType(@Param("boardStarType") String boardStarType, @Param("offset") int offset);
    List<Integer>sortDESCBoardIdByStarType(@Param("boardStarType") String boardStarType, @Param("offset") int offset);

    void boardStarUpdate(BoardStar boardStar);

    List<BoardStar> getBoardStarByBoardId(int boardId);

    List<BoardStar> sortTest(int boardId);

}
