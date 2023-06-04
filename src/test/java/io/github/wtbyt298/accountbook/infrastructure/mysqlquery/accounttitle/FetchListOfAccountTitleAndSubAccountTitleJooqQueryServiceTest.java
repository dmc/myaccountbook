package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.SubAccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;

@SpringBootTest
@Transactional
class FetchListOfAccountTitleAndSubAccountTitleJooqQueryServiceTest {
	
	@Autowired
	private FetchListOfAccountTitleAndSubAccountTitleQueryService fetchListQueryService;

	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private SubAccountTitleTestDataCreator subAccountTitleTestDataCreator;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Test
	void ユーザIDを指定して勘定科目と補助科目のIDと名前の一覧を取得できる() {
		//given:ユーザと勘定科目と補助科目が既に作成されている
		User user = userTestDataCreator.create("TEST_USER");
		
		//現金は補助科目を持たない
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		
		//食費は補助科目を持つ
		AccountTitle foodExpenses = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		subAccountTitleTestDataCreator.create(foodExpenses.id(), "0", "その他", user.id());
		subAccountTitleTestDataCreator.create(foodExpenses.id(), "1", "食料品", user.id());

		//when:クエリサービスのメソッドを実行してリストを取得する
		List<AccountTitleAndSubAccountTitleDto> list = fetchListQueryService.fetchAll(user.id());
		
		//then:保存してある勘定科目、補助科目を取得できる
		AccountTitleAndSubAccountTitleDto cashDto = list.get(0);
		AccountTitleAndSubAccountTitleDto foodExpensesDto1 = list.get(1);
		AccountTitleAndSubAccountTitleDto foodExpensesDto2 = list.get(2);
		
		//現金は補助科目を持たないので、補助科目IDと補助科目名はデフォルト値を使う
		assertAll(
			() -> assertEquals("101", cashDto.getAccountTitleId()),
			() -> assertEquals("現金", cashDto.getAccountTitleName()),
			() -> assertEquals("0", cashDto.getSubAccountTitleId()),
			() -> assertEquals("", cashDto.getSubAccountTitleName())
		);
		
		//食費は補助科目を2つ持ち、取得したデータも2件存在する
		assertAll(
			() -> assertEquals("401", foodExpensesDto1.getAccountTitleId()),
			() -> assertEquals("食費", foodExpensesDto1.getAccountTitleName()),
			() -> assertEquals("0", foodExpensesDto1.getSubAccountTitleId()),
			() -> assertEquals("その他", foodExpensesDto1.getSubAccountTitleName()),
			() -> assertEquals("401", foodExpensesDto2.getAccountTitleId()),
			() -> assertEquals("食費", foodExpensesDto2.getAccountTitleName()),
			() -> assertEquals("1", foodExpensesDto2.getSubAccountTitleId()),
			() -> assertEquals("食料品", foodExpensesDto2.getSubAccountTitleName())
		);
	}
	
	@Test
	void ユーザIDに該当する補助科目が存在しない場合は勘定科目のみが取り出される() {
		//given:ユーザが既に作成されている
		User testUser = userTestDataCreator.create("TEST_USER");
		User beginner = userTestDataCreator.create("BEGINNER");
		
		//勘定科目が作成されている
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		AccountTitle foodExpenses = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		
		//ユーザID「TEST_USER」は補助科目を作成している
		//ユーザID「BEGINNER」は補助科目を作成していない
		subAccountTitleTestDataCreator.create(foodExpenses.id(), "0", "その他", testUser.id());
		subAccountTitleTestDataCreator.create(foodExpenses.id(), "1", "食料品", testUser.id());
		
		//when:クエリサービスのメソッドを実行してリストを取得する
		//ユーザID「BEGINNER」を指定する
		List<AccountTitleAndSubAccountTitleDto> list = fetchListQueryService.fetchAll(beginner.id());
		
		//then:勘定科目のみが取り出されている
		//補助科目は取り出されないので、listの要素数は2である
		assertAll(
			() -> assertEquals(2, list.size()),
			() -> assertEquals("101", list.get(0).getAccountTitleId()),
			() -> assertEquals("現金", list.get(0).getAccountTitleName()),
			() -> assertEquals("0", list.get(0).getSubAccountTitleId()),
			() -> assertEquals("", list.get(0).getSubAccountTitleName()),
			() -> assertEquals("401", list.get(1).getAccountTitleId()),
			() -> assertEquals("食費", list.get(1).getAccountTitleName()),
			() -> assertEquals("0", list.get(1).getSubAccountTitleId()),
			() -> assertEquals("", list.get(1).getSubAccountTitleName())
		);
	}

}
