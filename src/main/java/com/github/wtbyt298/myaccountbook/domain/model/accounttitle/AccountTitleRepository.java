package com.github.wtbyt298.myaccountbook.domain.model.accounttitle;

import java.util.List;

/**
 * 勘定科目集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface AccountTitleRepository {

	AccountTitle findById(AccountTitleId id);
	
	List<AccountTitle> findAll();
	
	boolean exists(AccountTitleId id);
	
}
