package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * ユーザ認証処理のコントローラクラス 
 */
@Controller
public class AuthenticateUserController {
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * ログイン画面を表示する
	 */
	@GetMapping("/user/login")
	public String load() {
		//既にユーザがログイン済みの場合は、ホーム画面を表示する
		UserSession userSession = userSessionProvider.getUserSession();
		if (userSession.isAuthenticated()) {
			return "redirect:/user/home";
		}
		
		return "/user/login";
	}
	
}
