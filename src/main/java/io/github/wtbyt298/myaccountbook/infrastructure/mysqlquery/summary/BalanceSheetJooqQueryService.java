package io.github.wtbyt298.myaccountbook.infrastructure.mysqlquery.summary;

import static generated.tables.Accounttitles.ACCOUNTTITLES;
import static generated.tables.MonthlyBalances.MONTHLY_BALANCES;
import static generated.tables.SubAccounttitles.SUB_ACCOUNTTITLES;
import static org.jooq.impl.DSL.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.wtbyt298.myaccountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.myaccountbook.application.query.model.summary.MonthlyBalanceDto;
import io.github.wtbyt298.myaccountbook.application.query.service.summary.BalanceSheetQueryService;
import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.myaccountbook.domain.model.user.UserId;
import io.github.wtbyt298.myaccountbook.domain.shared.types.SummaryType;

/**
 * 貸借対照表のデータを取得するクラス
 */
@Component
class BalanceSheetJooqQueryService implements BalanceSheetQueryService {
	
	@Autowired
	private DSLContext jooq;
		
	/**
	 * 勘定科目ごとの月次残高を取得する
	 */
	@Override
	public List<Entry<String, BigDecimal>> aggregateByAccountTitle(YearMonth yearMonth, UserId userId, AccountingType accountingType) {
		//勘定科目テーブルと月次残高テーブルをJOINする
		Result<Record2<String, BigDecimal>> result = jooq.select(ACCOUNTTITLES.ACCOUNTTITLE_NAME, sum(MONTHLY_BALANCES.BALANCE))
			.from(ACCOUNTTITLES)
			.leftOuterJoin(MONTHLY_BALANCES)
				.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.ACCOUNTTITLE_ID))
				.and(MONTHLY_BALANCES.USER_ID.eq(userId.value()))
				//BSの科目の場合、指定した年月より古いレコードを全て集計対象とする
				.and(MONTHLY_BALANCES.FISCAL_YEARMONTH.lessOrEqual(yearMonth.toString()))
			.where(ACCOUNTTITLES.SUMMARY_TYPE.eq(SummaryType.BS.toString()))
				.and(ACCOUNTTITLES.ACCOUNTING_TYPE.eq(accountingType.toString()))
			.groupBy(ACCOUNTTITLES.ACCOUNTTITLE_ID)
			.fetch();
	
		//取得結果をMapに詰め替える
		Map<String, BigDecimal> data = new HashMap<>();
		for (Record2<String, BigDecimal> each : result) {
			//集計結果のカラムはnullの場合あり
			String accountTitleName = each.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME);
			Optional<BigDecimal> balance = Optional.ofNullable(each.get(each.field2()));
			data.put(accountTitleName, balance.orElse(BigDecimal.ZERO));
		}
		
		return new ArrayList<>(data.entrySet());
	}
	
	/**
	 * 補助科目ごとの月次残高を取得する
	 */
	@Override
	public FinancialStatement aggregateIncludingSubAccountTitle(YearMonth yearMonth, UserId userId) {
		//勘定科目テーブルと補助科目テーブルと月次残高テーブルをJOINする
		List<MonthlyBalanceDto> data = jooq.select(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, ACCOUNTTITLES.ACCOUNTING_TYPE, sum(MONTHLY_BALANCES.BALANCE))
		   	.from(ACCOUNTTITLES)
		   	.leftOuterJoin(SUB_ACCOUNTTITLES)
		   		.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID))
				.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.value()))
			.leftOuterJoin(MONTHLY_BALANCES)
				.on(MONTHLY_BALANCES.USER_ID.eq(userId.value()))
				//BSの科目の場合、指定した年月より古いレコードを全て集計対象とする
				.and(MONTHLY_BALANCES.FISCAL_YEARMONTH.lessOrEqual(yearMonth.toString()))
				.and(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.ACCOUNTTITLE_ID))
				.and(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID)
					.or(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID.isNull()))
			.where(ACCOUNTTITLES.SUMMARY_TYPE.eq(SummaryType.BS.toString()))
			.groupBy(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)
			.fetch()
			.map(record -> mapRecordToDto(record));
		
		return new FinancialStatement(data);
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
