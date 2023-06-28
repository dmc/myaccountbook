package com.github.wtbyt298.myaccountbook.application.usecase.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.wtbyt298.myaccountbook.application.usecase.shared.AccountBalanceUpdator;
import com.github.wtbyt298.myaccountbook.domain.model.account.Account;
import com.github.wtbyt298.myaccountbook.domain.model.account.AccountRepository;
import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.user.User;
import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import com.github.wtbyt298.myaccountbook.helper.testfactory.AccountTitleTestFactory;
import com.github.wtbyt298.myaccountbook.helper.testfactory.JournalEntryTestFactory;
import com.github.wtbyt298.myaccountbook.helper.testfactory.UserTestFactory;

class AccountBalanceUpdatorTest {
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private AccountTitleRepository accountTitleRepository;
	
	@InjectMocks
	private AccountBalanceUpdator accountBalanceUpdator;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		//依存オブジェクトの設定
		AccountTitle expenses = AccountTitleTestFactory.create("401", "食費", AccountingType.EXPENSES);
		when(accountTitleRepository.findById(expenses.id())).thenReturn(expenses);
		when(accountRepository.find(eq(expenses), any(), any(), any()))
			.thenReturn(new Account(expenses, SubAccountTitleId.valueOf("0"), YearMonth.now(), 0));
		
		AccountTitle assets = AccountTitleTestFactory.create("101", "現金", AccountingType.EXPENSES);
		when(accountTitleRepository.findById(assets.id())).thenReturn(assets);
		when(accountRepository.find(eq(assets), any(), any(), any()))
			.thenReturn(new Account(assets, SubAccountTitleId.valueOf("0"), YearMonth.now(), 0));
	}

	@Test
	void 仕訳を渡すと各勘定の残高が更新された状態でリポジトリに渡される() {
		//given:ユーザが作成されている
		User user = UserTestFactory.create();
		
		//仕訳を作成する
		ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
		JournalEntry entry = new JournalEntryTestFactory.Builder()
			.addDetail("401", "0", LoanType.DEBIT, 1000)
			.addDetail("101", "0", LoanType.CREDIT, 1000)
			.build();
		
		//when:仕訳とユーザIDを渡してテスト対象メソッドを実行する
		accountBalanceUpdator.execute(entry, user.id());
		final int count = entry.entryDetails().size(); //仕訳が保持している仕訳明細の数
		verify(accountRepository, times(count)).save(captor.capture(), any()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then:仕訳明細の金額が勘定の残高に反映されている
		List<Account> capturedAccounts = captor.getAllValues();
		assertAll(
			() -> assertEquals("401", capturedAccounts.get(0).accountTitleId().value()),
			//費用が1000円発生しているので残高は「1000円」
			() -> assertEquals(1000, capturedAccounts.get(0).balance()), 
			() -> assertEquals("101", capturedAccounts.get(1).accountTitleId().value()),
			//資産が1000円減少しているので残高は「-1000円」
			() -> assertEquals(-1000, capturedAccounts.get(1).balance()) 
		);
	}

}
