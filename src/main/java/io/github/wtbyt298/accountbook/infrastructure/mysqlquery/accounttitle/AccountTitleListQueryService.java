package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.accounttitle;

import java.util.*;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static generated.Tables.*;
import generated.tables.records.*;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleIdAndNameDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.IAccountTitleIdAndNameQueryService;

/**
 * 勘定科目一覧取得処理クラス
 * DBから取得した値を戻り値クラスに詰め替えて返す
 */
@Service
public class AccountTitleListQueryService implements IAccountTitleIdAndNameQueryService {

	@Autowired
	private DSLContext jooq;

	public List<AccountTitleIdAndNameDto> findAll() {
		List<AccountTitleIdAndNameDto> list = new ArrayList<>();
		Result<AccounttitlesRecord> result = jooq.selectFrom(ACCOUNTTITLES).fetch();
		if (result.isEmpty()) {
			throw new RuntimeException();
		}
		for (AccounttitlesRecord record : result) {
			AccountTitleIdAndNameDto dto = new AccountTitleIdAndNameDto(record.getAccounttitleId(), record.getAccounttitleName());
			list.add(dto);
		}
		return list;
	}
	
}