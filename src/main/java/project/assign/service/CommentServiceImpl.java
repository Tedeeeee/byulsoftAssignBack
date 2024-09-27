package project.assign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.dto.CommentDTO;
import project.assign.entity.Comment;
import project.assign.entity.Member;
import project.assign.exception.BusinessExceptionHandler;
import project.assign.mapper.CommentMapper;
import project.assign.mapper.MemberMapper;
import project.assign.util.SecurityUtil;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public List<CommentDTO> saveComment(CommentDTO commentDTO) {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다"));

        commentDTO.setMemberId(member.getMemberId());

        // DTO -> Entity
        Comment comment = commentDTO.toEntity();

        commentMapper.saveComment(comment);
        List<Comment> comments = commentMapper.findByBoardId(comment.getBoardId());
        return comments.stream().map(CommentDTO::from).toList();
    }

    @Override
    public List<CommentDTO> findByBoardId(int boardId) {
        List<Comment> comments = commentMapper.findByBoardId(boardId);
        return comments.stream().map(CommentDTO::from).toList();
    }

    @Override
    @Transactional
    public void deleteByCommentId(int commentId) {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다"));

        Comment comment = commentMapper.findByCommentId(commentId)
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "존재하지 않는 댓글입니다"));

        // 지우려는자와 삭제하려는자가 다를 경우
        if (comment.getWriter(member.getMemberId())) {
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "회원의 정보가 일치하지 않습니다.");
        }

        commentMapper.deleteByCommentId(commentId);
    }

    @Override
    public List<CommentDTO> changeCommentContent(CommentDTO commentDTO) {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다"));

        Comment comment = commentMapper.findByCommentId(commentDTO.getCommentId())
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다"));

        if (comment.getWriter(member.getMemberId())) {
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "회원의 정보가 일치하지 않습니다.");
        }

        commentMapper.changeComment(commentDTO.getCommentId(), commentDTO.getCommentContent());

        List<Comment> comments = commentMapper.findByBoardId(commentDTO.getBoardId());
        return comments.stream().map(CommentDTO::from).toList();
    }
}
