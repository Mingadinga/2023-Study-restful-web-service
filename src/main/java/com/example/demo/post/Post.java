package com.example.demo.post;

import com.example.demo.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id @GeneratedValue
    private Integer id;


    private String description;

    // User : Post -> 1 : * 즉, parent child 관계
    @JsonIgnore // 외부에 공개하지 않음
    @ManyToOne(fetch = FetchType.LAZY) // 포스트 여러개가 하나의 유저에 속함 // Post 조회시 User 조인 안함
    private User user;

}
