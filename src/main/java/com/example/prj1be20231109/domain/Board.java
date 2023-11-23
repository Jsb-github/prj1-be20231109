package com.example.prj1be20231109.domain;

import com.example.prj1be20231109.util.AppUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Insert;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    private Integer countFile;

    private List<BoardFile> files;

    public String getAgo() {
        return AppUtil.getAgo(inserted);
    }
}
