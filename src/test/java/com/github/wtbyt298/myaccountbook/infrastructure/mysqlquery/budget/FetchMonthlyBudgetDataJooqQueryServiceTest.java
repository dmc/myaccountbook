package com.github.wtbyt298.myaccountbook.infrastructure.mysqlquery.budget;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.github.wtbyt298.myaccountbook.application.query.model.budget.MonthlyBudgetDto;
import com.github.wtbyt298.myaccountbook.application.query.service.budget.FetchMonthlyBudgetDataQueryService;
import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.budget.MonthlyBudget;
import com.github.wtbyt298.myaccountbook.domain.model.budget.MonthlyBudgetRepository;
import com.github.wtbyt298.myaccountbook.domain.model.user.User;
import com.github.wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import com.github.wtbyt298.myaccountbook.helper.testdatacreator.UserTestDataCreator;

@SpringBootTest
@Transactional
class FetchMonthlyBudgetDataJooqQueryServiceTest {

	@Autowired
	private FetchMonthlyBudgetDataQueryService fetchMonthlyBudgetDataQueryService;
	
	@Autowired
	private MonthlyBudgetRepository monthlyBudgetRepository;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Test
	void ユーザIDを指定して予算データのリストを取得できる() {
		//given:勘定科目が作成されている
		//勘定科目の数は2
		AccountTitle a1 = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		AccountTitle a2 = accountTitleTestDataCreator.create("402", "消耗品費", AccountingType.EXPENSES);
		
		//ユーザが作成されている
		//ユーザの数は2
		User user1 = userTestDataCreator.create("TEST_USER_01");
		User user2 = userTestDataCreator.create("TEST_USER_02");
		
		//予算が保存されている
		//TEST_USER_01の予算データを作成
		monthlyBudgetRepository.save(MonthlyBudget.valueOf(10000), a1.id(), user1.id());
		monthlyBudgetRepository.save(MonthlyBudget.valueOf(1000), a2.id(), user1.id());
		
		//TEST_USER_02の予算データを作成
		monthlyBudgetRepository.save(MonthlyBudget.valueOf(50000), a1.id(), user2.id());
		monthlyBudgetRepository.save(MonthlyBudget.valueOf(5000), a2.id(), user2.id());
		
		//when:ユーザごとに予算データを取得する
		List<MonthlyBudgetDto> data1 = fetchMonthlyBudgetDataQueryService.fetchAll(user1.id());
		List<MonthlyBudgetDto> data2 = fetchMonthlyBudgetDataQueryService.fetchAll(user2.id());
		
		//then:ユーザごとに設定した予算額を取得できる
		assertAll(
			//データの数はそれぞれ2
			() -> assertEquals(2, data1.size()),
			() -> assertEquals(2, data2.size()),
			//TEST_USER_01の予算データのアサーション
			() -> assertEquals("401", data1.get(0).getAccountTitleId()),
			() -> assertEquals(10000, data1.get(0).getBudgetAmount()),
			() -> assertEquals("402", data1.get(1).getAccountTitleId()),
			() -> assertEquals(1000, data1.get(1).getBudgetAmount()),
			//TEST_USER_02の予算データのアサーション
			() -> assertEquals("401", data2.get(0).getAccountTitleId()),
			() -> assertEquals(50000, data2.get(0).getBudgetAmount()),
			() -> assertEquals("402", data2.get(1).getAccountTitleId()),
			() -> assertEquals(5000, data2.get(1).getBudgetAmount())
		);
	}
	
	@Test
	void 予算が保存されていない場合は予算額は0となる() {
		//given:勘定科目が作成されている
		AccountTitle a = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		
		//ユーザが作成されている
		User user = userTestDataCreator.create("TEST_USER");
		
		//予算は保存されていない
		
		//when:予算データを取得する
		List<MonthlyBudgetDto> data = fetchMonthlyBudgetDataQueryService.fetchAll(user.id());
		
		//then:予算額0でデータを取得できる
		assertAll(
			() -> assertEquals(1, data.size()),
			() -> assertEquals(a.id().value(), data.get(0).getAccountTitleId()),
			() -> assertEquals(a.name().value(), data.get(0).getAccountTitleName()),
			() -> assertEquals(0, data.get(0).getBudgetAmount())
		);
	}
	
	@Test
	void 勘定科目のうち費用の科目のみが取り出される() {
		//given:勘定科目が作成されている
		//費用の科目のIDは4で始まる
		accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		accountTitleTestDataCreator.create("402", "消耗品費", AccountingType.EXPENSES);
		
		//費用以外の科目も存在する
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		accountTitleTestDataCreator.create("201", "未払金", AccountingType.LIABILITIES);
		accountTitleTestDataCreator.create("301", "貯蓄", AccountingType.EQUITY);
		accountTitleTestDataCreator.create("501", "給与", AccountingType.REVENUE);
		
		//ユーザが作成されている
		User  user = userTestDataCreator.create("TEST_USER");
		
		//when:予算データを取得する
		List<MonthlyBudgetDto> data = fetchMonthlyBudgetDataQueryService.fetchAll(user.id());
		
		//then:費用の科目のみのデータを取得できる
		assertTrue(
			data.stream().allMatch(each -> 
				each.getAccountTitleId().matches("4.*")
			)
		);
	}
	
}
