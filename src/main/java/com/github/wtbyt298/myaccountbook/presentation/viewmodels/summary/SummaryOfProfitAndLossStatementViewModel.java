package com.github.wtbyt298.myaccountbook.presentation.viewmodels.summary;

import com.github.wtbyt298.myaccountbook.presentation.shared.util.AmountPresentationFormatter;

import lombok.Getter;

/**
 * 損益計算書の各合計値を保持するクラス
 */
@Getter
public class SummaryOfProfitAndLossStatementViewModel {

	private final int totalOfExpenses;
	private final int totalOfRevenue;
	
	public SummaryOfProfitAndLossStatementViewModel(int totalOfExpenses, int totalOfRevenue) {
		this.totalOfExpenses = totalOfExpenses;
		this.totalOfRevenue = totalOfRevenue;
	}
	
	public int netIncome() {
		return totalOfRevenue - totalOfExpenses;
	}
	
	public String totalOfExpensesFormatted() {
		return AmountPresentationFormatter.yen(totalOfExpenses);
	}
	
	public String totalOfRevenueFormatted() {
		return AmountPresentationFormatter.yen(totalOfRevenue);
	}
	
	public String netIncomeFormatted() {
		return AmountPresentationFormatter.yen(netIncome());
	}
	
}
