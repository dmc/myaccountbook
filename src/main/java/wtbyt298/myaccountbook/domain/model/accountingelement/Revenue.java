package wtbyt298.myaccountbook.domain.model.accountingelement;

import wtbyt298.myaccountbook.domain.shared.types.LoanType;
import wtbyt298.myaccountbook.domain.shared.types.SummaryType;

/**
 * 収益クラス
 */
class Revenue implements AccountingElement {

	@Override
	public String lavel() {
		return "収益";
	}

	@Override
	public LoanType loanType() {
		return LoanType.CREDIT;
	}

	@Override
	public SummaryType summaryType() {
		return SummaryType.PL;
	}

}
