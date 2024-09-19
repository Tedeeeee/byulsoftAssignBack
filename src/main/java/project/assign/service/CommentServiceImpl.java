package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.dto.CommentDTO;
import project.assign.entity.Comment;
import project.assign.entity.Member;
import project.assign.repository.CommentMapper;
import project.assign.repository.MemberMapper;
import project.assign.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public List<CommentDTO> saveComment(CommentDTO commentDTO) {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        commentDTO.setMemberId(member.getMemberId());

        // DTO -> Entity
        Comment comment = commentDTO.toEntity();
        try{
            commentMapper.saveComment(comment);
            List<Comment> comments = commentMapper.findByBoardId(comment.getBoardId());
            return comments.stream().map(CommentDTO::from).toList();
        } catch (Exception e) {
            throw new RuntimeException("입력 정보에 오류가 있습니다",e);
        }
    }

    @Override
    public List<CommentDTO> findByBoardId(int boardId) {
        List<Comment> comments = commentMapper.findByBoardId(boardId);
        return comments.stream().map(CommentDTO::from).toList();
    }

    @Override
    @Transactional
    public int deleteByCommentId(int commentId) {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        Comment comment = commentMapper.findByCommentId(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다"));

        // 프론트에서 id를 저장해서 전달한다.
        if (member.getMemberId() != comment.getMemberId()) {
            throw new RuntimeException("회원의 정보가 일치하지 않습니다");
        }
        try {
            commentMapper.deleteByCommentId(commentId);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("삭제에 실패하였습니다", e);
        }
    }

    @Override
    public List<CommentDTO> changeCommentContent(CommentDTO commentDTO) {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        Comment comment = commentMapper.findByCommentId(commentDTO.getCommentId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다"));

        if (member.getMemberId() != comment.getMemberId()) {
            throw new RuntimeException("회원의 정보가 일치하지 않습니다");
        }

        try {
            commentMapper.changeComment(commentDTO.getCommentId(), commentDTO.getCommentContent());

            List<Comment> comments = commentMapper.findByBoardId(commentDTO.getBoardId());
            return comments.stream().map(CommentDTO::from).toList();
        } catch (Exception e) {
            throw new RuntimeException("댓글 수정에 문제가 발생", e);
        }
    }
}
