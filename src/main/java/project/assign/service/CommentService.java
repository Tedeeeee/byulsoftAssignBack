package project.assign.service;

import project.assign.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    int saveComment(CommentDTO commentDTO);
    List<CommentDTO> findByBoardId(int boardId);
}
