package com.example.prj1be20231109.mapper;

import com.example.prj1be20231109.domain.BoardFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("""
                INSERT INTO boardFile(boardId,fileName)
                VALUES (#{boardId},#{fileName})
                
            """)
    int insert(Integer boardId, String fileName);


    @Select("""
                SELECT id,fileName
                FROM boardfile
                WHERE boardId = #{boardId}
            """)
    List<BoardFile> selectNamesByBoardId(Integer boardId);


    @Delete("""
                DELETE FROM boardfile
                WHERE boardId= #{boardId}        
             """)
    int deleteByFileBoardId(Integer boardId);

    @Select("""
            SELECT * FROM boardfile
            WHERE id = #{id}
            """)
    BoardFile selectById(Integer id);


    @Delete("""
                DELETE FROM boardfile
                WHERE id = #{id}
            """)
    int deleteById(Integer id);
}
