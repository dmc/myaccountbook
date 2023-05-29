package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryUseCase;
import io.github.wtbyt298.accountbook.domain.shared.exception.CannotCreateJournalEntryException;
import io.github.wtbyt298.accountbook.presentation.params.journalentry.RegisterEntryDetailParam;
import io.github.wtbyt298.accountbook.presentation.params.journalentry.RegisterJournalEntryParam;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * 仕訳登録処理のコントローラクラス
 */
@Controller
public class RegisterJournalEntryController {
	
	@Autowired
	private RegisterJournalEntryUseCase registerJournalEntryUseCase;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
		
	/**
	 * 仕訳登録画面を表示する
	 */
	@GetMapping("/entry/register")
	public String load(@ModelAttribute("entryParam") RegisterJournalEntryParam param, Model model) {
		//画面読み込み時に少なくとも1行の仕訳明細フォームを表示する
		param.initList();
		return "/entry/register";
	}
	
	/**
	 * 仕訳を登録する
	 */
	@PostMapping("/entry/register")
	public String register(@Valid @ModelAttribute("entryParam") RegisterJournalEntryParam param, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "/entry/register";
		}
		try {
			RegisterJournalEntryCommand command = mapParameterToCommand(param);
			UserSession userSession = userSessionProvider.getUserSession();
			registerJournalEntryUseCase.execute(command, userSession);
			return "redirect:/entry/register";
		} catch (CannotCreateJournalEntryException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			return "/entry/register";
		}
	}
	
	/**
	 * パラメータをコマンドオブジェクトに詰め替える（仕訳）
	 */
	private RegisterJournalEntryCommand mapParameterToCommand(RegisterJournalEntryParam param) {
		//仕訳明細データを詰め替える
		List<RegisterEntryDetailCommand> detailCommands = param.getEntryDetailParams().stream()
			.map(each -> mapParameterToCommand(each))
			.toList();
		return new RegisterJournalEntryCommand(
			param.getDealDate(),   
			param.getDescription(),
			detailCommands
		);
	}
	
	/**
	 * パラメータをコマンドオブジェクトに詰め替える（仕訳明細）
	 */
	private RegisterEntryDetailCommand mapParameterToCommand(RegisterEntryDetailParam param) {
		return new RegisterEntryDetailCommand(
			param.getAccountTitleId(),    
			param.getSubAccountTitleId(), 
			param.getDetailLoanType(),    
			param.getAmount()
		);
	}
	
	/**
	 * 仕訳明細の入力フォームを追加する
	 * 上限は10
	 */
	@PostMapping(value = "/entry/register", params = "add")
	public String addForm(@ModelAttribute("entryParam") RegisterJournalEntryParam param, @RequestParam("add") String value, Model model) {
		final String ERROR_MESSAGE = "これ以上追加できません。";
		if (param.isFull(value)) {
			model.addAttribute("error_" + value, ERROR_MESSAGE);
		}
		param.addList(value);
		return "/entry/register";
	}
	
	/**
	 * 仕訳明細の入力フォームを削除する
	 * 下限は1
	 */
	@PostMapping(value = "/entry/register", params = "remove")
	public String removeForm(@ModelAttribute("entryParam") RegisterJournalEntryParam param, @RequestParam("remove") String value, HttpServletRequest request) {
		final String type = value.substring(0, value.indexOf("-"));
		final int index = Integer.valueOf(request.getParameter("remove").substring(value.indexOf("-") + 1));
		param.removeList(type, index);
		return "/entry/register";
	}
	
}
