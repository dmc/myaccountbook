package io.github.wtbyt298.myaccountbook.presentation.controller.user;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.wtbyt298.myaccountbook.application.query.service.summary.ProfitAndLossStatementQueryService;
import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.presentation.controller.summary.ChartDataProcessor;
import io.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.myaccountbook.presentation.shared.util.AmountPresentationFormatter;

/**
 * ホーム画面のコントローラクラス
 */
@Controller
public class HomeController {

	@Autowired
	private ProfitAndLossStatementQueryService profitAndLossStatementQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * ホーム画面を表示する
	 */
	@GetMapping("/user/home")
	public String load(Model model) {
		return showChart(YearMonth.now(), model);
	}
	
	/**
	 * ホーム画面を表示する（年月を指定してアクセスした場合）
	 */
	@GetMapping("/user/home/{selectedYearMonth}")
	public String load(@PathVariable @ModelAttribute String selectedYearMonth, Model model) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		return showChart(yearMonth, model);
	}
	
	/**
	 * 収支のグラフを表示する
	 */
	private String showChart(YearMonth yearMonth, Model model) {
		UserSession userSession = userSessionProvider.getUserSession();
		
		//費用の科目の集計値を取得する
		List<Entry<String, BigDecimal>> expensesData = profitAndLossStatementQueryService.aggregateByAccountTitle(yearMonth, userSession.userId(), AccountingType.EXPENSES);
		ChartDataProcessor expensesChart = new ChartDataProcessor(expensesData);
		model.addAttribute("expensesLabels", expensesChart.labels());
		model.addAttribute("expensesData", expensesChart.values());
		model.addAttribute("totalOfExpenses", AmountPresentationFormatter.yen(expensesChart.total()));
		
		//収益の科目の集計値を取得する
		List<Entry<String, BigDecimal>> revenueData = profitAndLossStatementQueryService.aggregateByAccountTitle(yearMonth, userSession.userId(), AccountingType.REVENUE);
		ChartDataProcessor revenueChart = new ChartDataProcessor(revenueData);
		model.addAttribute("revenueLavels", revenueChart.labels());
		model.addAttribute("revenueData", revenueChart.values());
		model.addAttribute("totalOfRevenue", AmountPresentationFormatter.yen(revenueChart.total()));

		model.addAttribute("selectedYearMonth", yearMonth.toString());
		
		return "/user/home";
	}
	
}
