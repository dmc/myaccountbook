package io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype;

import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.model.shared.types.SummaryType;

/**
 * 費用クラス
 */
class Expenses implements IAccountingElement {

	@Override
	public String lavel() {
		return "費用";
	}

	@Override
	public LoanType loanType() {
		return LoanType.DEBIT;
	}

	@Override
	public SummaryType summaryType() {
		return SummaryType.PL;
	}

}
