package io.github.wtbyt298.accountbook.presentation.viewmodels.budget;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import io.github.wtbyt298.accountbook.application.usecase.budget.RatioOfBudgetDto;
import io.github.wtbyt298.accountbook.presentation.shared.util.AmountPresentationFormatter;
import lombok.Getter;

/**
 * 予算比の画面表示用クラス
 */
@Getter
public class RatioOfBudgetViewModel {

	private final int budgetAmount;
	private final BigDecimal ratio;
	
	public RatioOfBudgetViewModel(RatioOfBudgetDto dto) {
		this.budgetAmount = dto.getBudgetAmount();
		this.ratio = dto.getRatio();
	}
	
	public String getBudgetAmount() {
		return AmountPresentationFormatter.yen(budgetAmount);
	}
	
	/**
	 * %表記に変換する
	 */
	public String getRatio() {
		BigDecimal coefficient = BigDecimal.valueOf(100);
		BigDecimal corrected = ratio.multiply(coefficient);
		
		return new DecimalFormat("0.00").format(corrected.doubleValue()) + "%";
	}
	
}
