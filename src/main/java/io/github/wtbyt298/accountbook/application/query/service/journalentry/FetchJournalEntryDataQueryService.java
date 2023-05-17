package io.github.wtbyt298.accountbook.application.query.service.journalentry;

import java.time.YearMonth;
import java.util.List;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 仕訳データ取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FetchJournalEntryDataQueryService {
	
	JournalEntryDto fetchOne(EntryId entryId);

	List<JournalEntryDto> fetchAll(YearMonth yearMonth, JournalEntryOrderKey orderKey, UserId userId);
	
}
