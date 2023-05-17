package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.account;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.account.Account;
import io.github.wtbyt298.accountbook.domain.model.account.AccountRepository;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testfactory.JournalEntryTestFactory;

@SpringBootTest
@Transactional
class AccountJooqRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Test
	void 保存した勘定を検索メソッドで取得できる() {
		//given:ユーザと勘定科目が既に作成されている
		//仕訳はまだ作成されていない
		User user = userTestDataCreator.create();
		AccountTitle assets = accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		Account created = new Account(assets, SubAccountTitleId.valueOf("0"), YearMonth.of(2023, 4), new ArrayList<>());
		
		//when:リポジトリに勘定を保存し、取り出す
		accountRepository.save(created, user.id());
		Account found = accountRepository.find(assets, SubAccountTitleId.valueOf("0"), user.id(), YearMonth.of(2023, 4));
		
		//then:保存した勘定を取得できる
		assertEquals(found.balance(), created.balance());
		assertEquals(found.accountTitleId(), created.accountTitleId());
		assertEquals(found.fiscalYearMonth(), created.fiscalYearMonth());
	}
	
	@Test
	void 仕訳が既に作成されている場合は仕訳金額が残高に反映された状態で勘定を取得できる() {
		//given:ユーザと勘定科目が既に作成されている
		User user = userTestDataCreator.create();
		AccountTitle assets = accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		AccountTitle expenses = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		//この時点では各勘定の残高は0である
		YearMonth current = YearMonth.of(2023, 4);
		Account accountOfAssets = accountRepository.find(assets, SubAccountTitleId.valueOf("0"), user.id(), current);
		Account accountOfExpenses = accountRepository.find(expenses, SubAccountTitleId.valueOf("0"), user.id(), current);
		assertEquals(0, accountOfAssets.balance());
		assertEquals(0, accountOfExpenses.balance());
		//仕訳を作成して保存する
		JournalEntry entry = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 4, 1))
			.addDetail(expenses.id().value(), "0", LoanType.DEBIT, 1000)
			.addDetail(assets.id().value(), "0", LoanType.CREDIT, 1000)
			.build();
		journalEntryRepository.save(entry, user.id());
		
		//when:再度勘定を取得する
		Account accountOfAssetsUpdated = accountRepository.find(assets, SubAccountTitleId.valueOf("0"), user.id(), current);
		Account accountOfExpensesUpdated = accountRepository.find(expenses, SubAccountTitleId.valueOf("0"), user.id(), current);
		
		//then:勘定の残高が正しく更新されている
		assertEquals(-1000, accountOfAssetsUpdated.balance());
		assertEquals(1000, accountOfExpensesUpdated.balance());
		//異なる会計年月の勘定を取得した場合、残高は0である
		Account previousMonth = accountRepository.find(assets, SubAccountTitleId.valueOf("0"), user.id(), current.minusMonths(1)); //前月の現金勘定
		assertEquals(0, previousMonth.balance());
	}

}
