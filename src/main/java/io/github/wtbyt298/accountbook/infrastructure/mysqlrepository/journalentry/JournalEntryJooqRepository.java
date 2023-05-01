package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.journalentry;

import static generated.tables.JournalEntries.*;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.tables.EntryDetails.*;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

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
	public void save(JournalEntry entry, UserId userId) {
		insertIntoJournalEntries(entry, userId);
		insertIntoEntryDetails(entry);
	}
	
	/**
	 * 仕訳テーブルにデータを挿入する
	 */
	private void insertIntoJournalEntries(JournalEntry entry, UserId userId) {
		jooq.insertInto(JOURNAL_ENTRIES)
			.set(JOURNAL_ENTRIES.ENTRY_ID, entry.id().value())
			.set(JOURNAL_ENTRIES.DEAL_DATE, entry.dealDate().value())
			.set(JOURNAL_ENTRIES.ENTRY_DESCRIPTION, entry.description().value())
			.set(JOURNAL_ENTRIES.FISCAL_YEARMONTH, entry.fiscalYearMonth())
			.set(JOURNAL_ENTRIES.TOTAL_AMOUNT, entry.totalAmount().value())
			.set(JOURNAL_ENTRIES.USER_ID, userId.value())
			.execute();
	}
	
	/**
	 * 仕訳明細テーブルにデータを挿入する
	 */
	private void insertIntoEntryDetails(JournalEntry entry) {
		List<EntryDetail> entryDetails = entry.entryDetails();
		for (EntryDetail detail : entryDetails) {
			jooq.insertInto(ENTRY_DETAILS)
				.set(ENTRY_DETAILS.ENTRY_ID, entry.id().value())
				.set(ENTRY_DETAILS.ACCOUNTTITLE_ID, detail.accountTitleId().value())
				.set(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID, detail.subAccountTitleId().value())
				.set(ENTRY_DETAILS.LOAN_TYPE, detail.detailLoanType().toString())
				.set(ENTRY_DETAILS.AMOUNT, detail.amount().value())
				.execute();
		}
	}

	/**
	 * 仕訳を削除する
	 */
	@Override
	public void drop(EntryId entryId) {
		jooq.deleteFrom(ENTRY_DETAILS)
			.where(ENTRY_DETAILS.ENTRY_ID.eq(entryId.value()))
			.execute();
		jooq.deleteFrom(JOURNAL_ENTRIES)
			.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.value()))
			.execute();
	}
	
	/**
	 * IDで指定した仕訳が存在するかどうかを判断する
	 */
	@Override
	public boolean exists(EntryId entryId) {
		final int resultCount = jooq.selectFrom(JOURNAL_ENTRIES)
									.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.value()))
									.execute();
		if (resultCount == 0) return false;
		return true;
	}

}
