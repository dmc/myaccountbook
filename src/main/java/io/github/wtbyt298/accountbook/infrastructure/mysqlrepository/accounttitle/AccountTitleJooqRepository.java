package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.accounttitle;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.tables.Accounttitles.*;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;

/**
 * 勘定科目のリポジトリクラス
 * 勘定科目集約の永続化と再構築の詳細を記述する
 */
@Repository
public class AccountTitleJooqRepository implements AccountTitleRepository {

	@Autowired
	private DSLContext jooq;
	
	@Override
	public AccountTitle findById(AccountTitleId id) {
		Record result = jooq.select()
							.from(ACCOUNTTITLES)
						    .where(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(id.toString()))
						    .fetchOne();	 
		return new AccountTitle(
			AccountTitleId.valueOf(result.get(ACCOUNTTITLES.ACCOUNTTITLE_ID)), 
			AccountTitleName.valueOf(result.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME)), 
			AccountingType.valueOf(result.get(ACCOUNTTITLES.ACCOUNTING_TYPE))
		);
	}
	
}
