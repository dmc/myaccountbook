package io.github.wtbyt298.accountbook.application.query.service.journalentry;

import java.time.YearMonth;
import java.util.List;

import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;

/**
 * 仕訳一覧取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FetchJournalEntryListQueryService {

	List<JournalEntryDto> findAll(YearMonth yearMonth);
	
}
