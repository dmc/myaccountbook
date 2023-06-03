package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.summary;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Result;
import static org.jooq.impl.DSL.*;
import static generated.tables.MonthlyBalances.*;
import static generated.tables.Accounttitles.*;
import static generated.tables.SubAccounttitles.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	 * 勘定科目ごとの月次残高を取得する
	 */
	@Override
	public List<Entry<String, BigDecimal>> aggregateByAccountTitle(YearMonth yearMonth, UserId userId, AccountingType accountingType) {
		Result<Record2<String, BigDecimal>> result = jooq.select(ACCOUNTTITLES.ACCOUNTTITLE_NAME, sum(MONTHLY_BALANCES.BALANCE))
			.from(ACCOUNTTITLES)
			.leftOuterJoin(MONTHLY_BALANCES)
				.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(MONTHLY_BALANCES.ACCOUNTTITLE_ID))
				.and(MONTHLY_BALANCES.USER_ID.eq(userId.value()))
				.and(MONTHLY_BALANCES.FISCAL_YEARMONTH.eq(yearMonth.toString()))
			.where(ACCOUNTTITLES.SUMMARY_TYPE.eq(SummaryType.PL.toString()))
				.and(ACCOUNTTITLES.ACCOUNTING_TYPE.eq(accountingType.toString()))
				.and(MONTHLY_BALANCES.BALANCE.gt(0))
			.groupBy(ACCOUNTTITLES.ACCOUNTTITLE_ID)
			.fetch();
		Map<String, BigDecimal> data = new HashMap<>();
		for (Record2<String, BigDecimal> each : result) {
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
		List<MonthlyBalanceDto> data = jooq.select(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, ACCOUNTTITLES.ACCOUNTING_TYPE, MONTHLY_BALANCES.BALANCE)
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
			.where(ACCOUNTTITLES.SUMMARY_TYPE.eq(SummaryType.PL.toString()))
			.fetch()
			.map(record -> mapRecordToDto(record));
		return new FinancialStatement(data);
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
