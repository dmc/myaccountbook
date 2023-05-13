package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static generated.Tables.*;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.AccountTitleAndSubAccountTitleListQueryService;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 勘定科目と補助科目の一覧取得処理クラス
 * DBから取得した値をDTOに詰め替えて返す
 */
@Component
class AccountTitleAndSubAccountTitleListJooqQueryService implements AccountTitleAndSubAccountTitleListQueryService {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 勘定科目テーブルと補助科目テーブルをJOINしてIDと科目名を取り出す
	 */
	@Override
	public List<AccountTitleAndSubAccountTitleDto> findAll(UserId userId) {
		List<AccountTitleAndSubAccountTitleDto> list = new ArrayList<>();
		Result<Record> result = jooq.select()
									.from(ACCOUNTTITLES)
									.leftOuterJoin(SUB_ACCOUNTTITLES).on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID)
									.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.toString())))
									.orderBy(ACCOUNTTITLES.ACCOUNTTITLE_ID, SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)
									.fetch();
		if (result.isEmpty()) {
			throw new RuntimeException("勘定科目と補助科目のデータの取得ができませんでした。");
		}
		for (Record record : result) {
			list.add(mapRecordToDto(record));
		}
		return list;
	}
	
	/**
	 * 取得したレコードを戻り値のDTOクラスに詰め替える
	 */
	private AccountTitleAndSubAccountTitleDto mapRecordToDto(Record record) {
		if (record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID) == null ||
			record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME) == null	) {
			return new AccountTitleAndSubAccountTitleDto(
				record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID), 
				record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME),
				"0", //補助科目が存在しない場合は
				""   //補助科目ID："0"　補助科目名：""（空白）とする
			);
		}
 		return new AccountTitleAndSubAccountTitleDto(
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID), 
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME), 
			record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID), 
			record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME) 
		);
	}
	
}