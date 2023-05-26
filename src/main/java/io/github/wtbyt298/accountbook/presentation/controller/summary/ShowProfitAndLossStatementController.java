package io.github.wtbyt298.accountbook.presentation.controller.summary;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.application.query.service.summary.ProfitAndLossStatementQueryService;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.accountbook.presentation.shared.util.AmountPresentationFormatter;
import io.github.wtbyt298.accountbook.presentation.viewmodels.summary.FinancialStatementViewModel;

/**
 * 損益計算書取得処理のコントローラクラス
 */
@Controller
public class ShowProfitAndLossStatementController {

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
		FinancialStatement pl = generateProfitAndLossStatement(selectedYearMonth);
		List<FinancialStatementViewModel> viewModels = mapDtoToViewModel(pl);
		model.addAttribute("financialStatements", viewModels);

		final int totalOfExpenses = pl.calculateTotalAmount(AccountingType.EXPENSES);
		final int totalOfRevenue = pl.calculateTotalAmount(AccountingType.REVENUE);
		final int netIncome = totalOfRevenue - totalOfExpenses; //収益と費用の差額をとって純利益とする
		model.addAttribute("totalOfExpenses", totalOfExpenses);
		model.addAttribute("totalOfExpensesFormatted", AmountPresentationFormatter.yen(totalOfExpenses));
		model.addAttribute("totalOfRevenue", totalOfRevenue);
		model.addAttribute("totalOfRevenueFormatted", AmountPresentationFormatter.yen(totalOfRevenue));
		model.addAttribute("netIncome", netIncome);
		model.addAttribute("netIncomeFormatted", AmountPresentationFormatter.yen(netIncome));
		
		return "/summary/pl";
	}
	
	/**
	 * 損益計算書を取得する
	 */
	private FinancialStatement generateProfitAndLossStatement(String selectedYearMonth) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		UserSession userSession = userSessionProvider.getUserSession();
		return profitAndLossStatementQueryService.fetch(yearMonth, userSession.userId(), SummaryType.PL);
	}
	
	/**
	 * 損益計算書のデータをビューモデルに詰め替える
	 */
	private List<FinancialStatementViewModel> mapDtoToViewModel(FinancialStatement pl) {
		List<FinancialStatementViewModel> viewModels = new ArrayList<>();
		List<AccountTitle> accountTitles = accountTitleRepository.findAll();
		for (AccountTitle each : accountTitles) {
			FinancialStatementViewModel veiwModel = new FinancialStatementViewModel(
				each.name().value(), 
				each.accountingType(), 
				pl.filteredByAccountTitle(each.id())
			);
			viewModels.add(veiwModel);
		}
		return viewModels;
	}
	
}
