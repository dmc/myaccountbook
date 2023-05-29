package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.CorrectJournalEntryUseCase;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.shared.exception.CannotCreateJournalEntryException;
import io.github.wtbyt298.accountbook.presentation.params.journalentry.RegisterEntryDetailParam;
import io.github.wtbyt298.accountbook.presentation.params.journalentry.RegisterJournalEntryParam;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * 仕訳の訂正処理のコントローラクラス
 */
@Controller
public class CorrectJournalEntryController {
	
	@Autowired
	private CorrectJournalEntryUseCase correctJournalEntryUseCase;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 仕訳を訂正する
	 */
	@PostMapping("/entry/correct")
	public String correct(@ModelAttribute("entryId") String id, @Valid @ModelAttribute("entryParam") RegisterJournalEntryParam param, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "/entry/entry";
		}
		EntryId entryId = EntryId.fromString(id);
		RegisterJournalEntryCommand command = mapParameterToCommand(param);
		UserSession userSession = userSessionProvider.getUserSession();
		try {
			correctJournalEntryUseCase.execute(entryId, command, userSession);
			return redirectPath();
		} catch (CannotCreateJournalEntryException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			return "/entry/entry";
		}
	}
	
	private String redirectPath() {
		String currentYearMonth = YearMonth.now().toString();
		return "redirect:/entry/entries/" + currentYearMonth;
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
	@PostMapping(value = "/entry/correct", params = "add")
	public String addForm(@ModelAttribute("entryId") String id, @ModelAttribute("entryParam") RegisterJournalEntryParam param, @RequestParam("add") String value, Model model) {
		final String ERROR_MESSAGE = "これ以上追加できません。";
		if (param.isFull(value)) {
			model.addAttribute("error_" + value, ERROR_MESSAGE);
		}
		param.addList(value);
		return "/entry/entry";
	}
	
	/**
	 * 仕訳明細の入力フォームを削除する
	 * 下限は1
	 */
	@PostMapping(value = "/entry/correct", params = "remove")
	public String removeForm(@ModelAttribute("entryId") String id, @ModelAttribute("entryParam") RegisterJournalEntryParam param, @RequestParam("remove") String value, HttpServletRequest request) {
		final String type = value.substring(0, value.indexOf("-"));
		final int index = Integer.valueOf(request.getParameter("remove").substring(value.indexOf("-") + 1));
		param.removeList(type, index);
		return "/entry/entry";
	}
	
}
