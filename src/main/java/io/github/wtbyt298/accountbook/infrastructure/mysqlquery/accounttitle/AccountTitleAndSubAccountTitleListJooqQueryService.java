package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static generated.Tables.*;
import static org.jooq.impl.DSL.*;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.AccountTitleAndSubAccountTitleListQueryService;

/**
 * 勘定科目と補助科目の一覧取得処理クラス
 * DBから取得した値をDTOに詰め替えて返す
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
		Result<Record> result = jooq.select()
									.from(ACCOUNTTITLES
									.leftOuterJoin(SUB_ACCOUNTTITLES).on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID))
									.where(SUB_ACCOUNTTITLES.USER_ID.eq("TEST_USER"))) //TODO ログイン機能実装後に修正する
									.fetch();
		if (result.isEmpty()) {
			throw new RuntimeException("勘定科目と補助科目データの取得ができませんでした。");
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
		return new AccountTitleAndSubAccountTitleDto(
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID), 
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME), 
			record.get(coalesce(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID, "0")), //補助科目が存在しない場合、IDは"0"とする
			record.get(coalesce(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, "")) //補助科目が存在しない場合、補助科目名は空文字列とする
		);
	}
	
}