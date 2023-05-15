package com.example.demo.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

// 자료구조 클래스
@Data
@AllArgsConstructor
public class User {
    private Integer id;

    @Size(min=2, message = "이름은 두 글자 이상으로 입력해 주세요.")
    private String name;

    @Past
    private Date joinDate;
}
