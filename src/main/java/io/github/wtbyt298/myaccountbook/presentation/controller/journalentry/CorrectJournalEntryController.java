package io.github.wtbyt298.myaccountbook.presentation.controller.journalentry;

import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.application.usecase.journalentry.CorrectJournalEntryUseCase;
import io.github.wtbyt298.myaccountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.myaccountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.myaccountbook.domain.shared.exception.CannotCreateJournalEntryException;
import io.github.wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterEntryDetailForm;
import io.github.wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterJournalEntryForm;
import io.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
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
	public String correct(@ModelAttribute("entryId") String id, @Valid @ModelAttribute("entryForm") RegisterJournalEntryForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "/entry/edit";
		}
		
		EntryId entryId = EntryId.fromString(id);
		RegisterJournalEntryCommand command = mapParameterToCommand(form);
		UserSession userSession = userSessionProvider.getUserSession();
		
		try {
			correctJournalEntryUseCase.execute(entryId, command, userSession);
			return redirectPath();
		} catch (CannotCreateJournalEntryException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			return "/entry/edit";
		}
	}
	
	private String redirectPath() {
		String currentYearMonth = YearMonth.now().toString();
		return "redirect:/entry/entries/" + currentYearMonth;
	}
	
	/**
	 * フォームクラスをコマンドオブジェクトに詰め替える（仕訳）
	 */
	private RegisterJournalEntryCommand mapParameterToCommand(RegisterJournalEntryForm form) {
		//仕訳明細データを詰め替える
		List<RegisterEntryDetailCommand> detailCommands = form.getEntryDetailParams().stream()
			.map(each -> mapParameterToCommand(each))
			.toList();
		
		return new RegisterJournalEntryCommand(
			form.getDealDate(),   
			form.getDescription(),
			detailCommands
		);
	}
	
	/**
	 * フォームクラスをコマンドオブジェクトに詰め替える（仕訳明細）
	 */
	private RegisterEntryDetailCommand mapParameterToCommand(RegisterEntryDetailForm form) {
		return new RegisterEntryDetailCommand(
			form.getAccountTitleId(),    
			form.getSubAccountTitleId(), 
			form.getDetailLoanType(),    
			form.getAmount()
		);
	}
	
}
