package io.github.wtbyt298.accountbook.presentation.controller.shared;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import io.github.wtbyt298.accountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.accountbook.domain.shared.exception.DomainException;

/**
 * 共通の例外処理用のコントローラクラス
 */
@ControllerAdvice
public class ExceptionHandlerUtilityController {

	@ExceptionHandler({DomainException.class, UseCaseException.class})
	public String handleDomainException(DomainException exception, Model model) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		model.addAttribute("status", status.value());
		model.addAttribute("error", status.name());
		model.addAttribute("timestamp", new Date());
		model.addAttribute("message", exception.getMessage());
		return "error";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception exception, Model model) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		model.addAttribute("status", status.value());
		model.addAttribute("error", status.name());
		model.addAttribute("timestamp", new Date());
		model.addAttribute("message", "予期せぬエラーが発生しました。");
		return "error";
	}
	
}
