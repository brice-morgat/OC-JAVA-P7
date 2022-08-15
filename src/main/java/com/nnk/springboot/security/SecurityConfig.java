package com.nnk.springboot.security;

import com.nnk.springboot.services.MyUserDetailsService;
import com.nnk.springboot.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    UserServiceImpl userService;
//
//    /**
//     * The configuration of Spring Security
//     * And Authentification Params
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeRequests()
//                .antMatchers("/forgetPassword").permitAll()
//                .antMatchers("/user/**").permitAll()
//                .antMatchers("/registerUser").permitAll()
//                .antMatchers("/login").permitAll()
//                .anyRequest().authenticated()
//                .and().exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and().formLogin().loginPage("/login").loginProcessingUrl("/login")
//                .defaultSuccessUrl("/home.html").failureUrl("/login?error")
//                .and().logout().logoutUrl("/logout");
////        http.csrf().disable().authorizeRequests().antMatchers("/user/**").anonymous().and().authorizeRequests().antMatchers(
////                        "/registration**",
////                        "/js/**",
////                        "/css/**",
////                        "/img/**").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .formLogin().defaultSuccessUrl("/home")
////                .permitAll()
////                .and()
////                .logout().and()
////                .userDetailsService(userService);
////                    .tokenValiditySeconds(30);
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    UserServiceImpl userService;
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
//    }
//
//    /**
//     * The configuration of Spring Security
//     * And Authentification Params
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests().antMatchers("/user/**", "/home").anonymous().and()
//                .authorizeRequests().antMatchers(
//                        "/registration**",
//                        "/js/**",
//                        "/css/**",
//                        "/img/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .permitAll()
//                .and()
//                .logout()
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login?logout")
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//                .and()
//                .rememberMe()
//                .userDetailsService(userService);
////                    .tokenValiditySeconds(30);
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//}



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/","/login/**","/css/**", "/img/**").permitAll()
                .antMatchers("/user/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/",true).permitAll()
                .and()
                .oauth2Login().defaultSuccessUrl("/",true).permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true).clearAuthentication(true)
                .logoutUrl("/login")
                .logoutRequestMatcher(new AntPathRequestMatcher("/app-logout"));
    }

}