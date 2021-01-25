package com.example.demo.config;

import com.example.demo.security.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin()
                .loginPage("/login/page")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/success/login")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/create/project").hasAnyAuthority("TEAM_LEADER")
                .antMatchers("/search/name").hasAnyAuthority("TEAM_LEADER")
                .antMatchers("/filter/project").hasAnyAuthority("TEAM_LEADER")
                .antMatchers("/create").hasAnyAuthority("TEAM_LEADER")
                .antMatchers("/project").hasAnyAuthority("TEAM_LEADER")
                .antMatchers("/leader/page").hasAnyAuthority("TEAM_LEADER")
                .antMatchers("/log/page").hasAnyAuthority("TEAM_MEMBER")
                .antMatchers("/create/page").hasAnyAuthority("TEAM_MEMBER")
                .antMatchers("/create/log").hasAnyAuthority("TEAM_MEMBER")
                .antMatchers("/member/page").hasAnyAuthority("TEAM_MEMBER");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
