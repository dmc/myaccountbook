package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import io.github.wtbyt298.accountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserCommand;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserUseCase;
import io.github.wtbyt298.accountbook.presentation.forms.user.RegisterUserForm;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
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
	
	@Autowired UserSessionProvider userSessionProvider;

	@GetMapping("/user/signup")
	public String load(@ModelAttribute("userForm") RegisterUserForm form) {
		return "/user/signup";
	}
	
	/**
	 * ユーザを作成する
	 */
	@PostMapping("/signup")
	public String signUp(@Valid @ModelAttribute("userForm") RegisterUserForm form, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "/user/signup";
		}
		
		try {
			CreateUserCommand command = mapParameterToCommand(form);
			createUserUseCase.execute(command);
			
			//ユーザ作成に成功した場合、そのままログインする
			autoLogin(form.getId(), form.getPassword(), request);
			return "redirect:/user/home";
		} catch (UseCaseException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			return "/user/signup";
		}
	}
	
	/**
	 * フォームクラスをコマンドオブジェクトに詰め替える
	 */
	private CreateUserCommand mapParameterToCommand(RegisterUserForm form) {
		return new CreateUserCommand(form.getId(), form.getPassword(), form.getMailAddress());
	}
	
	/**
	 * 自動でログイン処理を行う
	 * TODO 別クラスに切り出すことを検討する
	 */
	private void autoLogin(String userId, String password, HttpServletRequest request) {
		UserSession userSession = userSessionProvider.getUserSession();
		//既にユーザがログイン済みの場合は、一旦ログアウトさせる
		if (! userSession.isEmpty()) {
			SecurityContextHolder.clearContext();
		}
		
		try {
			request.login(userId, password);
		} catch (ServletException exception) {
			exception.printStackTrace();
		}
	}
	
}
