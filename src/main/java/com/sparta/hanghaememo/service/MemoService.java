package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.MemoRequestDto;
import com.sparta.hanghaememo.entity.Memo;
import com.sparta.hanghaememo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
//@Transactional(readOnly = true)이부분 오류일때 import javax.transaction.Transactional;는 사용하지 않는다.
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {
    //데이터베이스와 연결을 해야하는데 연결하는 부분이 MemoRepository를 사용할 수 있도록 추가해준다.
    private final MemoRepository memoRepository;

    //이렇게 하고 다시 MemoController로 넘어간다.
    public Memo createMemo(MemoRequestDto requestDto) {
        //데이터베이스에 연결을 해서 저장하려면 @Entity 에노테이션이 걸려져 있는
        //Memo클래스를 인스턴스로 만들어서 그 값을 사용하여 저장을 해야 한다.
        Memo memo = new Memo(requestDto);
        // Memo로 이동한다.
        //Memo에서 돌아와서 memoRepository에 연결하여 save함수안에 memo를 넣어주면
        //자동으로 쿼리가 성성이 되어서 데이터베이스에 연결된다.
        memoRepository.save(memo);
        return memo;
    }

    @Transactional(readOnly = true)
    public List<Memo> getMemos() {
        return memoRepository.findAllByOrderByModifiedAtDesc();
        //findAll()가 아닌 다른 방식으로 memo를 불러와야한다.
    }

    @Transactional
    public Long update(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("아이디가 존재하지 않습니다. ")
        );
        memo.update(requestDto);
        return memo.getId();
    }

    @Transactional
    public Long deleteMemo(Long id) {
        memoRepository.deleteById(id);
        return id;
    }
}
