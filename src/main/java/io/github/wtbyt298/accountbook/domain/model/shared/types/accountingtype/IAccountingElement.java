package io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype;

import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.model.shared.types.SummaryType;

/**
 * 簿記の5要素を表すインターフェース
 */
interface IAccountingElement {
	
	String lavel();            //要素名（資産/負債/純資産/費用/収益）
	LoanType loanType();       //貸借区分（借方/貸方）
	SummaryType summaryType(); //集計区分（貸借/損益）
	
}
