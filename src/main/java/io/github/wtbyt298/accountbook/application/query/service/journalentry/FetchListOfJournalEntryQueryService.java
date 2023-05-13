package io.github.wtbyt298.accountbook.application.query.service.journalentry;

import java.time.YearMonth;
import java.util.List;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 仕訳一覧取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FetchListOfJournalEntryQueryService {

	List<JournalEntryDto> findAll(YearMonth yearMonth, JournalEntryOrderKey orderKey, UserId userId);
	
}
