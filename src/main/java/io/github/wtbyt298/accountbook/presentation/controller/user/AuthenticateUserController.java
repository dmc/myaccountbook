package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ユーザ認証処理のコントローラクラス 
 */
@Controller
public class AuthenticateUserController {
	
	/**
	 * ログイン画面を表示する
	 */
	@GetMapping("/user/login")
	public String load() {
		return "/user/login";
	}
	
}
