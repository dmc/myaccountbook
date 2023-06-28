package com.github.wtbyt298.myaccountbook.presentation.controller.journalentry;

import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import com.github.wtbyt298.myaccountbook.application.usecase.journalentry.CancelJournalEntryUseCase;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import com.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * 仕訳取消処理のコントローラクラス
 */
@Controller
public class CancelJournalEntryController {

	@Autowired
	private CancelJournalEntryUseCase cancelJournalEntryUseCase;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 登録済みの仕訳を取り消す
	 */
	@PostMapping(value = "/entry/correct", params = "cancel")
	public String cancel(@RequestParam("entryId") String id) {
		EntryId entryId = EntryId.fromString(id);
		UserSession userSession = userSessionProvider.getUserSession();
		
		cancelJournalEntryUseCase.execute(entryId, userSession);
		
		return redirectPath();
	}
	
	private String redirectPath() {
		String currentYearMonth = YearMonth.now().toString();
		return "redirect:/entry/entries/" + currentYearMonth;
	}
	
}
