package com.example.demo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

// 자료구조 클래스
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Date joinDate;
}
