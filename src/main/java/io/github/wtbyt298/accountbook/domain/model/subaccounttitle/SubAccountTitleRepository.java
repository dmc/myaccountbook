package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 補助科目集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface SubAccountTitleRepository {

	void save(SubAccountTitles subAccountTitles, UserId userId);
	
	SubAccountTitles findCollectionByParentId(AccountTitleId parentId, UserId userId);
	
	boolean exists(AccountTitleId parentId, SubAccountTitleId id, UserId userId);
	
}
