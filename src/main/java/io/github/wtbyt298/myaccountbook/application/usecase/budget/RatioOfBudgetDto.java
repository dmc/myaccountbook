package io.github.wtbyt298.myaccountbook.application.usecase.budget;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * 予算比データを保持するクラス
 */
@Getter
public class RatioOfBudgetDto {

	private final String accountTitleId;
	private final int budgetAmount;
	private final BigDecimal ratio;
	
	public RatioOfBudgetDto(String accountTitleId, int budgetAmount, BigDecimal ratio) {
		this.accountTitleId = accountTitleId;
		this.budgetAmount = budgetAmount;
		this.ratio = ratio;
	}
	
}
