package io.github.wtbyt298.accountbook.domain.model.account;

import java.time.YearMonth;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 勘定集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface AccountRepository {

	void save(Account account, UserId userId);
	
	Account find(AccountTitle accountTitle, SubAccountTitleId subId, UserId userId, YearMonth fiscalYearMonth);
	
}
