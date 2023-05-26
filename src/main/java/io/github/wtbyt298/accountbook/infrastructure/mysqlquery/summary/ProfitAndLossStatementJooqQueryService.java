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
import generated.tables.Accounttitles;
import generated.tables.MonthlyBalances;
import generated.tables.SubAccounttitles;
import io.github.wtbyt298.accountbook.application.query.model.summary.MonthlyBalanceDto;
import io.github.wtbyt298.accountbook.application.query.model.summary.ProfitAndLossStatementQueryService;
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
	
	//テーブルの別名を定義する
	private final Accounttitles A = ACCOUNTTITLES.as("A");
	private final SubAccounttitles B = SUB_ACCOUNTTITLES.as("B");
	private final MonthlyBalances C = MONTHLY_BALANCES.as("C");
	
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
		return jooq.select(A.ACCOUNTTITLE_ID, B.SUB_ACCOUNTTITLE_NAME, A.ACCOUNTING_TYPE, C.BALANCE)
				   	.from(A)
				   	.leftOuterJoin(B)
				   		.on(A.ACCOUNTTITLE_ID.eq(B.ACCOUNTTITLE_ID))
						.and(B.USER_ID.eq(userId.value()))
					.leftOuterJoin(C)
						.on(C.USER_ID.eq(userId.value()))
						.and(C.FISCAL_YEARMONTH.eq(yearMonth.toString()))
						.and(A.ACCOUNTTITLE_ID.eq(C.ACCOUNTTITLE_ID))
						.and(B.SUB_ACCOUNTTITLE_ID.eq(C.SUB_ACCOUNTTITLE_ID)
							.or(B.SUB_ACCOUNTTITLE_ID.isNull()))
					.where(A.SUMMARY_TYPE.eq(summaryType.toString()))
					.fetch();
	}
	
	/**
	 * レコードをDTOに詰め替える
	 */
	private MonthlyBalanceDto mapRecordToDto(Record4<String, String, String, Integer> record) {
		return new MonthlyBalanceDto(
			AccountTitleId.valueOf(record.get(A.ACCOUNTTITLE_ID)), 
			Optional.ofNullable(record.get(B.SUB_ACCOUNTTITLE_NAME)).orElse(""), 
			AccountingType.valueOf(record.get(A.ACCOUNTING_TYPE)), 
			Optional.ofNullable(record.get(C.BALANCE)).orElse(0)
		);
	}
	
}
