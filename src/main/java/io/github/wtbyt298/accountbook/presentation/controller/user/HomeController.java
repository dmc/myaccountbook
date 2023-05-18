package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * ホーム画面のコントローラクラス
 */
@Controller
public class HomeController {

	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * ホーム画面を表示する
	 */
	@GetMapping("/user/home")
	public String load() {
		return "/user/home";
	}
	
	/**
	 * ログイン中のユーザ名を返す
	 */
	@ModelAttribute("loginUser")
	public String loginUser() {
		UserSession userSession = userSessionProvider.getUserSession();
		return userSession.userId().toString();
	}
	
}
