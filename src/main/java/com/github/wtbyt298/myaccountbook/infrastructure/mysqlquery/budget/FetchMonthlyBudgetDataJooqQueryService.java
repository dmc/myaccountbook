package com.github.wtbyt298.myaccountbook.infrastructure.mysqlquery.budget;

import static generated.tables.Budgets.*;
import static generated.tables.Accounttitles.*;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.wtbyt298.myaccountbook.application.query.model.budget.MonthlyBudgetDto;
import com.github.wtbyt298.myaccountbook.application.query.service.budget.FetchMonthlyBudgetDataQueryService;
import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 月次予算データの取得処理クラス
 * DBから取得した値を戻り値クラスに詰め替えて返す
 */
@Component
class FetchMonthlyBudgetDataJooqQueryService implements FetchMonthlyBudgetDataQueryService {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 全ての勘定科目の予算データを取得する
	 */
	@Override
	public List<MonthlyBudgetDto> fetchAll(UserId userId) {
		return jooq.select()
			.from(ACCOUNTTITLES)
			.leftOuterJoin(BUDGETS)
				.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(BUDGETS.ACCOUNTTITLE_ID))
				.and(BUDGETS.USER_ID.eq(userId.value()))
			//費用の科目のみを取り出す
			.where(ACCOUNTTITLES.ACCOUNTING_TYPE.eq(AccountingType.EXPENSES.toString()))
			.fetch()
			.map(record -> mapRecordToDto(record));
	}
	
	/**
	 * レコードをDTOに詰め替える
	 */
	private MonthlyBudgetDto mapRecordToDto(Record record) {
		//予算額がnullの場合、デフォルト値として0を設定する
		return new MonthlyBudgetDto(
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID), 
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME), 
			Optional.ofNullable(record.get(BUDGETS.BUDGET_AMOUNT)).orElse(0)
		);
	}

}
