package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserCommand;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserUseCase;
import io.github.wtbyt298.accountbook.presentation.request.user.CreateUserParam;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * ユーザ作成処理のコントローラクラス
 */
@Controller
public class SignUpController {

	@Autowired
	private CreateUserUseCase createUserUseCase;

	@GetMapping("/user/signup")
	public String load(@ModelAttribute("userParam") CreateUserParam param) {
		return "/user/signup";
	}
	
	/**
	 * ユーザを作成する
	 */
	@PostMapping("/signup")
	public String signUp(@Valid @ModelAttribute("userParam") CreateUserParam param, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "/user/signup";
		}
		try {
			CreateUserCommand command = new CreateUserCommand(
				param.getId(), 
				param.getPassword(), 
				param.getMailAddress()
			);
			createUserUseCase.execute(command);
			autoLogin(param.getId(), param.getPassword(), request);
			return "redirect:/user/home";
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "/user/signup";
		}
	}
	
	/**
	 * 自動でログイン処理を行う
	 */
	private void autoLogin(String userId, String password, HttpServletRequest request) {
		//既にユーザがログイン済みの場合は、一旦ログアウトさせる
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.clearContext();
		}
		try {
			request.login(userId, password);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
}
