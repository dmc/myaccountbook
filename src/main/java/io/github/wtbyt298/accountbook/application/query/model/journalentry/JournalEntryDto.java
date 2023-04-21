package io.github.wtbyt298.accountbook.application.query.model.journalentry;

import lombok.Getter;

/**
 * DBから取得した仕訳データを保持するクラス
 */
@Getter
public class JournalEntryDto {

	private final String id;
	private final String dealDate;
	private final String description;
	private final int totalAmount;
	
	public JournalEntryDto() {
		
	}
	
}
