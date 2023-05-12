package io.github.wtbyt298.accountbook.application.usecase.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.github.wtbyt298.accountbook.domain.model.account.Account;
import io.github.wtbyt298.accountbook.domain.model.account.AccountRepository;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.DealDate;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Description;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetails;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testfactory.AccountTitleTestFactory;
import io.github.wtbyt298.accountbook.helper.testfactory.EntryDetailTestFactory;
import io.github.wtbyt298.accountbook.helper.testfactory.UserTestFactory;

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
			.thenReturn(new Account(expenses, SubAccountTitleId.valueOf("0"), YearMonth.now(), new ArrayList<>()));
		AccountTitle assets = AccountTitleTestFactory.create("101", "現金", AccountingType.EXPENSES);
		when(accountTitleRepository.findById(assets.id())).thenReturn(assets);
		when(accountRepository.find(eq(assets), any(), any(), any()))
			.thenReturn(new Account(assets, SubAccountTitleId.valueOf("0"), YearMonth.now(), new ArrayList<>()));
	}

	@Test
	void test() {
		//given:
		ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
		
		JournalEntry entry = createTestJournalEntry();
		User user = UserTestFactory.create();
		
		//when:仕訳とユーザIDを渡してテスト対象メソッドを実行する
		accountBalanceUpdator.execute(entry, user.id());
		final int count = entry.entryDetails().size(); //仕訳が保持している仕訳明細の数
		verify(accountRepository, times(count)).save(captor.capture(), any()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then:仕訳明細の金額が勘定の残高に反映されている
		List<Account> capturedAccounts = captor.getAllValues();
		assertEquals("401", capturedAccounts.get(0).accountTitleId().value());
		assertEquals(1000, capturedAccounts.get(0).balance()); //費用が1000円発生しているので残高は「1000円」
		assertEquals("101", capturedAccounts.get(1).accountTitleId().value());
		assertEquals(-1000, capturedAccounts.get(1).balance()); //資産が1000円減少しているので残高は「-1000円」
	}
	
	/**
	 * テスト用の仕訳インスタンスを生成する
	 * 借方　勘定科目ID：401　金額：1000円
	 * 貸方　勘定科目ID：101　金額：1000円
	 */
	private JournalEntry createTestJournalEntry() {
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(EntryDetailTestFactory.create("401", "0", LoanType.DEBIT, 1000));
		elements.add(EntryDetailTestFactory.create("101", "0", LoanType.CREDIT, 1000));
		return JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳です。"), 
			new EntryDetails(elements)
		);
	}

}
