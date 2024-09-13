package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    int saveComment(Comment comment);
    List<Comment> findByBoardId(int boardId);
    void deleteByCommentId(int boardId);
    void changeComment(@Param("id") int id, @Param("content") String content);
}
