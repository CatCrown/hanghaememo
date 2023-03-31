package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.MemoRequestDto;
import com.sparta.hanghaememo.entity.Memo;
import com.sparta.hanghaememo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;
@RestController
@RequiredArgsConstructor
public class MemoController {
    //MemoService를 만들었기 때문에 MemoService도 여기서 연결하여야 하기때문에
    // 이부분을 불러온다.
    private final MemoService memoService;

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @GetMapping("/api/memos")
    public List<Memo> getMemos() {
        return memoService.getMemos();
    }

    @PostMapping("/api/memos")
    public Memo createMemo(@RequestBody MemoRequestDto requestDto){
        // 실제로 데이터 로직을 작성하고 데이터베이스와 연결하는 부분이 Service인데
        // 그 부분을 개발해애 한다. 패키지 Service에서 MemoService를 개발한다.

        // memoService 연결했기때문에 createMemo라는 부분에 메서드를 만들어주고
        // parameter값으로 클라이언트에서 가지고 온 requestDto 안에 들어있는 값을 사용해야 해서
        // parameter에 넣어주고 다음에 createdMemo함수를 만들어 주도록 합니다.-> Memoservice로 이동한다.
        return memoService.createMemo(requestDto);
    }
    @PutMapping("/api/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto){
        return memoService.update(id,requestDto);
    }

    @DeleteMapping("/api/memos/{id}")
    public Long deleteMemo(@PathVariable Long id){
        return memoService.deleteMemo(id);
    }

}
