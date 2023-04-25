package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.journalentry;

import static generated.tables.JournalEntries.*;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.tables.EntryDetails.*;
import io.github.wtbyt298.accountbook.domain.model.journalentry.DetailRow;
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
		insertIntoJournalEntries(entry);
		insertIntoEntryDetails(entry);
	}
	
	/**
	 * 仕訳テーブルにデータを挿入する
	 */
	private void insertIntoJournalEntries(JournalEntry entry) {
		jooq.insertInto(JOURNAL_ENTRIES)
			.set(JOURNAL_ENTRIES.ENTRY_ID, entry.id())
			.set(JOURNAL_ENTRIES.DEAL_DATE, entry.dealDate())
			.set(JOURNAL_ENTRIES.ENTRY_DESCRIPTION, entry.description())
			.set(JOURNAL_ENTRIES.FISCAL_YEARMONTH, entry.fiscalYearMonth())
			.set(JOURNAL_ENTRIES.TOTAL_AMOUNT, entry.totalAmount().value())
			.set(JOURNAL_ENTRIES.USER_ID, "TEST_USER") //TODO ユーザ認証機能実装後に修正する
			.execute();
	}
	
	/**
	 * 仕訳明細テーブルにデータを挿入する
	 */
	private void insertIntoEntryDetails(JournalEntry entry) {
		List<DetailRow> detailRows = entry.entryDetail();
		for (DetailRow row : detailRows) {
			jooq.insertInto(ENTRY_DETAILS)
				.set(ENTRY_DETAILS.ENTRY_ID, entry.id())
				.set(ENTRY_DETAILS.ACCOUNTTITLE_ID, row.accountTitleId())
				.set(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID, row.subAccountTitleId())
				.set(ENTRY_DETAILS.LOAN_TYPE, row.detailLoanType())
				.set(ENTRY_DETAILS.AMOUNT, row.amount())
				.execute();
		}
	}

	/**
	 * 仕訳を削除する
	 */
	@Override
	public void drop(EntryId entryId) {
		jooq.deleteFrom(ENTRY_DETAILS)
			.where(ENTRY_DETAILS.ENTRY_ID.eq(entryId.toString()))
			.execute();
		jooq.deleteFrom(JOURNAL_ENTRIES)
			.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.toString()))
			.execute();
	}
	
	/**
	 * IDで指定した仕訳が存在するかどうかを判断する
	 */
	@Override
	public boolean exists(EntryId entryId) {
		final int resultCount = jooq.selectFrom(JOURNAL_ENTRIES)
									.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.toString()))
									.execute();
		if (resultCount == 0) return false;
		return true;
	}

}
