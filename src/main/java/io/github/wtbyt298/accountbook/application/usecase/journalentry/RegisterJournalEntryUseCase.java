package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.journalentry.*;
import io.github.wtbyt298.accountbook.domain.service.JournalEntryFactory;

/**
 * 仕訳登録処理クラス
 */
@Service
public class RegisterJournalEntryUseCase {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private JournalEntryFactory journalEntryFactory;
	
	/**
	 * 仕訳を登録する
	 * @param registerParam 仕訳登録用のDTO
	 */
	@Transactional
	public void register(RegisterJournalEntryCommand command) {
		JournalEntry entry = journalEntryFactory.create(command);
		journalEntryRepository.save(entry);
	}
	
}
