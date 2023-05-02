package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testfactory.EntryDetailTestFactory;

class JournalEntryTest {

	@Test
	void 取引日等を渡すと渡した値でインスタンスが生成される() {
		//given:
		DealDate dealDate = DealDate.valueOf(LocalDate.now());
		Description description = Description.valueOf("テスト用の仕訳です。");
		List<EntryDetail> list = new ArrayList<>();
		list.add(EntryDetailTestFactory.create(AccountingType.EXPENSES, LoanType.DEBIT, 100));
		list.add(EntryDetailTestFactory.create(AccountingType.ASSETS, LoanType.CREDIT, 100));
		EntryDetails entryDetails = new EntryDetails(list);
		
		//when:
		JournalEntry journalEntry = JournalEntry.create(dealDate, description, entryDetails);
		
		//then:
		assertEquals(dealDate, journalEntry.dealDate());
		assertEquals(description, journalEntry.description());
		assertEquals(2, journalEntry.entryDetails().size());
	}

}
