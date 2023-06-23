package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.budget;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.budget.MonthlyBudget;
import io.github.wtbyt298.accountbook.domain.model.budget.MonthlyBudgetRepository;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;

@SpringBootTest
@Transactional
class MonthlyBudgetJooqRepositoryTest {
	
	@Autowired
	private MonthlyBudgetRepository monthlyBudgetRepository;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;

	@Test
	void 勘定科目とユーザごとに予算を保存し検索メソッドで取得できる() {
		//given:勘定科目が作成されている
		AccountTitle accountTitle = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		
		//ユーザが作成されている
		User user1 = userTestDataCreator.create("TEST_USER_01");
		User user2 = userTestDataCreator.create("TEST_USER_02");
		
		//予算オブジェクトを生成
		MonthlyBudget created1 = MonthlyBudget.valueOf(10000);
		MonthlyBudget created2 = MonthlyBudget.valueOf(20000);
		
		//when:予算を保存し、その後ユーザIDを指定して取得する
		monthlyBudgetRepository.save(created1, accountTitle.id(), user1.id());
		monthlyBudgetRepository.save(created2, accountTitle.id(), user2.id());
		
		Map<AccountTitleId, MonthlyBudget> budgets1 = monthlyBudgetRepository.findAll(user1.id());
		Map<AccountTitleId, MonthlyBudget> budgets2 = monthlyBudgetRepository.findAll(user2.id());
		
		//then:予算額が正しく設定されている
		MonthlyBudget found1 = budgets1.get(accountTitle.id());
		MonthlyBudget found2 = budgets2.get(accountTitle.id());
		
		assertAll(
			() -> assertEquals(created1, found1),
			() -> assertEquals(created2, found2)
 		);
	}
	
	@Test
	void 予算が保存されていない状態で検索メソッドを呼び出すと予算額は0になる() {
		//given:予算は設定されていない
		//勘定科目とユーザが作成されている
		AccountTitle accountTitle = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		User user = userTestDataCreator.create("TEST_USER");
		
		//when:ユーザIDを指定して予算を取得する
		Map<AccountTitleId, MonthlyBudget> budgets = monthlyBudgetRepository.findAll(user.id());
		
		//then:予算額は0である
		MonthlyBudget found = budgets.get(accountTitle.id());
		assertEquals(0, found.value());
	}
	
	@Test
	void DBに予算が保存されていれば存在チェックメソッドはtrueを返す() {
		//given:勘定科目とユーザが作成されている
		AccountTitle accountTitle = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		User user = userTestDataCreator.create("TEST_USER");
		
		//予算はまだ保存されていない
		assertFalse(monthlyBudgetRepository.exists(accountTitle.id(), user.id()));
		
		//when:予算を保存する
		MonthlyBudget monthlyBudget = MonthlyBudget.valueOf(10000);
		monthlyBudgetRepository.save(monthlyBudget, accountTitle.id(), user.id());
		
		//then:存在チェックメソッドがtrueを返す
		assertTrue(monthlyBudgetRepository.exists(accountTitle.id(), user.id()));
	}
	
}
