package io.github.wtbyt298.accountbook.application.query.service.journalentry;

import java.util.List;

import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;

/**
 * 仕訳一覧取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface IJournalEntryListQueryService {

	List<JournalEntryDto> findAll(); //TODO 引数に年月を指定する
	
}
