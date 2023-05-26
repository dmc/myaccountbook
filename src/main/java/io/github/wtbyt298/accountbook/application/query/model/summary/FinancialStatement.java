package io.github.wtbyt298.accountbook.application.query.model.summary;

import java.util.List;
import java.util.stream.Collectors;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;

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
		int total = 0;
		for (MonthlyBalanceDto each : breakDowns) {
			if (each.getAccountingType().equals(type)) {
				total += each.getBalance();
			}
		}
		return total;
	}
	
	/**
	 * 勘定科目ごとに絞り込んだリストを返す
	 */
	public List<MonthlyBalanceDto> filteredByAccountTitle(AccountTitleId id) {
		List<MonthlyBalanceDto> filterd = breakDowns.stream()
													.filter(each -> each.getAccountTitleId().equals(id))
													.collect(Collectors.toUnmodifiableList());
		return filterd;
	}
	
}
