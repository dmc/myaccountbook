package io.github.wtbyt298.accountbook.domain.model.account;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

class AccountingTransactionTest {

	@Test
	void 貸借区分が借方であれば借方と判断される() {
		//given:
		//when:
		AccountingTransaction accountingTransaction = new AccountingTransaction(LoanType.DEBIT, Amount.valueOf(100));
		
		//then:
		assertTrue(accountingTransaction.isDebit());
		assertFalse(accountingTransaction.isCredit());
	}
	
	@Test
	void 貸借区分が貸方であれば貸方と判断される() {
		//given:
		//when:
		AccountingTransaction accountingTransaction = new AccountingTransaction(LoanType.CREDIT, Amount.valueOf(100));
		
		//then:
		assertTrue(accountingTransaction.isCredit());
		assertFalse(accountingTransaction.isDebit());
	}

}
