package com.github.wtbyt298.myaccountbook.application.query.model.summary;

import java.util.List;
import java.util.stream.Collectors;

import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;

/**
 * 財務諸表を表すクラス
 */
public class FinancialStatement {

	private final List<MonthlyBalanceDto> breakDowns;
	
	public FinancialStatement(List<MonthlyBalanceDto> breakDowns) {
		this.breakDowns = breakDowns;
	}
	
	/**
	 * 会計区分ごとの合計金額を計算する
	 */
	public int calculateTotalAmount(AccountingType type) {
		return breakDowns.stream()
			.filter(each -> each.getAccountingType().equals(type))
			.mapToInt(each ->  each.getBalance())
			.sum();
	}	
	
	/**
	 * 勘定科目ごとの合計金額を計算する
	 */
	public int calculateTotalAmount(AccountTitleId accountTitleId) {
		return breakDowns.stream()
			.filter(each -> each.getAccountTitleId().equals(accountTitleId))
			.mapToInt(each ->  each.getBalance())
			.sum();
	}	
	
	/**
	 * 勘定科目ごとに絞り込んだリストを返す
	 */
	public List<MonthlyBalanceDto> filteredByAccountTitle(AccountTitleId id) {
		return breakDowns.stream()
			.filter(each -> each.getAccountTitleId().equals(id))
			.collect(Collectors.toUnmodifiableList());
	}
	
}
