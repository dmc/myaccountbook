package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.journalentry;

import static generated.tables.JournalEntries.*;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static generated.tables.EntryDetails.*;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;

/**
 * 仕訳のリポジトリクラス
 * 仕訳集約の永続化と再構築の詳細を記述する
 */
@Repository
public class JournalEntryJooqRepository implements JournalEntryRepository {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 仕訳を保存する
	 * 仕訳テーブルと仕訳明細テーブルに別々にINSERTする
	 */
	@Override
	public void save(JournalEntry entry) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/**
	 * 仕訳を削除する
	 */
	@Override
	public void drop(EntryId id) {
		
	}

}
