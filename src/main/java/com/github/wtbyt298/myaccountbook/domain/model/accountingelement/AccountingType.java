package com.github.wtbyt298.myaccountbook.domain.model.accountingelement;

import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import com.github.wtbyt298.myaccountbook.domain.shared.types.SummaryType;

/**
 * 会計区分
 */
public enum AccountingType {

	ASSETS(new Assets()),            //資産
	LIABILITIES(new Liabilities()), //負債
	EQUITY(new Equity()),			 //純資産
	EXPENSES(new Expenses()),       //費用
	REVENUE(new Revenue());         //収益
	
	private final AccountingElement element;
	
	private AccountingType(AccountingElement element) {
		this.element = element;
	}
	
	/**
	 * 会計区分名
	 */
	public String lavel() {
		return element.lavel();
	}
	
	/**
	 * 貸借区分
	 */
	public LoanType loanType() {
		return element.loanType();
	}
	
	/**
	 * 集計区分
	 */
	public SummaryType summaryType() {
		return element.summaryType();
	}
	
	/**
	 * 自身を借方、引数を貸方として組み合わせ可能かどうかを判断する
	 * @param other 貸方の会計区分
	 */
	public boolean canCombineWith(AccountingType other) {
		return AllowedCombinationRule.ok(this, other);
	}
	
}
