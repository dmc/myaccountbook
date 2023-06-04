package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.accounttitle;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.tables.Accounttitles.*;
import java.util.List;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.infrastructure.shared.exception.RecordNotFoundException;

/**
 * 勘定科目のリポジトリクラス
 * 勘定科目集約の永続化と再構築の詳細を記述する
 */
@Repository
class AccountTitleJooqRepository implements AccountTitleRepository {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * IDに一致する勘定科目を取得する
	 */
	@Override
	public AccountTitle findById(AccountTitleId id) {
		Record record = jooq.select()
			.from(ACCOUNTTITLES)
		    .where(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(id.value()))
		    .fetchOne();
		
		if (record == null) {
			throw new RecordNotFoundException("指定した勘定科目は存在しません。");
		}
		
		return mapRecordToEntity(record);
	}
	
	/**
	 * 全ての勘定科目を取得する
	 */
	@Override
	public List<AccountTitle> findAll() {
		return jooq.select()
			.from(ACCOUNTTITLES)
			.fetch()
			.map(record -> mapRecordToEntity(record));
	}
	
	/**
	 * レコードをエンティティに詰め替える
	 */
	private AccountTitle mapRecordToEntity(Record record) {
		return new AccountTitle(
			AccountTitleId.valueOf(record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID)), 
			AccountTitleName.valueOf(record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME)), 
			AccountingType.valueOf(record.get(ACCOUNTTITLES.ACCOUNTING_TYPE))
		);
	}
	
	/**
	 * IDに一致する勘定科目が存在するかどうかを判断する
	 */
	@Override
	public boolean exists(AccountTitleId id) {
		final int resultCount = jooq.select()
			.from(ACCOUNTTITLES)
		    .where(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(id.value()))
		    .execute();
		
		return resultCount > 0;
	}
	
}
