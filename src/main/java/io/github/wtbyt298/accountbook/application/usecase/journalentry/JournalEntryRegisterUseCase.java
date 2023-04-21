package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.journalentry.*;
import io.github.wtbyt298.accountbook.domain.service.JournalEntryFactory;
import io.github.wtbyt298.accountbook.presentation.httprequest.journalentry.JournalEntryRegisterParam;

/**
 * 仕訳登録処理クラス
 */
@Service
public class JournalEntryRegisterUseCase {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private JournalEntryFactory journalEntryFactory;
	
	/**
	 * 仕訳を登録する
	 * @param registerParam 仕訳登録用のDTO
	 */
	@Transactional
	public void register(JournalEntryRegisterParam registerParam) {
		JournalEntry entry = journalEntryFactory.create(registerParam);
		journalEntryRepository.save(entry);
	}
	
}
