package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.dto.CommentDTO;
import project.assign.entity.Comment;
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
    public int saveComment(CommentDTO commentDTO) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        System.out.println(currentMemberId);

        int memberId = memberMapper.findMemberIdByNickname(commentDTO.getNickname());
        System.out.println(memberId);
        commentDTO.setMemberId(memberId);

        // DTO -> Entity
        Comment comment = CommentDTO.toEntity(commentDTO);

        try{
            commentMapper.saveComment(comment);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("입력 정보에 오류가 있습니다",e);
        }
    }

    @Override
    public List<CommentDTO> findByBoardId(int boardId) {
        List<Comment> comments = commentMapper.findByBoardId(boardId);
        return comments.stream().map(Comment::toDTO).toList();
    }
}
