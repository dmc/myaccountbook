package io.github.wtbyt298.myaccountbook.presentation.viewmodels.summary;

import java.util.ArrayList;
import java.util.List;

import io.github.wtbyt298.myaccountbook.application.query.model.summary.MonthlyBalanceDto;
import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.presentation.shared.util.AmountPresentationFormatter;
import lombok.Getter;

/**
 * 財務諸表の画面表示用クラス
 */
@Getter
public class FinancialStatementViewModel {

	private final String accountTitleName;
	private final AccountingType accountingType;
	private final List<MonthlyBalanceViewModel> breakDowns;
	
	public FinancialStatementViewModel(String accountTitleName, AccountingType accountingType, List<MonthlyBalanceDto> breakDowns) {
		this.accountTitleName = accountTitleName;
		this.accountingType = accountingType;
		this.breakDowns = new ArrayList<>();
		
		for (MonthlyBalanceDto each : breakDowns) {
			this.breakDowns.add(new MonthlyBalanceViewModel(each.getSubAccountTitleName(), each.getBalance()));
		}
	}
	
	public int total() {
		return breakDowns.stream()
			.mapToInt(each -> each.getBalance())
			.sum();
	}
	
	public boolean hasBreakDowns() {
		return breakDowns.size() > 1;
	}
	
	public String showTotal () {
		return AmountPresentationFormatter.yen(total());
	}
	
}
