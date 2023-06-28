package com.github.wtbyt298.myaccountbook.presentation.controller.summary;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.wtbyt298.myaccountbook.application.query.model.summary.FinancialStatement;
import com.github.wtbyt298.myaccountbook.application.query.model.summary.MonthlyBalanceDto;
import com.github.wtbyt298.myaccountbook.application.query.service.summary.ProfitAndLossStatementQueryService;
import com.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import com.github.wtbyt298.myaccountbook.application.usecase.budget.CalculateRatioOfBudgetUseCase;
import com.github.wtbyt298.myaccountbook.application.usecase.budget.RatioOfBudgetDto;
import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import com.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import com.github.wtbyt298.myaccountbook.presentation.viewmodels.budget.RatioOfBudgetViewModel;
import com.github.wtbyt298.myaccountbook.presentation.viewmodels.summary.FinancialStatementViewModel;
import com.github.wtbyt298.myaccountbook.presentation.viewmodels.summary.SummaryOfProfitAndLossStatementViewModel;

/**
 * 損益計算書取得処理のコントローラクラス
 */
@Controller
public class ShowProfitAndLossStatementController {
	
	@Autowired
	private CalculateRatioOfBudgetUseCase calculateRatioOfBudgetUseCase;

	@Autowired
	private ProfitAndLossStatementQueryService profitAndLossStatementQueryService; 
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 月別収支一覧画面を表示する
	 */
	@GetMapping("/summary/pl/{selectedYearMonth}")
	public String load(@PathVariable String selectedYearMonth, Model model) {
		UserSession userSession = userSessionProvider.getUserSession();
		
		//勘定科目ごとの合計金額を保持するビューモデルを取得する
		FinancialStatement pl = fetchProfitAndLossStatement(selectedYearMonth, userSession);
		List<FinancialStatementViewModel> viewModels = mapDtoToViewModel(pl);
		model.addAttribute("expenses", filterByAccountingType(viewModels, AccountingType.EXPENSES));
		model.addAttribute("revenues", filterByAccountingType(viewModels, AccountingType.REVENUE));
		
		//会計区分ごとの合計金額を保持するビューモデルを取得する
		SummaryOfProfitAndLossStatementViewModel summaryViewModel = createSummary(pl);
		model.addAttribute("summary", summaryViewModel);
		
		//予算と予算比を保持するビューモデルを取得する
		List<RatioOfBudgetDto> data = calculateRatioOfBudgetUseCase.execute(pl, userSession);
		List<RatioOfBudgetViewModel> budgetViewModels = data.stream()
			.map(RatioOfBudgetViewModel::new)
			.toList();
		model.addAttribute("budgets", budgetViewModels);
		
		return "/summary/pl";
	}
	
	/**
	 * 損益計算書を取得する
	 */
	private FinancialStatement fetchProfitAndLossStatement(String selectedYearMonth, UserSession userSession) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		return profitAndLossStatementQueryService.aggregateIncludingSubAccountTitle(yearMonth, userSession.userId());
	}
	
	/**
	 * 会計区分ごとの合計金額をビューモデルに詰め替える
	 */
	private SummaryOfProfitAndLossStatementViewModel createSummary(FinancialStatement pl) {
		final int totalOfExpenses = pl.calculateTotalAmount(AccountingType.EXPENSES);
		final int totalOfRevenue = pl.calculateTotalAmount(AccountingType.REVENUE);
		
		return new SummaryOfProfitAndLossStatementViewModel(totalOfExpenses, totalOfRevenue);
	}
	
	/**
	 * DTOをビューモデルに詰め替える
	 */
	private List<FinancialStatementViewModel> mapDtoToViewModel(FinancialStatement fs) {
		//勘定科目の一覧を取得し、勘定科目ごとにビューモデルを作成する
		List<AccountTitle> entities = accountTitleRepository.findAll();
		return entities.stream()
			.map(each -> mapEntityAndDtoToViewModel(each, fs.filteredByAccountTitle(each.id())))
			.toList();
	}
	
	/**
	 * エンティティとDTOをビューモデルに詰め替える
	 */
	private FinancialStatementViewModel mapEntityAndDtoToViewModel(AccountTitle entity, List<MonthlyBalanceDto> dto) {
		return new FinancialStatementViewModel(
			entity.name().value(), 
			entity.accountingType(), 
			dto
		);
	}
	
	/**
	 * ビューモデルを会計区分で絞り込んだリストを返す
	 */
	private List<FinancialStatementViewModel> filterByAccountingType(List<FinancialStatementViewModel> viewModels, AccountingType type) {
		return viewModels.stream()
			.filter(each -> each.getAccountingType().equals(type))
			.toList();
	}
	
}
