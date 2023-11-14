package com.example.prj1be20231109.mapper;

import com.example.prj1be20231109.domain.Auth;
import com.example.prj1be20231109.domain.Board;
import com.example.prj1be20231109.domain.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    @Insert("""
                INSERT INTO member(id,password,name,email,nickName,gender, birth, phone)
                VALUES (#{id},#{password},#{name},#{email},#{nickName},#{gender},#{birth},#{phone})
            """)
    int add(Member member);

    @Select("""
                SELECT id FROM member
                WHERE id= #{id}
            """)
    String selectId(String id);

    @Select("""
                SELECT email FROM member
                WHERE email= #{email}
            """)
    String selectEmail(String email);



    @Select("""
                SELECT *
                FROM member
                ORDER BY id DESC 
            """)
    List<Member> selectAll();


    @Select("""
        SELECT *
        FROM member
        WHERE id = #{id}
        """)
    Member selectById(String id);

    @Delete("""
        DELETE FROM member
        WHERE id = #{id}
        """)
    int deleteById(String id);

    @Update("""
        <script>
        UPDATE member
        SET 
          <if test="password != ''">
          password = #{password},
          </if>
          email = #{email},
          nickName = #{nickName}
        WHERE id = #{id}
        </script>
        """)
    int update(Member member);

    @Select("""
        SELECT nickName
        FROM member
        WHERE nickName = #{nickName}
        """)
    String selectNickName(String nickName);

    @Select("""
            SELECT *
            FROM auth
            WHERE memberId= #{id}
            """)
    List<Auth> selectAuthById(String id);
}
