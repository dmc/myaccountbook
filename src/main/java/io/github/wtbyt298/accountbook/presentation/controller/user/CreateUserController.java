package io.github.wtbyt298.accountbook.presentation.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserCommand;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserUseCase;
import io.github.wtbyt298.accountbook.presentation.request.user.CreateUserParam;
import jakarta.validation.Valid;

/**
 * ユーザ作成処理のコントローラクラス
 */
@Controller
public class CreateUserController {

	@Autowired
	private CreateUserUseCase createUseCase;
	
	@GetMapping("/signup")
	public String signUp() {
		return "/signup";
	}
	
	/**
	 * フォームに入力した内容でユーザを登録する
	 */
	@PostMapping("/signup")
	public String register(@Valid @ModelAttribute("userParam") CreateUserParam param, BindingResult result) {
		if (result.hasErrors()) {
			return "/signup";
		}
		CreateUserCommand command = new CreateUserCommand(param.getId(), param.getPassword(), param.getMailAddress());
		createUseCase.execute(command);
		return "登録完了画面";
	}
	
}
