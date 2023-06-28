package wtbyt298.myaccountbook.presentation.viewmodels.budget;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import lombok.Getter;
import wtbyt298.myaccountbook.application.usecase.budget.RatioOfBudgetDto;
import wtbyt298.myaccountbook.presentation.shared.util.AmountPresentationFormatter;

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
