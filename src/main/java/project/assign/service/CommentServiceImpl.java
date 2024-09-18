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
        String memberEmail = SecurityUtil.getCurrentMemberId();

        Member member = memberMapper.findMemberByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        commentDTO.setMemberId(member.getId());
        commentDTO.setNickname(member.getNickname());

        // DTO -> Entity
        Comment comment = CommentDTO.toEntity(commentDTO);
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
        SecurityUtil.getCurrentMemberId();
        try {
            commentMapper.deleteByCommentId(commentId);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("삭제에 실패하였습니다", e);
        }
    }

    @Override
    public List<CommentDTO> changeCommentContent(CommentDTO commentDTO) {
        try {
            SecurityUtil.getCurrentMemberId();

            commentMapper.changeComment(commentDTO.getId(), commentDTO.getContent());

            List<Comment> comments = commentMapper.findByBoardId(commentDTO.getBoardId());
            return comments.stream().map(CommentDTO::from).toList();
        } catch (Exception e) {
            throw new RuntimeException("댓글 수정에 문제가 발생", e);
        }


    }
}
