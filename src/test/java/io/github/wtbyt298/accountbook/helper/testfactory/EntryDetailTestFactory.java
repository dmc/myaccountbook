package io.github.wtbyt298.accountbook.helper.testfactory;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
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
			AccountTitleId.valueOf("101"), 
			SubAccountTitleId.valueOf("0"), 
			loanType, 
			Amount.valueOf(100)
		);
	}
	
	/**
	 * 貸借区分と金額を指定して仕訳明細インスタンスを生成する
	 */
	public static EntryDetail create(LoanType loanType, int amount) {
		return new EntryDetail(
				AccountTitleId.valueOf("101"), 
				SubAccountTitleId.valueOf("0"), 
				loanType, 
				Amount.valueOf(amount)
		);
	}
	
	/**
	 * 全ての引数を指定して仕訳明細インスタンスを生成する
	 */
	public static EntryDetail create(String accountTitleId, String subAccountTitleId, LoanType loanType, int amount) {
		return new EntryDetail(
			AccountTitleId.valueOf(accountTitleId), 
			SubAccountTitleId.valueOf(subAccountTitleId), 
			loanType, 
			Amount.valueOf(amount)
		);
	}
	
}
