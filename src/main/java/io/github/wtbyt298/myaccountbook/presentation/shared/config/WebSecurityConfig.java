package io.github.wtbyt298.myaccountbook.presentation.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurityによる認証の設定を行うクラス
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		//ログイン時の設定
		http.formLogin(login -> login
				.loginProcessingUrl("/login")
				.loginPage("/user/login")
				.defaultSuccessUrl("/user/home")
				.permitAll()
			//ログアウト時の設定
			).logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
			//アクセス権の設定
			).authorizeHttpRequests(auth -> auth
				.requestMatchers("/").permitAll()
				.requestMatchers(HttpMethod.GET, "/user/signup").permitAll()
				.requestMatchers(HttpMethod.POST, "/signup").permitAll()
				.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
				.anyRequest().authenticated()
			//CSRF対策
			).csrf(
		);
		
		return http.build();
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}
