package com.example.prj1be20231109.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    @PostMapping("add")
    public void add(){
        System.out.println("BoardController.add");

    }

}
