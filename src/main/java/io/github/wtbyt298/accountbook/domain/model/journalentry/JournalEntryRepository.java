package io.github.wtbyt298.accountbook.domain.model.journalentry;

/**
 * 仕訳集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface JournalEntryRepository {

	void save(JournalEntry entry);
	
	void drop(EntryId id);

	boolean exists(EntryId entryId);
	
}
