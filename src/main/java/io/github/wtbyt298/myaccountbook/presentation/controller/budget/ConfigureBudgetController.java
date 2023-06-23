package io.github.wtbyt298.myaccountbook.presentation.controller.budget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.wtbyt298.myaccountbook.application.query.model.budget.MonthlyBudgetDto;
import io.github.wtbyt298.myaccountbook.application.query.service.budget.FetchMonthlyBudgetDataQueryService;
import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.application.usecase.budget.ConfigureBudgetUseCase;
import io.github.wtbyt298.myaccountbook.presentation.forms.budget.ConfigureBudgetDetailForm;
import io.github.wtbyt298.myaccountbook.presentation.forms.budget.ConfigureBudgetForm;
import io.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import jakarta.validation.Valid;

/**
 * 予算設定処理のコントローラクラス
 */
@Controller
public class ConfigureBudgetController {
	
	@Autowired
	private ConfigureBudgetUseCase configureBudgetUseCase;

	@Autowired
	private FetchMonthlyBudgetDataQueryService fetchMonthlyBudgetDataQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 予算設定画面を表示する
	 */
	@GetMapping("/settings/budget")
	public String load(Model model) {
		UserSession userSession = userSessionProvider.getUserSession();
		List<MonthlyBudgetDto> data = fetchMonthlyBudgetDataQueryService.fetchAll(userSession.userId());
		
		ConfigureBudgetForm form = mapDtoToForm(data);
		model.addAttribute("budgetForm", form);
		
		return "/settings/budget";
	}
	
	/**
	 * 予算を設定する
	 */
	@PostMapping("/budget/configure")
	public String configureBudget(@Valid @ModelAttribute("budgetForm") ConfigureBudgetForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "/settings/budget";
		}
		
		//フォームのデータをユースケースに渡すためのMapに詰め替える
		Map<String, Integer> configureBudgetCommand = new HashMap<>();
		for (ConfigureBudgetDetailForm each : form.getDetails()) {
			configureBudgetCommand.put(each.getAccountTitleId(), each.getBudgetAmount());
		}
		
		UserSession userSession = userSessionProvider.getUserSession();
		configureBudgetUseCase.execute(configureBudgetCommand, userSession);
		
		return "redirect:/settings/budget";
	}
	
	/**
	 * DTOをフォームクラスに詰め替える（画面全体に対応するフォーム）
	 */
	private ConfigureBudgetForm mapDtoToForm(List<MonthlyBudgetDto> data) {
		List<ConfigureBudgetDetailForm> details = data.stream()
			.map(each -> mapDtoToForm(each))
			.toList();
		
		return new ConfigureBudgetForm(details);
	}
	
	/**
	 * DTOをフォームクラスに詰め替える（個別の入力フォーム）
	 */
	private ConfigureBudgetDetailForm mapDtoToForm(MonthlyBudgetDto dto) {	
		return new ConfigureBudgetDetailForm(
			dto.getAccountTitleId(), 
			dto.getAccountTitleName(), 
			dto.getBudgetAmount()
		);
	}
	
}
