package com.example.prj1be20231109.mapper;

import com.example.prj1be20231109.domain.Board;
import org.apache.ibatis.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
                INSERT INTO board(title,content,writer)
                VALUES (#{title}, #{content},#{writer})
            """)
    int insert(Board board);



    @Select("""
            SELECT b.id,
                   b.title,
                   m.nickName,
                   b.writer,
                   b.inserted,
                   COUNT(DISTINCT c.id) as countComment,
                   COUNT(DISTINCT l.id)  as countLike
            FROM board b join member m ON b.writer=m.id
                         LEFT JOIN comment c on b.id=c.boardId
                         LEFT JOIN boardlike l on b.id = l.boardId
            GROUP BY b.id
            ORDER BY b.id DESC;
            """)
    List<Board> selectAll();

    @Select("""
        SELECT b.id, b.title, b.content, m.nickName,b.writer, b.inserted
        FROM board b join  member m ON b.writer= m.id
        WHERE b.id = #{id}
        """)
    Board selectById(Integer id);

    @Delete("""
            DELETE FROM board
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Update("""
            UPDATE board
            SET title = #{title},
                content =#{content},
                writer = #{writer},
                inserted = NOW()
                WHERE id = #{id}
            """)
    int update(Board board);

    @Delete("""
                DELETE FROM board
                WHERE writer=#{writer}
            """)
    int deleteByWriter(String writer);


    @Select("""
                SELECT id
                FROM board
                WHERE writer=#{writer}
            """)
    List<Integer> selectIdListByMemberId(String writer);
}
