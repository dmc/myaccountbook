package com.github.wtbyt298.myaccountbook.infrastructure.mysqlquery.summary;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.github.wtbyt298.myaccountbook.application.query.model.summary.FinancialStatement;
import com.github.wtbyt298.myaccountbook.application.query.service.summary.ProfitAndLossStatementQueryService;
import com.github.wtbyt298.myaccountbook.application.usecase.shared.AccountBalanceUpdator;
import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import com.github.wtbyt298.myaccountbook.domain.model.user.User;
import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import com.github.wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import com.github.wtbyt298.myaccountbook.helper.testdatacreator.UserTestDataCreator;
import com.github.wtbyt298.myaccountbook.helper.testfactory.JournalEntryTestFactory;

@SpringBootTest
@Transactional
class ProfitAndLossStatementJooqQueryServiceTest {

	@Autowired
	private ProfitAndLossStatementQueryService profitAndLossStatementQueryService;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private AccountBalanceUpdator accountBalanceUpdator;
	
	@Test
	void 年月とユーザIDを指定して科目ごとの残高を保持したオブジェクトを取得できる() {
		//given:ユーザ、勘定科目が作成されている
		User user = userTestDataCreator.create("TEST_USER");
		
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		accountTitleTestDataCreator.create("402", "消耗品費", AccountingType.EXPENSES);
		
		//仕訳を作成し、残高更新処理を行う
		//年月の異なる仕訳を作成する
		JournalEntry entry1 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 5, 1))
			.addDetail("101", "0", LoanType.CREDIT, 3000)
			.addDetail("401", "0", LoanType.DEBIT, 1000)
			.addDetail("402", "0", LoanType.DEBIT, 2000)
			.build();
		
		JournalEntry entry2 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 6, 1))
			.addDetail("101", "0", LoanType.CREDIT, 1000)
			.addDetail("401", "0", LoanType.DEBIT, 700)
			.addDetail("402", "0", LoanType.DEBIT, 300)
			.build();
		
		accountBalanceUpdator.execute(entry1, user.id());
		accountBalanceUpdator.execute(entry2, user.id());
		
		//when:「2023年5月」、「2023年6月」を指定してクエリサービスのメソッドを呼び出す
		FinancialStatement fs_2023_05 = profitAndLossStatementQueryService.aggregateIncludingSubAccountTitle(YearMonth.of(2023, 5), user.id());
		FinancialStatement fs_2023_06 = profitAndLossStatementQueryService.aggregateIncludingSubAccountTitle(YearMonth.of(2023, 6), user.id());
	
		//then:月ごと、会計区分ごとの合計が正しく計算されている
		assertAll(
			() -> assertEquals(3000, fs_2023_05.calculateTotalAmount(AccountingType.EXPENSES)),
			() -> assertEquals(0, fs_2023_05.calculateTotalAmount(AccountingType.REVENUE)),
			() -> assertEquals(1000, fs_2023_06.calculateTotalAmount(AccountingType.EXPENSES)),
			() -> assertEquals(0, fs_2023_06.calculateTotalAmount(AccountingType.REVENUE))
		);
	}

}
