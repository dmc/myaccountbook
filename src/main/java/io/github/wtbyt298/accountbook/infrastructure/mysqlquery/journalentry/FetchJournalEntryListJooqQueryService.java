package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.journalentry;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryListQueryService;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.JournalEntryOrderKey;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 仕訳の一覧取得処理クラス
 * DBから取得した値を戻り値クラスに詰め替えて返す
 */
@Component
public class FetchJournalEntryListJooqQueryService implements FetchJournalEntryListQueryService {
	
	//TODO コードが読みにくくなっているのでリファクタリングする
	
	@Autowired
	private DSLContext jooq;

	/**
	 * 仕訳の一覧を取得する
	 * 年月を絞り込みの条件とする
	 */
	@Override
	public List<JournalEntryDto> findAll(YearMonth yearMonth, JournalEntryOrderKey orderKey, UserId userId) {
		List<JournalEntryDto> resultList = new ArrayList<>();
		//WHERE句で絞り込むための文字列（yyyyMM形式の年月）
		String yyyyMm = yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
		//ORDER BY句で並べ替えるフィールドを取得
		SortField<?> orderColumn = buildOrderColumn(orderKey);
		Result<Record> result = jooq.select()
									.from(JOURNAL_ENTRIES)
									.where(JOURNAL_ENTRIES.FISCAL_YEARMONTH.eq(yyyyMm))
									.and(JOURNAL_ENTRIES.USER_ID.eq(userId.toString()))
									.orderBy(orderColumn)
									.fetch();
		if (result.isEmpty()) {
			throw new RuntimeException("該当する仕訳が見つかりませんでした。");
		}
		for (Record record : result) {
			JournalEntryDto dto = new JournalEntryDto(
				record.get(JOURNAL_ENTRIES.ENTRY_ID), 
				record.get(JOURNAL_ENTRIES.DEAL_DATE), 
				record.get(JOURNAL_ENTRIES.ENTRY_DESCRIPTION), 
				record.get(JOURNAL_ENTRIES.TOTAL_AMOUNT), 
				findEntryDetailById(record.get(JOURNAL_ENTRIES.ENTRY_ID))
			);
			resultList.add(dto);
		}
		return resultList;
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
			String subAccountTitleName;
			//補助科目名がnullの場合は空文字列を返す
			if (record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME) == null) {
				subAccountTitleName = "";
			}
			subAccountTitleName = record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME);
			EntryDetailDto dto = new EntryDetailDto(
				record.get(ACCOUNTTITLES.ACCOUNTTITLE_NAME),
				subAccountTitleName,
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
				return JOURNAL_ENTRIES.TOTAL_AMOUNT.asc();
			//ソート条件が追加された場合はここに書く
		}
		return JOURNAL_ENTRIES.DEAL_DATE.asc();
	}
	
}
