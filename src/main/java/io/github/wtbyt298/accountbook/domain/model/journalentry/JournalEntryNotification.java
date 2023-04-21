package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.time.LocalDate;

/**
 * 仕訳の永続化用の通知オブジェクトのインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface JournalEntryNotification {

	void entryId(String entryId);
	void dealDate(LocalDate dealDate);
	void description(String description);
	void fiscalYearMonth(String fiscalYearMonth);
	
}
