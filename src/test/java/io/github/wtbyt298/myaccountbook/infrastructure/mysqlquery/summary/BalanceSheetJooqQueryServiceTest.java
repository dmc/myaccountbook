package io.github.wtbyt298.myaccountbook.infrastructure.mysqlquery.summary;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.wtbyt298.myaccountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.myaccountbook.application.query.service.summary.BalanceSheetQueryService;
import io.github.wtbyt298.myaccountbook.application.usecase.shared.AccountBalanceUpdator;
import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.myaccountbook.domain.model.user.User;
import io.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.myaccountbook.helper.testdatacreator.UserTestDataCreator;
import io.github.wtbyt298.myaccountbook.helper.testfactory.JournalEntryTestFactory;

@SpringBootTest
@Transactional
class BalanceSheetJooqQueryServiceTest {

	@Autowired
	private BalanceSheetQueryService balanceSheetQueryService;
	
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
		accountTitleTestDataCreator.create("201", "未払金", AccountingType.LIABILITIES);
		accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		
		//仕訳を作成し、残高更新処理を行う
		//年月の異なる仕訳を作成する
		JournalEntry entry1 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 5, 1))
			.addDetail("101", "0", LoanType.CREDIT, 1000)
			.addDetail("201", "0", LoanType.CREDIT, 1000)
			.addDetail("401", "0", LoanType.DEBIT, 2000)
			.build();
		
		JournalEntry entry2 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 6, 1))
			.addDetail("101", "0", LoanType.CREDIT, 1500)
			.addDetail("201", "0", LoanType.CREDIT, 1500)
			.addDetail("401", "0", LoanType.DEBIT, 3000)
			.build();
		
		accountBalanceUpdator.execute(entry1, user.id());
		accountBalanceUpdator.execute(entry2, user.id());
		
		//when:「2023年5月」、「2023年6月」を指定してクエリサービスのメソッドを呼び出す
		FinancialStatement fs_2023_05 = balanceSheetQueryService.aggregateIncludingSubAccountTitle(YearMonth.of(2023, 5), user.id());
		FinancialStatement fs_2023_06 = balanceSheetQueryService.aggregateIncludingSubAccountTitle(YearMonth.of(2023, 6), user.id());
		
		//then:月ごと、会計区分ごとの合計が正しく計算されている
		//SummaryType.BSを指定した場合、該当年月以前の全ての残高を足したものが計算結果となる
		assertAll(
			() -> assertEquals(-1000, fs_2023_05.calculateTotalAmount(AccountingType.ASSETS)),
			() -> assertEquals(1000, fs_2023_05.calculateTotalAmount(AccountingType.LIABILITIES)),
			() -> assertEquals(-2500, fs_2023_06.calculateTotalAmount(AccountingType.ASSETS)),
			() -> assertEquals(2500, fs_2023_06.calculateTotalAmount(AccountingType.LIABILITIES))
		);
	}

}