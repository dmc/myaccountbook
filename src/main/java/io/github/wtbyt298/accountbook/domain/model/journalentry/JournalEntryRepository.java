package io.github.wtbyt298.accountbook.domain.model.journalentry;

/**
 * 仕訳集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface JournalEntryRepository {
	
	/**
	 * 仕訳をDBに保存する
	 */
	void save(JournalEntry entry);
	
	/**
	 * 仕訳をDBから削除する
	 */
	void drop(EntryId id);
	
}
