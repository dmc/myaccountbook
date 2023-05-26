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
import io.github.wtbyt298.accountbook.application.query.service.summary.BalanceSheetQueryService;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.accountbook.presentation.shared.util.AmountPresentationFormatter;
import io.github.wtbyt298.accountbook.presentation.viewmodels.summary.FinancialStatementViewModel;

/**
 * 貸借対照表取得処理のコントローラクラス
 */
@Controller
public class ShowBalanceSheetController {

	@Autowired
	private BalanceSheetQueryService balanceSheetQueryService; 
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 月別資産状況一覧画面を表示する
	 */
	@GetMapping("/summary/bs/{selectedYearMonth}")
	public String load(@PathVariable String selectedYearMonth, Model model) {
		FinancialStatement bs = generateProfitAndLossStatement(selectedYearMonth);
		List<FinancialStatementViewModel> viewModels = mapDtoToViewModel(bs);
		model.addAttribute("financialStatements", viewModels);
		
		final int totalOfAssets = bs.calculateTotalAmount(AccountingType.ASSETS);
		final int totalOfLiabilities = bs.calculateTotalAmount(AccountingType.LIABILITIES);
		final int totalOfEquity = bs.calculateTotalAmount(AccountingType.EQUITY);
		final int surplus = totalOfAssets - (totalOfLiabilities + totalOfEquity); //剰余金
		final int actualtotalOfEquity = totalOfEquity + surplus;
		model.addAttribute("totalOfAssets", totalOfAssets);
		model.addAttribute("totalOfAssetsFormatted", AmountPresentationFormatter.yen(totalOfAssets));
		model.addAttribute("totalOfLiabilities", totalOfLiabilities);
		model.addAttribute("totalOfLiabilitiesFormatted", AmountPresentationFormatter.yen(totalOfLiabilities));
		model.addAttribute("totalOfEquity", actualtotalOfEquity);
		model.addAttribute("totalOfEquityFormatted", AmountPresentationFormatter.yen(actualtotalOfEquity));
		model.addAttribute("surplus", surplus);
		model.addAttribute("surplusFormatted", AmountPresentationFormatter.yen(surplus));
		
		return "/summary/bs";
	}
	
	/**
	 * 貸借対照表を取得する
	 */
	private FinancialStatement generateProfitAndLossStatement(String selectedYearMonth) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		UserSession userSession = userSessionProvider.getUserSession();
		return balanceSheetQueryService.fetch(yearMonth, userSession.userId(), SummaryType.BS);
	}
	
	/**
	 * 貸借対照表のデータをビューモデルに詰め替える
	 */
	private List<FinancialStatementViewModel> mapDtoToViewModel(FinancialStatement bs) {
		List<FinancialStatementViewModel> viewModels = new ArrayList<>();
		List<AccountTitle> accountTitles = accountTitleRepository.findAll();
		for (AccountTitle each : accountTitles) {
			FinancialStatementViewModel veiwModel = new FinancialStatementViewModel(
				each.name().value(), 
				each.accountingType(), 
				bs.filteredByAccountTitle(each.id())
			);
			viewModels.add(veiwModel);
		}
		return viewModels;
	}
	
}
