package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.CancelJournalEntryUseCase;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

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
	@PostMapping("/cancel/{id}")
	public String cancel(@PathVariable String id) {
		EntryId entryId = EntryId.fromString(id);
		UserSession userSession = userSessionProvider.getUserSession();
		cancelJournalEntryUseCase.execute(entryId, userSession);
		return "/home";
	}
	
}
