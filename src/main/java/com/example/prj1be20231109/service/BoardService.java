package com.example.prj1be20231109.service;

import com.example.prj1be20231109.domain.Board;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.mapper.BoardMapper;
import com.example.prj1be20231109.mapper.CommentMapper;
import com.example.prj1be20231109.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper mapper;

    private final CommentMapper commentMapper;

    private final LikeMapper likeMapper;

    public boolean save(Board board, Member login) {
        board.setWriter(login.getId());
        return mapper.insert(board)==1;
    }

    public boolean validate(Board board) {
        if(board == null){
            return  false;
        }

        if(board.getContent() == null || board.getContent().isBlank()){
            return false;
        }

        if(board.getTitle()==null || board.getTitle().isBlank()){
            return false;
        }



        return true;
    }

    public List<Board> select(Integer page) {
        int from = (page-1) *10;
        return mapper.selectAll(from);
    }

    public Board get(Integer id) {

        return mapper.selectById(id);
    }

    public boolean remove(Integer id) {

        // 1. 게시물에  달린 댓글 삭제
        commentMapper.deleteByCommentId(id);

        // 2. 게시물에 달린 좋아요 삭제
        likeMapper.deleteByLikeBoardId(id);

        return mapper.deleteById(id) ==1;
    }

    public boolean update(Board board) {

        return mapper.update(board) ==1;
    }

    public boolean hasAccess(Integer id, Member login) {
        if(login == null){
            return false;
        }

        if(login.isAdmin()){
            return true;
        }

        Board board = mapper.selectById(id);

        return board.getWriter().equals(login.getId());
    }



}
