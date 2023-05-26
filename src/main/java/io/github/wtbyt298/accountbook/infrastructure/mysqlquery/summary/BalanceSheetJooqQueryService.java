package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.summary;

import static generated.tables.Accounttitles.ACCOUNTTITLES;
import static generated.tables.MonthlyBalances.MONTHLY_BALANCES;
import static generated.tables.SubAccounttitles.SUB_ACCOUNTTITLES;
import static org.jooq.impl.DSL.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import generated.tables.Accounttitles;
import generated.tables.MonthlyBalances;
import generated.tables.SubAccounttitles;
import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.application.query.model.summary.MonthlyBalanceDto;
import io.github.wtbyt298.accountbook.application.query.service.summary.BalanceSheetQueryService;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;

/**
 * 貸借対照表のデータを取得するクラス
 */
@Component
public class BalanceSheetJooqQueryService implements BalanceSheetQueryService {
	
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
		Result<Record4<String, String, String, BigDecimal>> result = executeQuery(yearMonth, userId, summaryType);
		List<MonthlyBalanceDto> elements = new ArrayList<>();
		for (Record4<String, String, String, BigDecimal> each : result) {
			elements.add(mapRecordToDto(each));
		}
		return new FinancialStatement(elements);
	}
	
	/**
	 * SQLを実行する
	 */
	private Result<Record4<String, String, String, BigDecimal>> executeQuery(YearMonth yearMonth, UserId userId,SummaryType summaryType) {
		//貸借対照表の場合、指定した年月以前の残高を全て足したものを当月の残高とする
		return jooq.select(A.ACCOUNTTITLE_ID, B.SUB_ACCOUNTTITLE_NAME, A.ACCOUNTING_TYPE, sum(C.BALANCE))
				   	.from(A)
				   	.leftOuterJoin(B)
				   		.on(A.ACCOUNTTITLE_ID.eq(B.ACCOUNTTITLE_ID))
						.and(B.USER_ID.eq(userId.value()))
					.leftOuterJoin(C)
						.on(C.USER_ID.eq(userId.value()))
						.and(C.FISCAL_YEARMONTH.lessOrEqual(yearMonth.toString()))
						.and(A.ACCOUNTTITLE_ID.eq(C.ACCOUNTTITLE_ID))
						.and(B.SUB_ACCOUNTTITLE_ID.eq(C.SUB_ACCOUNTTITLE_ID)
							.or(B.SUB_ACCOUNTTITLE_ID.isNull()))
					.where(A.SUMMARY_TYPE.eq(summaryType.toString()))
					.groupBy(A.ACCOUNTTITLE_ID, B.SUB_ACCOUNTTITLE_ID)
					.fetch();
	}
	
	/**
	 * レコードをDTOに詰め替える
	 */
	private MonthlyBalanceDto mapRecordToDto(Record4<String, String, String, BigDecimal> record) {
		Optional<BigDecimal> sum = Optional.ofNullable(record.getValue(record.field4()));
		BigDecimal value = sum.orElse(BigDecimal.ZERO);
		return new MonthlyBalanceDto(
			AccountTitleId.valueOf(record.get(A.ACCOUNTTITLE_ID)), 
			Optional.ofNullable(record.get(B.SUB_ACCOUNTTITLE_NAME)).orElse(""), 
			AccountingType.valueOf(record.get(A.ACCOUNTING_TYPE)), 
			value.intValue()
		);
	}

}
