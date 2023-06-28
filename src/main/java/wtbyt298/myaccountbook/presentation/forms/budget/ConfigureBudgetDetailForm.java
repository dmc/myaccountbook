package wtbyt298.myaccountbook.presentation.forms.budget;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 予算設定画面の1件分の入力フォームに対応するクラス
 */
@Data
public class ConfigureBudgetDetailForm {

	@NotBlank
	private String accountTitleId;
	
	@NotBlank
	private String accountTitleName;
	
	@NotNull(message = "予算額を入力してください。")
	@Min(value = 0, message = "予算は0円以上で入力してください。")
	private Integer budgetAmount;
	
	public ConfigureBudgetDetailForm() {
		
	}
	
	public ConfigureBudgetDetailForm(String accountTitleId, String accountTitleName, int budgetAmount) {
		this.accountTitleId = accountTitleId;
		this.accountTitleName = accountTitleName;
		this.budgetAmount = budgetAmount;
	}
	
}
