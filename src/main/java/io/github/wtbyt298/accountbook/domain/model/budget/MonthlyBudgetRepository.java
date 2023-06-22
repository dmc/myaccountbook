package io.github.wtbyt298.accountbook.domain.model.budget;

import java.util.Map;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 予算集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface MonthlyBudgetRepository {

	void save(MonthlyBudget monthlyBudget, AccountTitleId accountTitleId, UserId userId);
	
	Map<AccountTitleId, MonthlyBudget> findAll(UserId userId);
	
	boolean exists(AccountTitleId accountTitleId, UserId userId);
	
}
