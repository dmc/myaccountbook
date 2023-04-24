package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static generated.Tables.*;
import generated.tables.records.*;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.AccountTitleAndSubAccountTitleListQueryService;

/**
 * 勘定科目と補助科目の一覧取得処理クラス
 * DBから取得した値を戻り値クラスに詰め替えて返す
 */
@Service
public class AccountTitleAndSubAccountTitleListJooqQueryService implements AccountTitleAndSubAccountTitleListQueryService {

	@Autowired
	private DSLContext jooq;

	public List<AccountTitleAndSubAccountTitleDto> findAll() {
		List<AccountTitleAndSubAccountTitleDto> list = new ArrayList<>();
		Result<AccounttitlesRecord> result = jooq.selectFrom(ACCOUNTTITLES).fetch();
		if (result.isEmpty()) {
			throw new RuntimeException();
		}
		for (AccounttitlesRecord record : result) {
			AccountTitleAndSubAccountTitleDto dto = mapRecordToDto();
			list.add(dto);
		}
		return list;
	}
	
	private AccountTitleAndSubAccountTitleDto mapRecordToDto() {
		return new AccountTitleAndSubAccountTitleDto(
			null, 
			null, 
			null, 
			null
		);
	}
	
}