package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
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
	public List<AccountTitleAndSubAccountTitleDto> findAll(UserId userId) {
		Result<Record> result = jooq.select()
									.from(ACCOUNTTITLES)
									.leftOuterJoin(SUB_ACCOUNTTITLES)
										.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID)
										.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.toString())))
									.orderBy(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)
									.fetch();
		if (result.isEmpty()) {
			throw new RecordNotFoundException("勘定科目と補助科目のデータの取得ができませんでした。");
		}
		List<AccountTitleAndSubAccountTitleDto> data = new ArrayList<>();
		for (Record each : result) {
			data.add(mapRecordToDto(each));
		}
		return data;
	}
	
	/**
	 * 取得したレコードを戻り値のDTOクラスに詰め替える
	 */
	private AccountTitleAndSubAccountTitleDto mapRecordToDto(Record record) {
		Optional<String> subAccountTitleId = Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID));
		Optional<String> subAccountTitleName = Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME));
 		return new AccountTitleAndSubAccountTitleDto(
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID), 
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME),
 			subAccountTitleId.orElse("0"), //補助科目が存在しない場合、補助科目IDは"0"
 			subAccountTitleName.orElse("") //補助科目が存在しない場合、補助科目名は空白
		);
	}
	
}