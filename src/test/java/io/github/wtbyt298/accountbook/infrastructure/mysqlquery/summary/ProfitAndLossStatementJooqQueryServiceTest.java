package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.summary;

import static org.junit.jupiter.api.Assertions.*;

import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.wtbyt298.accountbook.application.query.model.summary.MonthlyBalanceDto;
import io.github.wtbyt298.accountbook.application.query.model.summary.ProfitAndLossStatementQueryService;
import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;

@SpringBootTest
@Transactional
class ProfitAndLossStatementJooqQueryServiceTest {

	@Autowired
	private ProfitAndLossStatementQueryService profitAndLossStatementQueryService;
	
	@Test
	void test() {
		fail("まだ実装されていません");
	}
	
	@Test
	void 取得のテスト() {
		FinancialStatement pl = profitAndLossStatementQueryService.fetch(YearMonth.of(2023, 5), UserId.valueOf("wtbyt298"), SummaryType.PL);
		List<MonthlyBalanceDto> data = pl.filteredByAccountTitle(AccountTitleId.valueOf("501"));
		for (MonthlyBalanceDto each : data) {
			System.out.println(each.getSubAccountTitleName());
			System.out.println(each.getBalance());
		}
		
	}

}
