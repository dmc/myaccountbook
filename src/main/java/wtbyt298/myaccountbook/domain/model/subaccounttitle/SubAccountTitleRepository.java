package wtbyt298.myaccountbook.domain.model.subaccounttitle;

import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 補助科目集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface SubAccountTitleRepository {

	void save(SubAccountTitles subAccountTitles, UserId userId);
	
	SubAccountTitles findCollectionByParentId(AccountTitleId parentId, UserId userId);
	
	boolean exists(AccountTitleId parentId, SubAccountTitleId id, UserId userId);
	
}
