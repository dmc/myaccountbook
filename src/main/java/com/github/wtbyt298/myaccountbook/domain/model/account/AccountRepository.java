package com.github.wtbyt298.myaccountbook.domain.model.account;

import java.time.YearMonth;

import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 勘定集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface AccountRepository {

	void save(Account account, UserId userId);
	
	Account find(AccountTitle accountTitle, SubAccountTitleId subId, UserId userId, YearMonth fiscalYearMonth);
	
}
