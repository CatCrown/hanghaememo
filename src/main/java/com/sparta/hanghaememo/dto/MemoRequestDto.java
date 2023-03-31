package com.sparta.hanghaememo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {
    //유저이름이랑 컨텐스를 받아와야 한다.
    // client에서 넘어오는 username과 contents를 MemoRequestDto 이객체로 받아오겠다.
    private String username;
    private String contents;
}
