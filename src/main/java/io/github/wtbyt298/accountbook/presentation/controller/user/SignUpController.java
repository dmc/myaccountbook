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

import io.github.wtbyt298.accountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserCommand;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserUseCase;
import io.github.wtbyt298.accountbook.presentation.params.user.CreateUserParam;
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
			CreateUserCommand command = mapParameterToCommand(param);
			createUserUseCase.execute(command);
			//ユーザ作成に成功した場合、そのままログインする
			autoLogin(param.getId(), param.getPassword(), request);
			return "redirect:/user/home";
		} catch (UseCaseException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			return "/user/signup";
		}
	}
	
	/**
	 * パラメータをコマンドオブジェクトに詰め替える
	 */
	private CreateUserCommand mapParameterToCommand(CreateUserParam param) {
		return new CreateUserCommand(param.getId(), param.getPassword(), param.getMailAddress());
	}
	
	/**
	 * 自動でログイン処理を行う
	 * TODO 別クラスに切り出すことを検討する
	 */
	private void autoLogin(String userId, String password, HttpServletRequest request) {
		//既にユーザがログイン済みの場合は、一旦ログアウトさせる
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.clearContext();
		}
		try {
			request.login(userId, password);
		} catch (ServletException exception) {
			exception.printStackTrace();
		}
	}
	
}
