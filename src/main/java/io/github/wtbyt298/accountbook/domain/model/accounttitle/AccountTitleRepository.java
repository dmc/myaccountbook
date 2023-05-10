package io.github.wtbyt298.accountbook.domain.model.accounttitle;

/**
 * 勘定科目集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface AccountTitleRepository {

	AccountTitle findById(AccountTitleId id);
	
	boolean exists(AccountTitleId id);
	
}
