package io.github.wtbyt298.accountbook.domain.model.account;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;

/**
 * 勘定クラス
 */
public class Account {
	
	private final AccountTitle accountTitle;
	private final SubAccountTitleId subAccountTitleId;
	private final YearMonth fiscalYearMonth;
	private final List<AccountingTransaction> transactionHistory;

	public Account(AccountTitle accountTitle, SubAccountTitleId subAccountTitleId, YearMonth fiscalYearMonth, List<AccountingTransaction> transactionHistory) {
		this.accountTitle = accountTitle;
		this.subAccountTitleId = subAccountTitleId;
		this.fiscalYearMonth = fiscalYearMonth;
		this.transactionHistory = transactionHistory;
	}
	
	/**
	 * 会計取引を追加する
	 * @return 追加後の勘定
	 */
	public Account addTransaction(AccountingTransaction newTransaction) {
		List<AccountingTransaction> adding = new ArrayList<>(transactionHistory);
		adding.add(newTransaction);
		return new Account(accountTitle, subAccountTitleId, fiscalYearMonth, adding);
	}
	
	/**
	 * 残高を計算する
	 */
	public int balance() {
		//借方勘定の場合、借方合計から貸方合計を引く
		//貸方勘定の場合、貸方合計から借方合計を引く
		//結果は負の数となることもある
		if (accountTitle.isDebit()) {
			return debitSum().value() - creditSum().value();
		}
		else {
			return creditSum().value() - debitSum().value();
		}
	}
	
	/**
	 * 借方合計を計算する
	 */
	private Amount debitSum() {
		Amount total = Amount.valueOf(0);
		for (AccountingTransaction each : transactionHistory) {
			if (each.isCredit()) continue;
			total = total.plus(each.amount);
		}
		return total;
	}
	
	/**
	 * 貸方合計を計算する
	 */
	private Amount creditSum() {
		Amount total = Amount.valueOf(0);
		for (AccountingTransaction each : transactionHistory) {
			if (each.isDebit()) continue;
			total = total.plus(each.amount);
		}
		return total;
	}
	
	@Override
	public String toString() {
		return accountTitle.name().toString() + subAccountTitleId.toString() + fiscalYearMonth.toString();
	}
	
}
