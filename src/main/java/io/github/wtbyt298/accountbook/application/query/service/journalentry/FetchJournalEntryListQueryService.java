package io.github.wtbyt298.accountbook.application.query.service.journalentry;

import java.time.YearMonth;
import java.util.List;

import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;

/**
 * 仕訳一覧取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FetchJournalEntryListQueryService {

	List<JournalEntryDto> findAll(YearMonth yearMonth, UserSession userSession);
	
}
