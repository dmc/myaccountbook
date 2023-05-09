package io.github.wtbyt298.accountbook.domain.model.account;

import static org.junit.jupiter.api.Assertions.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testfactory.AccountTitleTestFactory;

class AccountTest {

	@Test
	void 借方勘定の場合借方合計から貸方合計を引いた結果が残高となる() {
		//given:現金勘定
		//借方勘定なので、残高は 1000 - 500 = 500 となるはず
		List<AccountingTransaction> transactionHistory = new ArrayList<>();
		transactionHistory.add(new AccountingTransaction(LoanType.DEBIT, Amount.valueOf(1000)));
		transactionHistory.add(new AccountingTransaction(LoanType.CREDIT, Amount.valueOf(500)));
		Account account = new Account(
			AccountTitleTestFactory.create("101", "現金", AccountingType.ASSETS), 
			SubAccountTitleId.valueOf("0"), 
			YearMonth.of(2023, 4), 
			transactionHistory
		);
		
		//when:
		int balance = account.balance();
		
		//then:
		assertEquals(500, balance);
	}
	
	@Test
	void 貸方勘定の場合貸方合計から借方合計を引いた結果が残高となる() {
		//given:未払金勘定
		//貸方勘定なので、残高は 500 - 1000 = -500 となるはず
		List<AccountingTransaction> transactionHistory = new ArrayList<>();
		transactionHistory.add(new AccountingTransaction(LoanType.DEBIT, Amount.valueOf(1000)));
		transactionHistory.add(new AccountingTransaction(LoanType.CREDIT, Amount.valueOf(500)));
		Account account = new Account(
			AccountTitleTestFactory.create("201", "未払金", AccountingType.LIABILITIES), 
			SubAccountTitleId.valueOf("0"), 
			YearMonth.of(2023, 4), 
			transactionHistory
		);
		
		//when:
		int balance = account.balance();
		
		//then:
		assertEquals(-500, balance);
	}
	
	@Test
	void test() {
		//given:取引履歴を持たない勘定
		Account account = new Account(
			AccountTitleTestFactory.create("101", "現金", AccountingType.ASSETS), 
			SubAccountTitleId.valueOf("0"), 
			YearMonth.of(2023, 4), 
			new ArrayList<>()
		);
		assertEquals(0, account.balance());
		
		//when:取引履歴を追加する
		Account updated = account.addTransaction(new AccountingTransaction(LoanType.DEBIT, Amount.valueOf(1000)));
		
		//then:残高が変化している
		assertEquals(1000, updated.balance());
		assertEquals(0, account.balance()); //追加前の勘定の残高は変化しない
	}

}
