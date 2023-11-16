package com.example.prj1be20231109.mapper;

import com.example.prj1be20231109.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
            INSERT INTO comment(boardId,comment,memberId)
            VALUES (#{boardId},#{comment},#{memberId})
            """)
    int add(Comment comment);

    @Select("""
              SELECT
            c.id,
            c.comment,
            c.inserted,
            c.boardId,
            c.memberId,
            m.nickName
            FROM comment c JOIN  member m
            on  c.memberId = m.id
            WHERE boardId =  #{boardId}
            ORDER BY c.id DESC ;
            """)
    List<Comment> selectByBoardId(Integer boardId);

    @Delete("""
                DELETE FROM comment
                WHERE id=#{id}
            """)
    int deleteById(Integer id);

    @Select("""
        SELECT * FROM comment
        WHERE id = #{id}
        """)
    Comment selectById(Integer id);

    @Update("""
            UPDATE comment
            SET comment = #{comment},
                inserted = NOW()
                WHERE id=#{id}
           
            """)
    int update(Comment comment);



}
