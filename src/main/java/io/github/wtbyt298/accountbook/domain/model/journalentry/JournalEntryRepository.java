package io.github.wtbyt298.accountbook.domain.model.journalentry;

import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 仕訳集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface JournalEntryRepository {

	void save(JournalEntry entry, UserId userId);
	
	void drop(EntryId id, UserId userId);

	boolean exists(EntryId entryId, UserId userId);
	
}
