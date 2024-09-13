package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.entity.BoardStar;

import java.util.List;

@Mapper
public interface BoardStarMapper {
    int boardStarSave(BoardStar boardStar);
    int boardStarUpdate(BoardStar boardStar);

    List<BoardStar> getBoardStarByBoardId(int boardId);

    List<Integer>sortASCBoardIdByStarType(@Param("typeName") String typeName, @Param("offset") int offset);

    List<Integer>sortDESCBoardIdByStarType(@Param("typeName") String typeName, @Param("offset") int offset);

    List<BoardStar> sortTest(int boardId);
}
