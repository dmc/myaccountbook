package com.github.wtbyt298.myaccountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.Amount;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;

class EntryDetailTest {

	@Test
	void 貸借区分が借方なら借方明細と判定される() {
		//when:
		EntryDetail detail = new EntryDetail(
			AccountTitleId.valueOf("101"), 
			SubAccountTitleId.valueOf("0"), 
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
			AccountTitleId.valueOf("101"), 
			SubAccountTitleId.valueOf("0"), 
			LoanType.CREDIT, 
			Amount.valueOf(100)
		);
		
		//then:
		assertTrue(detail.isCredit());
		assertFalse(detail.isDebit());
	}
	
	@Test
	void 貸借区分を反転できる() {
		//given:借方、貸方それぞれの仕訳明細
		EntryDetail debit = new EntryDetail(
			AccountTitleId.valueOf("101"), 
			SubAccountTitleId.valueOf("0"), 
			LoanType.DEBIT, 
			Amount.valueOf(100)
		);
		
		EntryDetail credit = new EntryDetail(
			AccountTitleId.valueOf("101"), 
			SubAccountTitleId.valueOf("0"), 
			LoanType.CREDIT, 
			Amount.valueOf(100)
		);
		
		//when:貸借区分を反転させる
		EntryDetail debitToCredit = debit.transposeLoanType();
		EntryDetail creditToDebit = credit.transposeLoanType();
		
		//then:貸借区分が変更されている
		assertEquals(LoanType.CREDIT, debitToCredit.detailLoanType());
		assertEquals(LoanType.DEBIT, creditToDebit.detailLoanType());
	}

}
