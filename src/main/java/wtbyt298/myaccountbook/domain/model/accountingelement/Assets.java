package wtbyt298.myaccountbook.domain.model.accountingelement;

import wtbyt298.myaccountbook.domain.shared.types.LoanType;
import wtbyt298.myaccountbook.domain.shared.types.SummaryType;

/**
 * 資産クラス
 */
class Assets implements AccountingElement {

	@Override
	public String lavel() {
		return "資産";
	}

	@Override
	public LoanType loanType() {
		return LoanType.DEBIT;
	}

	@Override
	public SummaryType summaryType() {
		return SummaryType.BS;
	}

}
