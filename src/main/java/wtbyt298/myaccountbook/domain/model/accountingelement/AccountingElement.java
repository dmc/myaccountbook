package wtbyt298.myaccountbook.domain.model.accountingelement;

import wtbyt298.myaccountbook.domain.shared.types.LoanType;
import wtbyt298.myaccountbook.domain.shared.types.SummaryType;

/**
 * 簿記の5要素を表すインターフェース
 */
interface AccountingElement {
	
	String lavel();            //要素名（資産/負債/純資産/費用/収益）
	LoanType loanType();       //貸借区分（借方/貸方）
	SummaryType summaryType(); //集計区分（貸借/損益）
	
}
