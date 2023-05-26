package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.summary;

import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.Result;
import static generated.tables.MonthlyBalances.*;
import static generated.tables.Accounttitles.*;
import static generated.tables.SubAccounttitles.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.application.query.model.summary.MonthlyBalanceDto;
import io.github.wtbyt298.accountbook.application.query.service.summary.ProfitAndLossStatementQueryService;
import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;

/**
 * 損益計算書のデータを取得するクラス
 */
@Component
public class ProfitAndLossStatementJooqQueryService implements ProfitAndLossStatementQueryService {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 科目ごとの月次残高を取得する
	 */
	@Override
	public FinancialStatement fetch(YearMonth yearMonth, UserId userId, SummaryType summaryType) {
		Result<Record4<String, String, String, Integer>> result = executeQuery(yearMonth, userId, summaryType);
		List<MonthlyBalanceDto> elements = new ArrayList<>();
		for (Record4<String, String, String, Integer> each : result) {
			elements.add(mapRecordToDto(each));
		}
		return new FinancialStatement(elements);
	}
	
	/**
	 * SQLを実行する
	 */
	private Result<Record4<String, String, String, Integer>> executeQuery(YearMonth yearMonth, UserId userId,SummaryType summaryType) {
		return jooq.select(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, ACCOUNTTITLES.ACCOUNTING_TYPE, MONTHLY_BALANCES.BALANCE)
				   	.from(ACCOUNTTITLES)
				   	.leftOuterJoin(SUB_ACCOUNTTITLES)
				   		.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID))
						.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.value()))
					.leftOuterJoin(MONTHLY_BALANCES)
						.on(MONTHLY_BALANCES.USER_ID.eq(userId.value()))
						.and(MONTHLY_BALANCES.FISCAL_YEARMONTH.eq(yearMonth.toString()))
						.and(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.ACCOUNTTITLE_ID))
						.and(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID)
							.or(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID.isNull()))
					.where(ACCOUNTTITLES.SUMMARY_TYPE.eq(summaryType.toString()))
					.fetch();
	}
	
	/**
	 * レコードをDTOに詰め替える
	 */
	private MonthlyBalanceDto mapRecordToDto(Record4<String, String, String, Integer> record) {
		return new MonthlyBalanceDto(
			AccountTitleId.valueOf(record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID)), 
			Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME)).orElse(""), 
			AccountingType.valueOf(record.get(ACCOUNTTITLES.ACCOUNTING_TYPE)), 
			Optional.ofNullable(record.get(MONTHLY_BALANCES.BALANCE)).orElse(0)
		);
	}
	
}
