package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	 * 
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	/**
	 * 
	 */
	@GetMapping("/home")
	public String home(Model model) {
		UserSession session = userSessionProvider.getUserSession();
		String currentUser = session.userId();
		model.addAttribute("name", currentUser);
		return "home";
	}
	
}
