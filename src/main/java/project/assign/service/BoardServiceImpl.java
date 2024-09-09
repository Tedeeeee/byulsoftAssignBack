package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.assign.dto.CommentDTO;
import project.assign.entity.Board;
import project.assign.dto.BoardDTO;
import project.assign.entity.Comment;
import project.assign.repository.BoardMapper;
import project.assign.repository.MemberMapper;
import project.assign.repository.CommentMapper;
import project.assign.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;
    private final CommentMapper commentMapper;

    @Override
    public int saveBoard(BoardDTO boardDTO) {
        int memberId = memberMapper.findMemberIdByNickname(boardDTO.getNickname());

        try {
            Board board = BoardDTO.toEntity(boardDTO, memberId);

            boardMapper.boardSave(board);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("저장에 문제 발생", e);
        }
    }

    @Override
    public List<BoardDTO> getAllBoard() {
        System.out.println(SecurityUtil.getCurrentMemberId());
        List<Board> allBoard = boardMapper.getAllBoard();

        List<BoardDTO> boardResponseDTOList = new ArrayList<>();
        for (Board board : allBoard) {
            String nickname = memberMapper.findNicknameById(board.getMemberId())
                    .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));

            // 댓글 가져오기
            List<Comment> comments = commentMapper.findByBoardId(board.getBoardId());
            List<CommentDTO> commentList = comments.stream().map(Comment::toDTO).toList();

            BoardDTO dto = Board.toDTO(board, nickname, commentList);

            boardResponseDTOList.add(dto);
        }

        return boardResponseDTOList;
    }

    @Override
    public BoardDTO findByBoardId(int id) {
        Board board = boardMapper.findByBoardId(id);
        String nickname = memberMapper.findNicknameById(board.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));

        // 댓글 가져오기
        List<Comment> comments = commentMapper.findByBoardId(board.getBoardId());
        List<CommentDTO> commentList = comments.stream().map(Comment::toDTO).toList();

        return Board.toDTO(board, nickname, commentList);
    }
}
