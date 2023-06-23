package io.github.wtbyt298.myaccountbook.domain.model.account;

import static org.junit.jupiter.api.Assertions.*;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.myaccountbook.helper.testfactory.AccountTitleTestFactory;

class AccountTest {

	@Test
	void 借方勘定の場合明細の貸借区分が借方であれば残高は増加する() {
		//given:現金勘定
		Account account = new Account(
			AccountTitleTestFactory.create("101", "現金", AccountingType.ASSETS), 
			SubAccountTitleId.valueOf("0"), 
			YearMonth.of(2023, 4), 
			1000
		);
		
		//when:残高を更新する
		Account increased = account.updateBalance(LoanType.DEBIT, Amount.valueOf(500));
		Account decreased = account.updateBalance(LoanType.CREDIT, Amount.valueOf(500));
		
		//then:残高が正しく計算されている
		assertEquals(1500, increased.balance());
		assertEquals(500, decreased.balance());
	}
	
	@Test
	void 貸方勘定の場合明細の貸借区分が貸方であれば残高は増加する() {
		//given:未払金勘定
		Account account = new Account(
			AccountTitleTestFactory.create("201", "未払金", AccountingType.LIABILITIES), 
			SubAccountTitleId.valueOf("0"), 
			YearMonth.of(2023, 4), 
			1000
		);
		
		//when:残高を更新する
		Account increased = account.updateBalance(LoanType.CREDIT, Amount.valueOf(500));
		Account decreased = account.updateBalance(LoanType.DEBIT, Amount.valueOf(500));
		
		//then:残高が正しく計算されている
		assertEquals(1500, increased.balance());
		assertEquals(500, decreased.balance());
	}
	
}
