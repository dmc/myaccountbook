package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.journalentry;

import static generated.tables.JournalEntries.*;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.tables.EntryDetails.*;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.journalentry.DealDate;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Description;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetails;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * 仕訳のリポジトリクラス
 * 仕訳集約の永続化と再構築の詳細を記述する
 */
@Repository
class JournalEntryJooqRepository implements JournalEntryRepository {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 仕訳を保存する
	 * 仕訳テーブルと仕訳明細テーブルに別々にINSERTする
	 */
	@Override
	public void save(JournalEntry entry, UserId userId) {
		insertRecord(entry, userId);
		insertRecordOfDetails(entry);
	}
	
	/**
	 * 仕訳テーブルにデータを挿入する
	 */
	private void insertRecord(JournalEntry entry, UserId userId) {
		jooq.insertInto(JOURNAL_ENTRIES)
			.set(JOURNAL_ENTRIES.ENTRY_ID, entry.id().value())
			.set(JOURNAL_ENTRIES.DEAL_DATE, entry.dealDate().value())
			.set(JOURNAL_ENTRIES.ENTRY_DESCRIPTION, entry.description().value())
			.set(JOURNAL_ENTRIES.FISCAL_YEARMONTH, entry.fiscalYearMonth().toString())
			.set(JOURNAL_ENTRIES.TOTAL_AMOUNT, entry.totalAmount().value())
			.set(JOURNAL_ENTRIES.USER_ID, userId.value())
			.execute();
	}
	
	/**
	 * 仕訳明細テーブルにデータを挿入する
	 */
	private void insertRecordOfDetails(JournalEntry entry) {
		List<EntryDetail> entryDetails = entry.entryDetails();
		for (EntryDetail each : entryDetails) {
			jooq.insertInto(ENTRY_DETAILS)
				.set(ENTRY_DETAILS.ENTRY_ID, entry.id().value())
				.set(ENTRY_DETAILS.ACCOUNTTITLE_ID, each.accountTitleId().value())
				.set(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID, each.subAccountTitleId().value())
				.set(ENTRY_DETAILS.LOAN_TYPE, each.detailLoanType().toString())
				.set(ENTRY_DETAILS.AMOUNT, each.amount().value())
				.execute();
		}
	}
	
	/**
	 * IDに一致する仕訳を取得する
	 */
	public JournalEntry findById(EntryId entryId) {
		Record result = jooq.select()
			.from(JOURNAL_ENTRIES)
			.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.value()))
			.fetchOne();
		EntryDetails entryDetails = findEntryDetailsById(entryId);
		return JournalEntry.reconstruct(
			EntryId.fromString(result.get(JOURNAL_ENTRIES.ENTRY_ID)), 
			DealDate.valueOf(result.get(JOURNAL_ENTRIES.DEAL_DATE)), 
			Description.valueOf(result.get(JOURNAL_ENTRIES.ENTRY_DESCRIPTION)), 
			entryDetails
		);
	}
	
	/**
	 * IDに一致する仕訳明細を全て取得する
	 */
	private EntryDetails findEntryDetailsById(EntryId entryId) {
		List<EntryDetail> elements = jooq.select()
			.from(ENTRY_DETAILS)
			.where(ENTRY_DETAILS.ENTRY_ID.eq(entryId.value()))
			.fetch()
			.map(record -> mapRecordToEntity(record));
		return new EntryDetails(elements);
	}
	
	/**
	 * レコードをエンティティに詰め替える
	 */
	private EntryDetail mapRecordToEntity(Record record) {
		return new EntryDetail(
			AccountTitleId.valueOf(record.get(ENTRY_DETAILS.ACCOUNTTITLE_ID)), 
			SubAccountTitleId.valueOf(record.get(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID)), 
			LoanType.valueOf(record.get(ENTRY_DETAILS.LOAN_TYPE)), 
			Amount.valueOf(record.get(ENTRY_DETAILS.AMOUNT))
		);
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
		final int resultCount = jooq.select()
			.from(JOURNAL_ENTRIES)
			.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.value()))
			.execute();
		if (resultCount == 0) return false;
		return true;
	}

}
