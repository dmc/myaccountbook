package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

class EntryDetailTest {
	
	//TODO テストコードを改良する
	private static EntryDetail debit;
	private static EntryDetail credit;
	
	@BeforeAll
	static void prepare() {
		//会計区分と貸借区分以外はここでは重要でないのでnullを渡している
		debit = new EntryDetail(null, null, LoanType.DEBIT, null);
		credit = new EntryDetail(null, null, LoanType.CREDIT, null);
	}

	@Test
	void 貸借区分が借方なら借方明細と判定される() {
		assertTrue(debit.isDebit());
		assertFalse(debit.isCredit());
	}

	@Test
	void 貸借区分が貸方なら貸方明細と判定される() {
		assertTrue(credit.isCredit());
		assertFalse(credit.isDebit());
	}

}
