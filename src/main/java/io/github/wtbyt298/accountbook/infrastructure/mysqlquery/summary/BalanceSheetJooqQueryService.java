package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.summary;

import static generated.tables.Accounttitles.ACCOUNTTITLES;
import static generated.tables.MonthlyBalances.MONTHLY_BALANCES;
import static generated.tables.SubAccounttitles.SUB_ACCOUNTTITLES;
import static org.jooq.impl.DSL.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
		
	/**
	 * 科目ごとの月次残高を取得する
	 */
	@Override
	public FinancialStatement fetch(YearMonth yearMonth, UserId userId, SummaryType summaryType) {
		Result<Record4<String, String, String, BigDecimal>> result = executeQuery(yearMonth, userId, summaryType);
		List<MonthlyBalanceDto> data = result.stream()
			.map(record -> mapRecordToDto(record))
			.toList();
		return new FinancialStatement(data);
	}
	
	/**
	 * SQLを実行する
	 */
	private Result<Record4<String, String, String, BigDecimal>> executeQuery(YearMonth yearMonth, UserId userId,SummaryType summaryType) {
		//貸借対照表の場合、指定した年月以前の残高を全て足したものを当月の残高とする
		return jooq.select(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, ACCOUNTTITLES.ACCOUNTING_TYPE, sum(MONTHLY_BALANCES.BALANCE))
		   	.from(ACCOUNTTITLES)
		   	.leftOuterJoin(SUB_ACCOUNTTITLES)
		   		.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID))
				.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.value()))
			.leftOuterJoin(MONTHLY_BALANCES)
				.on(MONTHLY_BALANCES.USER_ID.eq(userId.value()))
				.and(MONTHLY_BALANCES.FISCAL_YEARMONTH.lessOrEqual(yearMonth.toString()))
				.and(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.ACCOUNTTITLE_ID))
				.and(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID)
					.or(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID.isNull()))
			.where(ACCOUNTTITLES.SUMMARY_TYPE.eq(summaryType.toString()))
			.groupBy(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)
			.fetch();
	}
	
	/**
	 * レコードをDTOに詰め替える
	 */
	private MonthlyBalanceDto mapRecordToDto(Record4<String, String, String, BigDecimal> record) {
		//sum関数で集計したフィールドの値を取り出す
		Optional<BigDecimal> sum = Optional.ofNullable(record.getValue(record.field4()));
		BigDecimal value = sum.orElse(BigDecimal.ZERO);
		return new MonthlyBalanceDto(
			AccountTitleId.valueOf(record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID)), 
			Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME)).orElse(""), 
			AccountingType.valueOf(record.get(ACCOUNTTITLES.ACCOUNTING_TYPE)), 
			value.intValue()
		);
	}

}
