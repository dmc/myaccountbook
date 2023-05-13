package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.AccountTitleAndSubAccountTitleListQueryService;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;

@SpringBootTest
@Transactional
class AccountTitleAndSubAccountTitleListJooqQueryServiceTest {
	
	@Autowired
	private AccountTitleAndSubAccountTitleListQueryService listQueryService;

	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Test
	void 勘定科目と補助科目テーブルからIDと科目名を取得できる() {
		//given:ユーザと勘定科目と補助科目が既に作成されている
		User user = userTestDataCreator.create();
		List<AccountTitle> accountTitles  = new ArrayList<>();
		//現金は補助科目を持たない
		AccountTitle cash = accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		accountTitles.add(cash);
		//食費は補助科目を持つ
		AccountTitle foodExpenses = accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		accountTitles.add(foodExpenses);
		SubAccountTitles subAccountTitles = new SubAccountTitles(new HashMap<>(), foodExpenses.id());
		subAccountTitles.add(SubAccountTitleName.valueOf("食料品"));
		subAccountTitleRepository.save(subAccountTitles, user.id());
		
		//when:クエリサービスのメソッドを実行してリストを取得する
		List<AccountTitleAndSubAccountTitleDto> dto = listQueryService.findAll(user.id());
		
		//then:保存してある勘定科目、補助科目を取得できる
		AccountTitleAndSubAccountTitleDto cashDto = dto.get(0);
		AccountTitleAndSubAccountTitleDto foodExpensesDto1 = dto.get(1);
		AccountTitleAndSubAccountTitleDto foodExpensesDto2 = dto.get(2);
		//現金は補助科目を持たないので、補助科目IDと補助科目名はデフォルト値を使う
		assertEquals("101", cashDto.getAccountTitleId());
		assertEquals("現金", cashDto.getAccountTitleName());
		assertEquals("0", cashDto.getSubAccountTitleId());
		assertEquals("", cashDto.getSubAccountTitleName());
		//食費は補助科目を2つ持ち、取得したデータも2件存在する
		assertEquals("401", foodExpensesDto1.getAccountTitleId());
		assertEquals("食費", foodExpensesDto1.getAccountTitleName());
		assertEquals("0", foodExpensesDto1.getSubAccountTitleId());
		assertEquals("その他", foodExpensesDto1.getSubAccountTitleName());
		assertEquals("401", foodExpensesDto2.getAccountTitleId());
		assertEquals("食費", foodExpensesDto2.getAccountTitleName());
		assertEquals("1", foodExpensesDto2.getSubAccountTitleId());
		assertEquals("食料品", foodExpensesDto2.getSubAccountTitleName());
	}

}
