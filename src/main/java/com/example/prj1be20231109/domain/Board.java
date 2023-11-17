package com.example.prj1be20231109.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Insert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    private Integer id;


    private String title;

    private String content;

    private String writer;
    private String nickName;
    private LocalDateTime inserted;
    private Integer countComment;
    private Integer countLike;
}
