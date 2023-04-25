package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.CancelJournalEntryUseCase;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;

/**
 * 仕訳取消処理のコントローラ
 */
@Controller
public class CancelJournalEntryController {

	@Autowired
	private CancelJournalEntryUseCase cancelUseCase;
	
	/**
	 * 登録済みの仕訳を取り消す
	 */
	@PostMapping("/cancel/{id}")
	public String cancel(@PathVariable String id) {
		EntryId entryId = EntryId.fromString(id);
		cancelUseCase.execute(entryId);
		return "/";
	}
	
}
