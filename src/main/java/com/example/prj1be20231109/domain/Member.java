package com.example.prj1be20231109.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Member {


    private int mnum;
    private String id;
    private String password;
    private String email;

    private String name;
    private String nickName;
    private String gender;
    private String birth;
    private String phone;
    private String grade;
    private LocalDateTime inserted;
    private List<Auth> auth;

    public boolean isAdmin(){
        if(auth!=null){
            auth.stream()
                    .map(a->a.getName())
                    .anyMatch(n-> n.equals("admin"));
        }
        return false;
    }
}
