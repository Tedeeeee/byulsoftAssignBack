package project.assign.service;

import project.assign.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    /**
     * 설명 : 댓글 저장
     * @since : 2024.09.10
     * @author : T.S YUN
     * @param commentDTO
     */
    List<CommentDTO> saveComment(CommentDTO commentDTO);

    /**
     * 설명 : 특정 게시글 댓글 불러오기
     * @since : 2024.09.10
     * @author : T.S YUN
     * @param boardId
     */
    List<CommentDTO> findByBoardId(int boardId);

    /**
     * 설명 : 특정 댓글 삭제
     * @since : 2024.09.10
     * @author : T.S YUN
     * @param commentId
     */
    void deleteByCommentId(int commentId);

    /**
     * 설명 : 특정 댓글 수정
     * @since : 2024.09.10
     * @author : T.S YUN
     * @param commentDTO
     */
    List<CommentDTO> changeCommentContent(CommentDTO commentDTO);
}
