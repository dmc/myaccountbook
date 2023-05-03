package io.github.wtbyt298.accountbook.helper.testfactory;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * テスト用の仕訳明細インスタンスを生成するクラス
 */
public class EntryDetailTestFactory {

	/**
	 * 貸借区分を指定して仕訳明細インスタンスを生成する
	 */
	public static EntryDetail create(LoanType loanType) {
		return new EntryDetail(
			AccountTitleTestFactory.create("102", "普通預金", AccountingType.ASSETS), 
			SubAccountTitleTestFactory.create("0", "その他"), 
			loanType, 
			Amount.valueOf(100)
		);
	}
	
	/**
	 * 貸借区分と金額を指定して仕訳明細インスタンスを生成する
	 */
	public static EntryDetail create(LoanType loanType, int amount) {
		return new EntryDetail(
			AccountTitleTestFactory.create("102", "普通預金", AccountingType.ASSETS), 
			SubAccountTitleTestFactory.create("0", "その他"), 
			loanType, 
			Amount.valueOf(amount)
		);
	}
	
	/**
	 * 会計区分と貸借区分と金額を指定して仕訳明細インスタンスを生成する
	 */
	public static EntryDetail create(AccountingType accountingType, LoanType loanType, int amount) {
		return new EntryDetail(
			AccountTitleTestFactory.create("000", "TEST", accountingType), 
			SubAccountTitleTestFactory.create("0", "その他"), 
			loanType, 
			Amount.valueOf(amount)
		);
	}
	
}
