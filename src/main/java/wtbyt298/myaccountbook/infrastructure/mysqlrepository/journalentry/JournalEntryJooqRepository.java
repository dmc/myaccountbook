package wtbyt298.myaccountbook.infrastructure.mysqlrepository.journalentry;

import static generated.tables.JournalEntries.*;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.journalentry.Amount;
import wtbyt298.myaccountbook.domain.model.journalentry.DealDate;
import wtbyt298.myaccountbook.domain.model.journalentry.Description;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetails;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntryRepository;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import wtbyt298.myaccountbook.domain.model.user.UserId;
import wtbyt298.myaccountbook.domain.shared.types.LoanType;
import wtbyt298.myaccountbook.infrastructure.shared.exception.RecordNotFoundException;

import static generated.tables.EntryDetails.*;

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
	 */
	@Override
	public void save(JournalEntry entry, UserId userId) {
		//仕訳テーブルと仕訳明細テーブルに別々にINSERTする
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
		//1件の仕訳から仕訳明細を取得し、仕訳明細の数だけINSERTを実行する
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
		Record record = jooq.select()
			.from(JOURNAL_ENTRIES)
			.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.value()))
			.fetchOne();
		
		if (record == null) {
			throw new RecordNotFoundException("指定した仕訳は存在しません。");
		}
		
		//仕訳明細を生成する
		EntryDetails entryDetails = findEntryDetailsById(entryId);
		
		return JournalEntry.reconstruct(
			EntryId.fromString(record.get(JOURNAL_ENTRIES.ENTRY_ID)), 
			DealDate.valueOf(record.get(JOURNAL_ENTRIES.DEAL_DATE)), 
			Description.valueOf(record.get(JOURNAL_ENTRIES.ENTRY_DESCRIPTION)), 
			entryDetails
		);
	}
	
	/**
	 * IDに一致する仕訳明細を全て取得する
	 */
	private EntryDetails findEntryDetailsById(EntryId entryId) {
		List<EntryDetail> entities = jooq.select()
			.from(ENTRY_DETAILS)
			.where(ENTRY_DETAILS.ENTRY_ID.eq(entryId.value()))
			.fetch()
			.map(record -> mapRecordToEntity(record));
		
		return new EntryDetails(entities);
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
		//外部キーの関係で仕訳明細テーブルのレコードを先に削除する
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
