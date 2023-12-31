package wtbyt298.myaccountbook.domain.model.journalentry;

import wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 仕訳集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface JournalEntryRepository {

	void save(JournalEntry entry, UserId userId);
	
	JournalEntry findById(EntryId entryId);
	
	void drop(EntryId id);

	boolean exists(EntryId entryId);
	
}
