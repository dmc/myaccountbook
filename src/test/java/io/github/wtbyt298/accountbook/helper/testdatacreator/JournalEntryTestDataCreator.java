package io.github.wtbyt298.accountbook.helper.testdatacreator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.wtbyt298.accountbook.domain.model.journalentry.DealDate;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Description;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetails;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.user.User;

/**
 * DBにテスト用の仕訳データを作成するクラス
 */
@Component
public class JournalEntryTestDataCreator {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	public JournalEntry create(User user) {
		JournalEntry journalEntry = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳データです。"), 
			testEntryDetails()
		);
		journalEntryRepository.save(journalEntry, user.id());
		return journalEntry;
	}
	
	private EntryDetails testEntryDetails() {
		List<EntryDetail> list = new ArrayList<>();
		//TODO 後で実装する
		return new EntryDetails(list);
	}
	
}
