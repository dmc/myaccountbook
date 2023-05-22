package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.journalentry;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

/**
 * 仕訳の取得処理クラス
 * DBから取得した値を戻り値クラスに詰め替えて返す
 */
@Component
class FetchJournalEntryDataJooqQueryService implements FetchJournalEntryDataQueryService {
	
	//TODO コードが読みにくくなっているのでリファクタリングする
	
	@Autowired
	private DSLContext jooq;
	
	/**
	 * 単一の仕訳を取得する
	 */
	@Override
	public JournalEntryDto fetchOne(EntryId entryId) {
		Record result = jooq.select()
							.from(JOURNAL_ENTRIES)
							.where(JOURNAL_ENTRIES.ENTRY_ID.eq(entryId.value()))
							.fetchOne();
		if (result == null) {
			throw new RuntimeException("該当するデータが見つかりませんでした。");
		}
		return new JournalEntryDto(
			result.get(JOURNAL_ENTRIES.ENTRY_ID), 
			result.get(JOURNAL_ENTRIES.DEAL_DATE), 
			result.get(JOURNAL_ENTRIES.ENTRY_DESCRIPTION), 
			result.get(JOURNAL_ENTRIES.TOTAL_AMOUNT), 
			findEntryDetailById(entryId.value())
		);
	}

	/**
	 * 仕訳の一覧を取得する
	 * 年月を絞り込みの条件とする
	 */
	@Override
	public List<JournalEntryDto> fetchAll(YearMonth yearMonth, JournalEntryOrderKey orderKey, UserId userId) {
		//WHERE句で絞り込むための文字列（yyyyMM形式の年月）
		String yyyyMm = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		//ORDER BY句で並べ替えるフィールドを取得
		SortField<?> orderColumn = buildOrderColumn(orderKey);
		Result<Record> result = jooq.select()
									.from(JOURNAL_ENTRIES)
									.where(JOURNAL_ENTRIES.FISCAL_YEARMONTH.eq(yyyyMm))
									.and(JOURNAL_ENTRIES.USER_ID.eq(userId.toString()))
									.orderBy(orderColumn)
									.fetch();
		if (result.isEmpty()) {
			throw new RuntimeException("該当するデータが見つかりませんでした。");
		}
		List<JournalEntryDto> data = new ArrayList<>();
		for (Record record : result) {
			JournalEntryDto dto = new JournalEntryDto(
				record.get(JOURNAL_ENTRIES.ENTRY_ID), 
				record.get(JOURNAL_ENTRIES.DEAL_DATE), 
				record.get(JOURNAL_ENTRIES.ENTRY_DESCRIPTION), 
				record.get(JOURNAL_ENTRIES.TOTAL_AMOUNT), 
				findEntryDetailById(record.get(JOURNAL_ENTRIES.ENTRY_ID))
			);
			data.add(dto);
		}
		return data;
	}
	
	/**
	 * 仕訳IDに一致する仕訳明細を全て取得する
	 */
	private List<EntryDetailDto> findEntryDetailById(String entryId) {
		List<EntryDetailDto> resultList = new ArrayList<>();
		Result<Record> result = jooq.select()
									.from(ENTRY_DETAILS)
									.leftOuterJoin(ACCOUNTTITLES).on(ENTRY_DETAILS.ACCOUNTTITLE_ID.eq(ACCOUNTTITLES.ACCOUNTTITLE_ID))
									.leftOuterJoin(SUB_ACCOUNTTITLES).on(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID.eq(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID))
									.where(ENTRY_DETAILS.ENTRY_ID.eq(entryId))
									.fetch();
		if (result.isEmpty()) {
			throw new RuntimeException("該当する仕訳明細が見つかりませんでした。");
		}
		for (Record record : result) {
			Optional<String> subAccountTitleId = Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID));
			Optional<String> subAccountTitleName = Optional.ofNullable(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME)); 
			EntryDetailDto dto = new EntryDetailDto(
				record.get(ACCOUNTTITLES.ACCOUNTTITLE_ID),
				record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME),
				subAccountTitleId.orElse("0"), //補助科目IDがnullの場合は"0"を返す
				subAccountTitleName.orElse(""), //補助科目名がnullの場合は空文字列を返す
				record.get(ENTRY_DETAILS.LOAN_TYPE),
				record.get(ENTRY_DETAILS.AMOUNT)
			);
			resultList.add(dto);
		}
		return resultList;
	}
	
	/**
	 * ORDER BY句で並べ替えるためのフィールドを生成する
	 */
	private SortField<?> buildOrderColumn(JournalEntryOrderKey orderKey) {
		switch (orderKey) {
			case DEAL_DATE:
				return JOURNAL_ENTRIES.DEAL_DATE.asc();
			case TOTAL_AMOUNT:
				return JOURNAL_ENTRIES.TOTAL_AMOUNT.desc();
			//ソート条件が追加された場合はここに書くこと
			default:
				return JOURNAL_ENTRIES.DEAL_DATE.asc();
		}
	}
	
}
