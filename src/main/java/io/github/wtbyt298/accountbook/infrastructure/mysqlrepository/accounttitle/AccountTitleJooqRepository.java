package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.accounttitle;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import generated.tables.records.AccounttitlesRecord;
import static generated.tables.Accounttitles.*;
import static generated.tables.SubAccounttitles.*;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType;

/**
 * 勘定科目のリポジトリクラス
 * 勘定科目集約の永続化と再構築の詳細を記述する
 */
@Repository
public class AccountTitleJooqRepository implements AccountTitleRepository {

	@Autowired
	private DSLContext jooq;
	
	@Override
	public void save(AccountTitle accountTitle) {
		
	}
	
	@Override
	public AccountTitle findById(AccountTitleId id) {
		Results<Record> results = jooq.selectFrom(ACCOUNTTITLES
										 .join(SUB_ACCOUNTTITLES).on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID))
										 .where(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(id.toString()))
										 
		if (result.size() == 0) {
			throw new RuntimeException("勘定科目が見つかりませんでした。");
		}
		return new AccountTitle(AccountTitleId.valueOf(result.getAccounttitleId()), 
			AccountTitleName.valueOf(result.getAccounttitleName()), 
			AccountingType.valueOf(result.getAccountingType())
		);
	}
	
}
