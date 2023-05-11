package io.github.wtbyt298.accountbook.helper.testfactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.github.wtbyt298.accountbook.domain.model.journalentry.DealDate;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Description;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetails;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * テスト用の仕訳明細インスタンスを生成するクラス
 */
public class JournalEntryTestFactory {

	public static JournalEntry create() {
		return JournalEntry.reconstruct(
			EntryId.newInstance(),
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳です。"), 
			createEntryDetails()
		);
	}
	
	private static EntryDetails createEntryDetails() {
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 1000));
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 2000));
		elements.add(EntryDetailTestFactory.create(LoanType.CREDIT, 3000));
		return new EntryDetails(elements);
	}
	
}
