package com.example.prj1be20231109.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    @Insert("""
                INSERT INTO boardFile(boardId,fileName)
                VALUES (#{boardId},#{fileName})
                
            """)
    int insert(Integer boardId, String fileName);
}
