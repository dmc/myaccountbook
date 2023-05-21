package io.github.wtbyt298.accountbook.presentation.controller.shared;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 共通の例外処理用のコントローラクラス
 * 各層で投げられた例外を処理する
 */
@ControllerAdvice
public class ExceptionHandlerUtilityController {

	@ExceptionHandler(Exception.class)
	public String handle(Exception exception, Model model) {
		model.addAttribute("errorMessage", exception.getMessage());
		return "error";
	}
	
}
