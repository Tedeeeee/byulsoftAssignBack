package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import project.assign.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    int saveComment(Comment comment);
    List<Comment> findByBoardId(int boardId);
}
