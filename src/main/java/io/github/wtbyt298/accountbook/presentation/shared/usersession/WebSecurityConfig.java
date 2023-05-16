package io.github.wtbyt298.accountbook.presentation.shared.usersession;

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
		http.formLogin(login -> login
				.loginProcessingUrl("/login")
				.loginPage("/user/login")
				.defaultSuccessUrl("/home")
				.permitAll()
			).logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
			).authorizeHttpRequests(auth -> auth
				.requestMatchers("/").permitAll()
				.requestMatchers(HttpMethod.GET, "/user/signup").permitAll()
				.requestMatchers(HttpMethod.POST, "/signup").permitAll()
				.requestMatchers("/css/**", "/js/**", "/lib/**").permitAll()
				.anyRequest().authenticated()
			).csrf(
		);
		return http.build();
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}
