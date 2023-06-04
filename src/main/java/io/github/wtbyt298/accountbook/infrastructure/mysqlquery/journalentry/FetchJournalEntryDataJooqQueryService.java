package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.journalentry;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import static generated.tables.JournalEntries.*;
import static generated.tables.EntryDetails.*;
import static generated.tables.Accounttitles.*;
import static generated.tables.SubAccounttitles.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.EntryDetailDto;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.JournalEntryOrderKey;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.infrastructure.shared.exception.RecordNotFoundException;

/**
 * 仕訳の取得処理クラス
 * DBから取得した値を戻り値クラスに詰め替えて返す
 */
@Component
class FetchJournalEntryDataJooqQueryService implements FetchJournalEntryDataQueryService {
	
	@Autowired
	private DSLContext jooq;
	
	/**
	 * 単一の仕訳を取得する
	 */
	@Override
	public JournalEntryDto fetchOne(EntryId entryId) {
		Record record = jooq.select()
			.from(JOURNAL_ENTRIES)
			.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.value()))
			.fetchOne();
		
		if (record == null) {
			throw new RecordNotFoundException("該当するデータが見つかりませんでした。");
		}
		
		return mapRecordToDto(record, entryId.value());
	}
	
	/**
	 * 仕訳の一覧を取得する
	 */
	@Override
	public List<JournalEntryDto> fetchAll(YearMonth yearMonth, JournalEntryOrderKey orderKey, UserId userId) {
		//ORDER BY句で並べ替えるフィールドを取得
		SortField<?> orderColumn = buildOrderColumn(orderKey);
		
		Result<Record> result = jooq.select()
			.from(JOURNAL_ENTRIES)
			.where(JOURNAL_ENTRIES.FISCAL_YEARMONTH.eq(yearMonth.toString()))
				.and(JOURNAL_ENTRIES.USER_ID.eq(userId.toString()))
			.orderBy(orderColumn)
			.fetch();
		
		if (result.isEmpty()) {
			throw new RecordNotFoundException("該当するデータが見つかりませんでした。");
		}
		
		return result.stream()
			.map(record -> mapRecordToDto(record, record.get(JOURNAL_ENTRIES.ENTRY_ID)))
			.toList();
	}
	
	/**
	 * 仕訳IDに一致する仕訳明細を全て取得する
	 */
	private List<EntryDetailDto> fetchEntryDetailDtoById(String entryId) {
		Result<Record> result = jooq.select()
			.from(ENTRY_DETAILS)
			.join(JOURNAL_ENTRIES).on(ENTRY_DETAILS.ENTRY_ID.eq(JOURNAL_ENTRIES.ENTRY_ID))
			.leftOuterJoin(ACCOUNTTITLES)
				.on(ENTRY_DETAILS.ACCOUNTTITLE_ID.eq(ACCOUNTTITLES.ACCOUNTTITLE_ID))
			.leftOuterJoin(SUB_ACCOUNTTITLES)
				.on(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)
				.and(SUB_ACCOUNTTITLES.USER_ID.eq(JOURNAL_ENTRIES.USER_ID))
				.and(ENTRY_DETAILS.ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID)))
			.where(ENTRY_DETAILS.ENTRY_ID.eq(entryId))
			.fetch();
		if (result.isEmpty()) {
			throw new RecordNotFoundException("該当するデータが見つかりませんでした。");
		}
		return result.stream()
			.map(record -> mapRecordToDto(record))
			.toList();
	}
	
	/**
	 * レコードをDTOに詰め替える（仕訳DTO）
	 */
	private JournalEntryDto mapRecordToDto(Record record, String entryId) {
		return new JournalEntryDto(
			record.get(JOURNAL_ENTRIES.ENTRY_ID), 
			record.get(JOURNAL_ENTRIES.DEAL_DATE), 
			record.get(JOURNAL_ENTRIES.ENTRY_DESCRIPTION), 
			record.get(JOURNAL_ENTRIES.TOTAL_AMOUNT), 
			fetchEntryDetailDtoById(entryId)
		);
	}
	
	/**
	 * レコードをDTOに詰め替える（仕訳明細DTO）
	 */
	private EntryDetailDto mapRecordToDto(Record record) {
		return new EntryDetailDto(
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID),
			record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME),
			Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)).orElse("0"),
			Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME)).orElse(""),
			record.get(ENTRY_DETAILS.LOAN_TYPE),
			record.get(ENTRY_DETAILS.AMOUNT)
		);
	}
	
	/**
	 * ORDER BY句で並べ替えるためのフィールドを生成する
	 */
	private SortField<?> buildOrderColumn(JournalEntryOrderKey orderKey) {
		switch (orderKey) {
			case DEAL_DATE:
				//日付昇順
				return JOURNAL_ENTRIES.DEAL_DATE.asc();
			case TOTAL_AMOUNT:
				//金額降順
				return JOURNAL_ENTRIES.TOTAL_AMOUNT.desc();
			//ソート条件が追加された場合はここに書くこと
			default:
				return JOURNAL_ENTRIES.DEAL_DATE.asc();
		}
	}
	
}
