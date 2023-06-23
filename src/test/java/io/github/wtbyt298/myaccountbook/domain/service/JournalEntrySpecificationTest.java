package io.github.wtbyt298.myaccountbook.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.myaccountbook.helper.testfactory.AccountTitleTestFactory;
import io.github.wtbyt298.myaccountbook.helper.testfactory.JournalEntryTestFactory;

class JournalEntrySpecificationTest {

	@Mock
	private AccountTitleRepository accountTitleRepository;
	
	@InjectMocks
	private JournalEntrySpecification journalEntrySpecification;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		//依存オブジェクトの設定
		AccountTitleId assets = AccountTitleId.valueOf("101");
		AccountTitleId liabilities = AccountTitleId.valueOf("201");
		AccountTitleId equity = AccountTitleId.valueOf("301");
		AccountTitleId expenses = AccountTitleId.valueOf("401");
		AccountTitleId revenue = AccountTitleId.valueOf("501");
		
		when(accountTitleRepository.findById(assets))
			.thenReturn(AccountTitleTestFactory.create("101", "現金", AccountingType.ASSETS));
		when(accountTitleRepository.findById(liabilities))
			.thenReturn(AccountTitleTestFactory.create("201", "未払金", AccountingType.LIABILITIES));
		when(accountTitleRepository.findById(equity))
			.thenReturn(AccountTitleTestFactory.create("301", "貯蓄", AccountingType.EQUITY));
		when(accountTitleRepository.findById(expenses))
			.thenReturn(AccountTitleTestFactory.create("401", "食費", AccountingType.EXPENSES));
		when(accountTitleRepository.findById(revenue))
			.thenReturn(AccountTitleTestFactory.create("501", "給与", AccountingType.REVENUE));
	}
	
	@Test
	void 明細行の貸借組み合わせが正しければtrueを返す() {
		//given:
		//借方：資産　貸方：負債、収益
		//借方：費用　貸方：負債、収益
		//今回の例だと全てtrueになるはずである

		//when:仕訳のインスタンスを生成する
		JournalEntry entry = new JournalEntryTestFactory.Builder()
			.addDetail("101", "0", LoanType.DEBIT, 100)
			.addDetail("401", "0", LoanType.DEBIT, 100)
			.addDetail("201", "0", LoanType.CREDIT, 100)
			.addDetail("501", "0", LoanType.CREDIT, 100)
			.build();
		
		//then:仕様オブジェクトによる整合性チェックを通過する
		assertTrue(journalEntrySpecification.isSatisfied(entry));
	}
	
	@Test
	void 明細行の貸借組み合わせが誤っていればfalseを返す() {
		//given:
		//借方：収益　貸方：費用
		//今回の例だとfalseになるはずである

		//when:仕訳のインスタンスを生成する
		JournalEntry entry = new JournalEntryTestFactory.Builder()
			.addDetail("501", "0", LoanType.DEBIT, 100)
			.addDetail("401", "0", LoanType.CREDIT, 100)
			.build();
		
		//then:仕様オブジェクトによる整合性チェックを通過しない
		assertFalse(journalEntrySpecification.isSatisfied(entry));
	}

}
