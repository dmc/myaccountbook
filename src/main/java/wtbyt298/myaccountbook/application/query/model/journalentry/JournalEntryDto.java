package wtbyt298.myaccountbook.application.query.model.journalentry;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

/**
 * DBから取得した仕訳データを保持するクラス
 */
@Getter
public class JournalEntryDto {
	
	private final String entryId;
	private final LocalDate dealDate;
	private final String description;
	private final int totalAmount;
	private final List<EntryDetailDto> entryDetails;
	
	public JournalEntryDto(String entryId, LocalDate dealDate, String description, int totalAmount, List<EntryDetailDto> entryDetails) {
		this.entryId = entryId;
		this.dealDate = dealDate;
		this.description = description;
		this.totalAmount = totalAmount;
		this.entryDetails = entryDetails;
	}
	
}
