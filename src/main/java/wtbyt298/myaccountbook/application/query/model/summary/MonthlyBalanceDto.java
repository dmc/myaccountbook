package wtbyt298.myaccountbook.application.query.model.summary;

import lombok.Getter;
import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;

/**
 * 月次残高を保持するクラス
 */
@Getter
public class MonthlyBalanceDto {
	
	private final AccountTitleId accountTitleId;
	private final String subAccountTitleName;
	private final AccountingType accountingType;
	private final int balance;
	
	public MonthlyBalanceDto(AccountTitleId accountTitleId, String subAccountTitleName, AccountingType accountingType, int balance) {
		this.accountTitleId = accountTitleId;
		this.subAccountTitleName = subAccountTitleName;
		this.accountingType =accountingType;
		this.balance = balance;
	}
	
}
