package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホーム画面のコントローラクラス
 */
@Controller
public class HomeController {

	/**
	 * ホーム画面を表示する
	 */
	@GetMapping("/user/home")
	public String load() {
		return "/user/home";
	}
	
}
