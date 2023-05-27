package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static generated.Tables.*;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.infrastructure.shared.exception.RecordNotFoundException;

/**
 * 勘定科目と補助科目の一覧取得処理クラス
 * DBから取得した値をDTOに詰め替えて返す
 */
@Component
class FetchListOfAccountTitleAndSubAccountTitleJooqQueryService implements FetchListOfAccountTitleAndSubAccountTitleQueryService {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 勘定科目テーブルと補助科目テーブルをJOINしてIDと科目名を取り出す
	 */
	@Override
	public List<AccountTitleAndSubAccountTitleDto> fetchAll(UserId userId) {
		List<AccountTitleAndSubAccountTitleDto> data = jooq.select()
			.from(ACCOUNTTITLES)
			.leftOuterJoin(SUB_ACCOUNTTITLES)
				.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID)
				.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.toString())))
			.orderBy(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)
			.fetch()
			.map(record -> mapRecordToDto(record));
		if (data.isEmpty()) {
			throw new RecordNotFoundException("勘定科目と補助科目のデータを取得できませんでした。");
		}
		return data;
	}
	
	/**
	 * レコードをDTOに詰め替える
	 */
	private AccountTitleAndSubAccountTitleDto mapRecordToDto(Record record) {
 		return new AccountTitleAndSubAccountTitleDto(
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID), 
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME),
			Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)).orElse("0"),
			Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME)).orElse("")
		);
	}
	
}