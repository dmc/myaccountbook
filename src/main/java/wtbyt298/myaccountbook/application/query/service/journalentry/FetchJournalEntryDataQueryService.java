package wtbyt298.myaccountbook.application.query.service.journalentry;

import java.time.YearMonth;
import java.util.List;

import wtbyt298.myaccountbook.application.query.model.journalentry.JournalEntryDto;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 仕訳データ取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FetchJournalEntryDataQueryService {
	
	JournalEntryDto fetchOne(EntryId entryId);

	List<JournalEntryDto> fetchAll(YearMonth yearMonth, JournalEntryOrderKey orderKey, UserId userId);
	
}
