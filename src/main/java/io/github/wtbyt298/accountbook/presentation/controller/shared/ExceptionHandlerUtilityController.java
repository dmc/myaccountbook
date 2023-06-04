package io.github.wtbyt298.accountbook.presentation.controller.shared;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wtbyt298.accountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.accountbook.domain.shared.exception.DomainException;
import io.github.wtbyt298.accountbook.infrastructure.shared.exception.RecordNotFoundException;

/**
 * 共通の例外処理用のコントローラクラス
 */
@ControllerAdvice
public class ExceptionHandlerUtilityController {

	@ExceptionHandler({DomainException.class, UseCaseException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleDomainAndUseCaseException(RuntimeException exception, Model model) {
		//ドメインクラスおよびユースケースクラスで発生した例外は、BAD_REQUESTとして処理する
		HttpStatus status = HttpStatus.BAD_REQUEST;
		model.addAttribute("status", status.value());
		model.addAttribute("error", status.name());
		model.addAttribute("timestamp", new Date());
		model.addAttribute("message", exception.getMessage());
		
		return "error";
	}
	
	@ExceptionHandler(RecordNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleRecordNotFoundException(RecordNotFoundException exception, Model model) {
		//DBアクセスの結果レコードが見つからない場合の例外は、NOT_FOUNDとして処理する
		HttpStatus status = HttpStatus.NOT_FOUND;
		model.addAttribute("status", status.value());
		model.addAttribute("error", status.name());
		model.addAttribute("timestamp", new Date());
		model.addAttribute("message", exception.getMessage());
		
		return "error";
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception exception, Model model) {
		//その他の例外は、INTERNAL_SERVER_ERRORとして処理する
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		model.addAttribute("status", status.value());
		model.addAttribute("error", status.name());
		model.addAttribute("timestamp", new Date());
		model.addAttribute("message", "予期せぬエラーが発生しました。");
		
		exception.printStackTrace();
		
		return "error";
	}
	
}
