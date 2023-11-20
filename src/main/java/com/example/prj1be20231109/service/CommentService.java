package com.example.prj1be20231109.service;

import com.example.prj1be20231109.domain.Comment;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommentService {

    private final CommentMapper mapper;



    public boolean add(Comment comment, Member login) {
        comment.setMemberId(login.getId());
       return mapper.add(comment)==1;
    }

    public boolean validate(Comment comment) {
        if (comment==null){
            return false;
        }

        if (comment.getBoardId() == null || comment.getBoardId()<1){
            return false;
        }

        if(comment.getComment() == null || comment.getComment().isBlank()){
            return false;
        }

        return true;
    }

    public Map<String,Object> list(Integer boardId, Integer page) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = mapper.countALl(boardId); // 총 코멘트 수
        int lastPageNumber = (countAll -1)/5+1; // 마지막 페이지 번호
        int startPageNumber= (page-1) / 5 * 5 +1;
        int endPageNumber= startPageNumber + 4;
            endPageNumber=Math.min(endPageNumber,lastPageNumber);

        int prevPageNumber = startPageNumber-5;

        int nextPageNumber = endPageNumber+1;

        pageInfo.put("currentPageNumber",page);
        pageInfo.put("startPageNumber",startPageNumber);
        pageInfo.put("endPageNumber",endPageNumber);
        pageInfo.put("lastPageNumber",lastPageNumber);
        if(prevPageNumber >0){
            pageInfo.put("prevPageNumber",prevPageNumber);
        }

        if(nextPageNumber<= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }
        int from = (page-1) *5;

        map.put("commentList",mapper.selectByBoardId(boardId,from));
        map.put("pageInfo",pageInfo);

        return map;
    }

    public boolean remove(Integer id) {

        return  mapper.deleteById(id)==1;
    }

    public boolean hasAccess(Integer id, Member login) {
        if (login== null){
            return false;
        }


        if (login.isAdmin()){
            return true;
        }
        Comment comment = mapper.selectById(id);

        return  comment.getMemberId().equals(login.getId());
    }

    public Comment get(Integer id) {
      return   mapper.selectById(id);
    }

    public boolean update(Comment comment) {

        return mapper.update(comment)==1;
    }

    public boolean editValidate(Comment comment) {

        if(comment==null){
            return  false;
        }

        if(comment.getId() == null ){
            return false;
        }

        if(comment.getComment() == null || comment.getComment().isBlank()){
            return false;
        }
        return true;
    }
}
