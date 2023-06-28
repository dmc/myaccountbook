package com.github.wtbyt298.myaccountbook.presentation.forms.budget;

import java.util.List;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * 予算設定画面のフォームクラス
 */
@Data
public class ConfigureBudgetForm {
	
	@Valid
	private List<ConfigureBudgetDetailForm> details;

	public ConfigureBudgetForm(List<ConfigureBudgetDetailForm> details) {
		this.details = details;
	}
	
}
