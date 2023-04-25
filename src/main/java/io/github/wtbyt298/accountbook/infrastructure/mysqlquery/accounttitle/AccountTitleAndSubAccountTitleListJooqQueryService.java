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

/**
 * 勘定科目と補助科目の一覧取得処理クラス
 * DBから取得した値を戻り値クラスに詰め替えて返す
 */
@Component
public class AccountTitleAndSubAccountTitleListJooqQueryService implements AccountTitleAndSubAccountTitleListQueryService {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 勘定科目テーブルと補助科目テーブルをJOINしてIDと科目名を取り出す
	 */
	@Override
	public List<AccountTitleAndSubAccountTitleDto> findAll() {
		List<AccountTitleAndSubAccountTitleDto> list = new ArrayList<>();
		Result<Record> result = jooq.selectFrom(ACCOUNTTITLES
									.leftOuterJoin(SUB_ACCOUNTTITLES)
									.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID))
									.where(SUB_ACCOUNTTITLES.USER_ID.eq("TEST_USER"))) //TODO ログイン機能実装後に修正する
									.fetch();
		if (result.isEmpty()) {
			throw new RuntimeException("勘定科目と補助科目データの取得ができませんでした。");
		}
		for (Record record : result) {
			AccountTitleAndSubAccountTitleDto dto = mapRecordToDto(record);
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 取得したレコードを戻り値のDTOクラスに詰め替える
	 */
	private AccountTitleAndSubAccountTitleDto mapRecordToDto(Record record) {
		String subAccountTitleId = record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID);
		//補助科目を持っていない場合は、補助科目IDは"0"、補助科目名は""（空白）とする
		if (subAccountTitleId == null) {
			return new AccountTitleAndSubAccountTitleDto(
				record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID), 
				record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME), 
				"0",
				""
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