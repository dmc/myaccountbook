package io.github.wtbyt298.accountbook.domain.model.accounttitle;

/**
 * 勘定科目集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface AccountTitleRepository {
	
	/**
	 * 勘定科目を保存する
	 */
	void save(AccountTitle accountTitle);

	/**
	 * 勘定科目をIDで検索する
	 */
	AccountTitle findById(AccountTitleId id);
	
}
