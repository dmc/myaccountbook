package wtbyt298.myaccountbook.presentation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;

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
	
	/**
	 * ゲストアカウントでログインする
	 */
	@PostMapping("/triallogin")
	public String trialLogin(HttpServletRequest request) {
		final String userId = "Guest_User";
		final String password = "Guest0123";
		
		try {
			request.login(userId, password);
		} catch (ServletException exception) {
			exception.printStackTrace();
		}
		
		return "redirect:/user/home";
	}
	
}
