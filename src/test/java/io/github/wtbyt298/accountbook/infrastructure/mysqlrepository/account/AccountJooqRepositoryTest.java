package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.account;

import static org.junit.jupiter.api.Assertions.*;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.account.Account;
import io.github.wtbyt298.accountbook.domain.model.account.AccountRepository;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;

@SpringBootTest
@Transactional
class AccountJooqRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Test
	void 保存した勘定を検索メソッドで取得できる() {
		//given:ユーザと勘定科目が既に作成されている
		User user = userTestDataCreator.create();
		AccountTitle assets = accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		Account created = new Account(assets, SubAccountTitleId.valueOf("0"), YearMonth.of(2023, 4), 0);
		
		//when:リポジトリに勘定を保存し、取り出す
		accountRepository.save(created, user.id());
		Account found = accountRepository.find(assets, SubAccountTitleId.valueOf("0"), user.id(), YearMonth.of(2023, 4));
		
		//then:保存した勘定を取得できる
		assertEquals(found.balance(), created.balance());
		assertEquals(found.accountTitleId(), created.accountTitleId());
		assertEquals(found.fiscalYearMonth(), created.fiscalYearMonth());
	}
	
	@Test
	void 残高テーブルにデータが存在する場合は仕訳金額が残高に反映された状態で勘定を取得できる() {
		//given:ユーザと勘定科目が既に作成されている
		User user = userTestDataCreator.create();
		AccountTitle assets = accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		
		//残高を設定して勘定を保存する
		Account created = new Account(assets, SubAccountTitleId.valueOf("0"), YearMonth.of(2023, 4), 1000);
		accountRepository.save(created, user.id());		
		
		//when:勘定の残高を更新してリポジトリに保存し、再度取り出す
		Account updated = created.updateBalance(LoanType.DEBIT, Amount.valueOf(5000));
		accountRepository.save(updated, user.id());
		Account found = accountRepository.find(assets, SubAccountTitleId.valueOf("0"), user.id(), YearMonth.of(2023, 4));
		
		//then:勘定の残高が正しく更新されている
		assertEquals(updated.balance(), found.balance());
		
		//異なる会計年月の勘定を取得した場合、残高は0である
		Account previousMonth = accountRepository.find(assets, SubAccountTitleId.valueOf("0"), user.id(), YearMonth.of(2023, 3)); //前月の現金勘定
		assertEquals(0, previousMonth.balance());
	}

}
