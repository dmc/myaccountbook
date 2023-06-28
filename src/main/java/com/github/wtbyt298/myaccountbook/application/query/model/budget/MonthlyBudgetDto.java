package com.github.wtbyt298.myaccountbook.application.query.model.budget;

import lombok.Getter;

/**
 * DBから取得した月次予算のデータを保持するクラス
 */
@Getter
public class MonthlyBudgetDto {

	private final String accountTitleId;
	private final String accountTitleName;
	private final int budgetAmount;
	
	public MonthlyBudgetDto(String accountTitleId, String accountTitleName, int budgetAmount) {
		this.accountTitleId = accountTitleId;
		this.accountTitleName = accountTitleName;
		this.budgetAmount = budgetAmount;
	}
	
}
