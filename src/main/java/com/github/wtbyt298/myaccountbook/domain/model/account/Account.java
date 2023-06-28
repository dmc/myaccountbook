package com.github.wtbyt298.myaccountbook.domain.model.account;

import java.time.YearMonth;

import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.Amount;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;

/**
 * 勘定クラス
 */
public class Account {
	
	private final AccountTitle accountTitle;
	private final SubAccountTitleId subAccountTitleId;
	private final YearMonth fiscalYearMonth;
	private final int balance;
	
	public Account(AccountTitle accountTitle, SubAccountTitleId subAccountTitleId, YearMonth fiscalYearMonth, int balance) {
		this.accountTitle = accountTitle;
		this.subAccountTitleId = subAccountTitleId;
		this.fiscalYearMonth = fiscalYearMonth;
		this.balance = balance;
	}
	
	/**
	 * 残高を更新する
	 * @param difference 増加額または減少額
	 * @return 更新後の勘定
	 */
	public Account updateBalance(LoanType loanType, Amount difference) {
		//勘定科目の貸借区分と仕訳明細の貸借区分が一致していれば、残高を増加させる
		//そうでなければ、残高を減少させる
		LoanType accountTitleLoanType = accountTitle.accountingType().loanType();
		final int balanceAfterUpdated;
		if (loanType.equals(accountTitleLoanType)) {
			balanceAfterUpdated = balance + difference.value();
		} else {
			balanceAfterUpdated = balance - difference.value();
		}
		
		return new Account(accountTitle, subAccountTitleId, fiscalYearMonth, balanceAfterUpdated);
	}
	
	/**
	 * @return 残高
	 */
	public int balance() {
		return balance;
	}
	
	/**
	 * @return 勘定科目ID
	 */
	public AccountTitleId accountTitleId() {
		return accountTitle.id();
	}
	
	/**
	 * @return 補助科目ID
	 */
	public SubAccountTitleId subAccountTitleId() {
		return subAccountTitleId;
	}
	
	/**
	 * @return 会計年月
	 */
	public YearMonth fiscalYearMonth() {
		return fiscalYearMonth;
	}
	
	@Override
	public String toString() {
		return accountTitle.name().toString() + subAccountTitleId.toString() + fiscalYearMonth.toString();
	}
	
}
