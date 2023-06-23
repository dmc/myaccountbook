package io.github.wtbyt298.myaccountbook.presentation.controller.summary;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.wtbyt298.myaccountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.myaccountbook.application.query.model.summary.MonthlyBalanceDto;
import io.github.wtbyt298.myaccountbook.application.query.service.summary.BalanceSheetQueryService;
import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.myaccountbook.presentation.viewmodels.summary.FinancialStatementViewModel;
import io.github.wtbyt298.myaccountbook.presentation.viewmodels.summary.SummaryOfBalanceSheetViewModel;

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
		//勘定科目ごとの合計金額を保持するビューモデルを取得する
		FinancialStatement bs = fetchProfitAndLossStatement(selectedYearMonth);
		List<FinancialStatementViewModel> viewModels = mapDtoToViewModel(bs);
		model.addAttribute("financialStatements", viewModels);
		
		//会計区分ごとの合計金額を保持するビューモデルを取得する
		SummaryOfBalanceSheetViewModel summaryViewModel = createSummary(bs);
		model.addAttribute("summary", summaryViewModel);
		
		return "/summary/bs";
	}
	
	/**
	 * 貸借対照表を取得する
	 */
	private FinancialStatement fetchProfitAndLossStatement(String selectedYearMonth) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		UserSession userSession = userSessionProvider.getUserSession();
		
		return balanceSheetQueryService.aggregateIncludingSubAccountTitle(yearMonth, userSession.userId());
	}
	
	/**
	 * 会計区分ごとの合計金額をビューモデルに詰め替える
	 */
	private SummaryOfBalanceSheetViewModel createSummary(FinancialStatement bs) {
		final int totalOfAssets = bs.calculateTotalAmount(AccountingType.ASSETS);
		final int totalOfLiabilities = bs.calculateTotalAmount(AccountingType.LIABILITIES);
		final int totalOfEquity = bs.calculateTotalAmount(AccountingType.EQUITY);
		
		return new SummaryOfBalanceSheetViewModel(totalOfAssets, totalOfLiabilities, totalOfEquity);
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
	
}
