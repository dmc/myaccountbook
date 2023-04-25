package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;

/**
 * 仕訳取消処理クラス
 */
@Service
public class CancelJournalEntryUseCase {
	
	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	/**
	 * 仕訳を取消（削除）する
	 * @param id 取消対象の仕訳ID
	 */
	@Transactional
	public void cancel(EntryId id) {
		journalEntryRepository.drop(id);
	}
	
}