package com.example.prj1be20231109.controller;


import com.example.prj1be20231109.domain.Comment;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {


    private final CommentService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment,
     @SessionAttribute(value = "login",required = false) Member login
     ){

        if(login==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(service.validate(comment)) {
          if (service.add(comment,login)){
            return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
             }
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public Map<String,Object> list(
            @RequestParam("id") Integer boardId,
            @RequestParam(value = "p",defaultValue = "1") Integer page
    ){

        return service.list(boardId,page);
    }

    @DeleteMapping("{id}")
    public ResponseEntity remove(
            @PathVariable Integer id,
            @SessionAttribute(value = "login",required = false) Member login,
            HttpSession session
    ){
        if(login==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if(service.hasAccess(id,login)){
           if(service.remove(id)){
               session.invalidate();
               return ResponseEntity.ok().build();
           }else {
               return ResponseEntity.internalServerError().build();
           }
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @GetMapping("id/{id}")
    public Comment get(@PathVariable Integer id){

        return  service.get(id);
    }


    @PutMapping("edit")
    public ResponseEntity update(
    @SessionAttribute(value = "login",required = false)Member login,
    @RequestBody Comment comment
    ) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.hasAccess(comment.getId(), login)) {
            if(!service.editValidate(comment)){
                return ResponseEntity.badRequest().build();
            }

            if (service.update(comment)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      }
    }
