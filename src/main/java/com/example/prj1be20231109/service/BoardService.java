package com.example.prj1be20231109.service;

import com.example.prj1be20231109.domain.Board;

import com.example.prj1be20231109.domain.BoardFile;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.mapper.BoardMapper;
import com.example.prj1be20231109.mapper.CommentMapper;
import com.example.prj1be20231109.mapper.FileMapper;
import com.example.prj1be20231109.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BoardService {

    private final BoardMapper mapper;

    private final CommentMapper commentMapper;

    private final LikeMapper likeMapper;
    private final FileMapper fileMapper;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    private final S3Client s3;

    public boolean save(Board board, MultipartFile[] files, Member login) throws IOException {
        //
        board.setWriter(login.getId());

        int cnt = mapper.insert(board);

        // boardFile 테이블에 files 정보 저장
        if(files != null){
            for(int i=0; i<files.length; i++){
                // id(pk) , boardId, name
                fileMapper.insert(board.getId(),files[i].getOriginalFilename());
                // 실제 파일은 S3 bucket에 upload
                // 일단 local에 저장
                upload(board.getId(),files[i]);
            }
        }
        return cnt == 1;

    }

    private void upload(Integer boardId, MultipartFile file) throws IOException {
        // 파일 저장 경로
        // c:\Temp\prj1\게시물번호\파일명
        String key = "prj1/" + boardId + "/" + file.getOriginalFilename();
//
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(),file.getSize()));


    }

    public boolean validate(Board board) {
        if(board == null){
            return  false;
        }

        if(board.getContent() == null || board.getContent().isBlank()){
            return false;
        }

        if(board.getTitle()==null || board.getTitle().isBlank()){
            return false;
        }



        return true;
    }

    public Map<String,Object> select(Integer page, String keyword, String category) {
       Map<String,Object> map = new HashMap<>();

       Map<String,Object> pageInfo=new HashMap<>();

        int countAll = mapper.countAll("%" + keyword + "%", category); // 총게시물
        int lastPageNumber = (countAll -1)/10+1; // 마지막 페이지 번호
        int startPageNumber= (page-1) / 10 * 10 +1;
        int endPageNumber= startPageNumber + 9;

        int prevPageNumber = startPageNumber-10;

        int nextPageNumber = endPageNumber+1;

        endPageNumber = Math.min(endPageNumber,lastPageNumber);


        pageInfo.put("currentPageNumber",page);
        pageInfo.put("startPageNumber",startPageNumber);
        pageInfo.put("endPageNumber",endPageNumber);
        pageInfo.put("lastPageNumber",lastPageNumber);
        if(prevPageNumber >0){
            pageInfo.put("prevPageNumber",prevPageNumber);
        }

        if(nextPageNumber<= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }
        int from = (page-1) *10;


        map.put("boardList" ,mapper.selectAll(from, "%" + keyword + "%", category));
        map.put("pageInfo" ,pageInfo);

         return map;
    }

    public Board get(Integer id) {

      Board board = mapper.selectById(id);

     List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);

     for(BoardFile boardFile : boardFiles){
         String url = urlPrefix + "prj1/"+id + "/" + boardFile.getFileName();
         boardFile.setUrl(url);
     }

     board.setFiles(boardFiles);

      return board;
    }

    public boolean remove(Integer id) {

        // 1. 게시물에  달린 댓글 삭제
        commentMapper.deleteByCommentId(id);

        // 2. 게시물에 달린 좋아요 삭제
        likeMapper.deleteByLikeBoardId(id);

        //3. aws3 버켓 지우기
        deleteFile(id);

        return mapper.deleteById(id) ==1;
    }

    private void deleteFile(Integer id) {
        //파일명 조회
        List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);

        //  aws3 버켓 지우기
        for(BoardFile file : boardFiles){
            String key = "prj1/" + id +"/" +file.getFileName();

            DeleteObjectRequest ObjectRequest =
                    DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build();

            s3.deleteObject(ObjectRequest);
        }

        // 4. 게시물에 달린 이미지 삭제
        fileMapper.deleteByFileBoardId(id);

    }

    public boolean update(Board board, MultipartFile[] uploadFiles, List<Integer> removeFileIds) throws IOException {
        // 파일 지우기

        if(removeFileIds != null){
            for(Integer id : removeFileIds){

                BoardFile file= fileMapper.selectById(id);
                String key = "prj1/" + board.getId() +"/" +file.getFileName();
                DeleteObjectRequest deleteObject = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();

                s3.deleteObject(deleteObject);
                //db에서 지우기
                fileMapper.deleteById(id);
            }

        }


        //파일 추가하기
        if (uploadFiles != null) {
                for (MultipartFile files : uploadFiles) {
                    //s3버킷에 업로드
                    upload(board.getId(),files);
                    //db에추가하기
                    fileMapper.insert(board.getId(),files.getOriginalFilename());
                }
        }

        return  mapper.update(board)==1;
    }

    public boolean hasAccess(Integer id, Member login) {
        if(login == null){
            return false;
        }

        if(login.isAdmin()){
            return true;
        }

        Board board = mapper.selectById(id);

        return board.getWriter().equals(login.getId());
    }



}
