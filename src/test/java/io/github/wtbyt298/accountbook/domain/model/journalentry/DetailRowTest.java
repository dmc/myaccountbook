package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.shared.Amount;
import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType;

class DetailRowTest {
	
	private static DetailRow debit;
	private static DetailRow credit;
	
	@BeforeAll
	static void prepare() {
		//会計区分と貸借区分以外はここでは重要でないので使い回している
		AccountTitleId id = AccountTitleId.valueOf("101");
		SubAccountTitleId subId = SubAccountTitleId.valueOf("0");
		Amount amount = Amount.valueOf(100);
		debit = new DetailRow(id, subId, AccountingType.ASSETS, LoanType.DEBIT, amount);
		credit = new DetailRow(id, subId, AccountingType.LIABILITIES, LoanType.CREDIT, amount);
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

	@Test
	void 会計区分を使って組み合わせ可能かどうかを判断できる() {
		//例えば、　借方：資産　貸方：負債　はOK
		//その逆の、借方：負債　貸方：資産　もOK
		assertTrue(debit.canCombinate(credit));
		assertTrue(credit.canCombinate(debit));
	}

}
