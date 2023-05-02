package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testfactory.AccountTitleTestFactory;
import io.github.wtbyt298.accountbook.helper.testfactory.SubAccountTitleTestFactory;

class EntryDetailTest {

	@Test
	void 貸借区分が借方なら借方明細と判定される() {
		//when:
		EntryDetail detail = new EntryDetail(
			AccountTitleTestFactory.create("102", "普通預金", AccountingType.ASSETS), 
			SubAccountTitleTestFactory.create("0", "その他"), 
			LoanType.DEBIT, 
			Amount.valueOf(100)
		);
		
		//then:
		assertTrue(detail.isDebit());
		assertFalse(detail.isCredit());
	}

	@Test
	void 貸借区分が貸方なら貸方明細と判定される() {
		//when:
		EntryDetail detail = new EntryDetail(
				AccountTitleTestFactory.create("102", "普通預金", AccountingType.ASSETS), 
				SubAccountTitleTestFactory.create("0", "その他"), 
				LoanType.CREDIT, 
				Amount.valueOf(100)
			);
		
		//then:
		assertTrue(detail.isCredit());
		assertFalse(detail.isDebit());
	}

}
