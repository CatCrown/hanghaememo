package com.sparta.hanghaememo.security;


import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    //UserDetailsServiceImpl 클래스는 UserDetailsService 인터페이스를 구현한 클래스입니다.
    //회원가입 및 로그인을 위한 사용자 정보를 관리하고 인증을 수행하는 데 필요한 메서드입니다.

    //이 메서드는 Spring Security에서 인증을 수행할 때, 사용자 정보를 가져오는 메서드입니다.

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        //UserRepository를 사용하여 DB에서 사용자 정보를 조회하고,
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

//        구현 방법 2:
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        if (!userOptional.isPresent()) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        User user = userOptional.get();

        return new UserDetailsImpl(user, user.getUsername());
        // 조회한 정보를 UserDetailsImpl 객체에 담아 반환합니다.
    }

}