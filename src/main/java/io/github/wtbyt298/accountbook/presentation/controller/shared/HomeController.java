package io.github.wtbyt298.accountbook.presentation.controller.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	@GetMapping("/home")
	public String load(Model model) {
		UserSession userSession = userSessionProvider.getUserSession();
		String loginUser = userSession.userId().toString();
		model.addAttribute("loginUser", loginUser);
		return "/home";
	}
	
}
