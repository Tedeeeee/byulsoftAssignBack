package project.assign.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.assign.entity.Comment;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    void saveComment(Comment comment);
    List<Comment> findByBoardId(int boardId);
    void deleteByCommentId(int boardId);
    void changeComment(@Param("commentId") int commentId, @Param("commentContent") String commentContent);
    Optional<Comment> findByCommentId(int commentId);
}
