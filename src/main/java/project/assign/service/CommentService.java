package project.assign.service;

import project.assign.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> saveComment(CommentDTO commentDTO);
    List<CommentDTO> findByBoardId(int boardId);
    int deleteByCommentId(int commentId);
    List<CommentDTO> changeCommentContent(CommentDTO commentDTO);
}
