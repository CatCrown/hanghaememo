package com.sparta.hanghaememo.config;

import com.sparta.hanghaememo.jwt.JwtAuthFilter;
import com.sparta.hanghaememo.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//이 SecurityConfig 클래스는 JWT 인증 필터를 등록하여 사용자 인증을 처리합니다.
// 또한 모든 URL에 대한 인가를 설정하고, CSRF 방지, 세션 사용 안 함, H2 콘솔과
// 정적 리소스에 대한 요청 무시 등 보안 관련 구성을 수행
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함. Spring Security를 활성화한다.
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {
     //configure() 메서드에서는 HTTP 보안 구성을 설정하고 있습니다.

    private final JwtUtil jwtUtil; //JwtUtil을 @RequiredArgsConstructor로 주입됨

    @Bean  //@Bean으로 PasswordEncoder를 등록
    public PasswordEncoder passwordEncoder() {
        //passwordEncoder() 메서드를 호출하여 사용자 비밀번호를 암호화할 인코더를 설정
        return new BCryptPasswordEncoder();
        //이 클래스는 BCrypt 해시 알고리즘을 사용하여 암호를 안전하게 저장한다.
    }

    //@Bean으로 WebSecurityCustomizer를 등록
    //이 메서드는 H2 콘솔과 정적 리소스에 대한 요청을 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // @Bean으로 SecurityFilterChain을 등록
    // 이 메서드는 HttpSecurity를 사용하여 HTTP 보안을 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //  csrf()를 호출하여 CSRF(Cross-site Request Forgery) 공격을 방지

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //sessionManagement()을 호출하여 세션 관리 정책을 STATELESS로 설정하여 세션을 사용하지 않도록 함.

        http.authorizeRequests()
                // authorizeRequests()를 호출하여 URL에 대한 권한 설정을 수행
                // antMatchers()를 사용하여 경로 패턴에 따라 권한을 설정
                // permitAll()을 호출하여 모든 사용자가 해당 URL에 접근할 수 있도록 허용
                .antMatchers("/api/user/**").permitAll()
                .antMatchers("/api/search").permitAll()
                .antMatchers("/api/shop").permitAll()
                .anyRequest().authenticated()
                //anyRequest()를 호출하여 나머지 URL에 대한 권한 설정을 수행
                //authenticated()를 호출하여 인증된 사용자만 접근할 수 있도록 함.

                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
                // addFilterBefore()를 호출하여 JWT 인증 필터를 등록

        http.formLogin().loginPage("/api/user/login-page").permitAll();
        // formLogin()을 호출하여 사용자 로그인 페이지를 지정

        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
        // exceptionHandling()을 호출하여 접근이 거부되었을 때 처리할 페이지를 지정
        return http.build();
        // build()를 호출하여 SecurityFilterChain을 반환
    }



//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//    //customUserDetailsService: 사용자 인증 정보를 조회하는 CustomUserDetailsService 빈을 주입합니다.
//    // UserDetailsServiceImpl 인거 같아
//
//    @Autowired
//    private JwtAuthenticationEntryPoint unauthorizedHandler;
//    //unauthorizedHandler: 인증되지 않은 사용자가 접근하면 발생하는 예외를 처리하는 JwtAuthenticationEntryPoint 빈을 주입합니다.
//    // jwtFilter인 거 같아
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter();
//    }
//    //jwtAuthenticationFilter: JWT 토큰을 이용하여 인증을 처리하는 JwtAuthenticationFilter 빈을 생성합니다.
//
//    @Override
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
//    //configure(AuthenticationManagerBuilder authenticationManagerBuilder): 사용자 인증 정보를 조회할 때 사용하는 userDetailsService 빈을 설정합니다.
//    // 또한, 패스워드 인코딩 방식을 passwordEncoder() 메소드를 이용하여 설정합니다.
//
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    //패스워드 인코딩 방식을 설정합니다. 예시에서는 BCrypt 알고리즘을 이용하여 패스워드를 암호화합니다.
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //configure(HttpSecurity http): HTTP 보안을 설정합니다.
//
//        http
//                .cors().and().csrf().disable()//cors와 csrf를 비활성화하고,
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                // 인증되지 않은 사용자가 접근하면 발생하는 예외를 처리하는 unauthorizedHandler 빈을 설정합니다
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers("/api/auth/**").permitAll()
//                .antMatchers("/api/broadcast/**").hasRole("BROADCASTER")
//                .anyRequest().authenticated();
//
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
//
//    // JWT 토큰 설정
//    @Bean
//    public JwtTokenProvider jwtTokenProvider() {
//        return new JwtTokenProvider();
//    }

}