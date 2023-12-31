package com.example.prj1be20231109.controller;


import com.example.prj1be20231109.domain.Board;
import com.example.prj1be20231109.domain.BoardFile;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
;import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
    public ResponseEntity add (
            Board board,
            @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
            @SessionAttribute(value = "login",required = false) Member login
    )throws IOException {


        if(login==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        if(!service.validate(board)){
            return ResponseEntity.badRequest().build();
        }


       if(service.save(board,files,login)){
          return ResponseEntity.ok().build();
       }else {
          return ResponseEntity.internalServerError().build();
       }
    }

    @GetMapping("list")
    public Map<String, Object> list(
    @RequestParam(value = "p",defaultValue = "1") Integer page,
    @RequestParam(value= "k",defaultValue = "") String keyword,
    @RequestParam(value = "c",defaultValue = "all") String category
    ){
        return service.select(page,keyword,category);
    }


    @GetMapping("id/{id}")
    public Board get(@PathVariable Integer id){
        return  service.get(id);
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(
            @PathVariable Integer id,
            @SessionAttribute(value="login", required=false) Member login
    ){

        if(login==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }


        if(!service.hasAccess(id, login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 권한
        }

        if(service.remove(id)){

            return  ResponseEntity.ok().build();
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("update")
    public ResponseEntity update(
             Board board,
            @RequestParam(value = "removeFileIds[]", required = false) List<Integer> removeFileIds,
            @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFiles,
            @SessionAttribute(value = "login",required = false) Member login
    )throws IOException {


            if (login == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401
            }



        if(!service.hasAccess(board.getId(),login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }

        if(!service.validate(board)){
            return ResponseEntity.badRequest().build();
        }


            if (service.update(board,uploadFiles,removeFileIds)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        }

    }


