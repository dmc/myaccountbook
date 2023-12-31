package wtbyt298.myaccountbook.domain.model.budget;

import java.util.Map;

import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 予算集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface MonthlyBudgetRepository {

	void save(MonthlyBudget monthlyBudget, AccountTitleId accountTitleId, UserId userId);
	
	Map<AccountTitleId, MonthlyBudget> findAll(UserId userId);
	
	boolean exists(AccountTitleId accountTitleId, UserId userId);
	
}
