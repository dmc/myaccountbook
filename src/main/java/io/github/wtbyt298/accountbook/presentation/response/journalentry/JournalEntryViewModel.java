package io.github.wtbyt298.accountbook.presentation.response;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.github.wtbyt298.accountbook.application.query.model.journalentry.EntryDetailDto;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.presentation.shared.util.AmountPresentationFormatter;
import lombok.Getter;

/**
 * 仕訳の画面表示用クラス
 */
@Getter
public class JournalEntryView {
	
	private final String entryId;
	private final String dealDate;
	private final String description;
	private final String totalAmount;
	private final List<EntryDetailView> debitDetails = new ArrayList<>();
	private final List<EntryDetailView> creditDetails = new ArrayList<>();
	
	public JournalEntryView(JournalEntryDto dto) {
		this.entryId = dto.getEntryId();
		this.dealDate = dto.getDealDate().format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
		this.description = dto.getDescription();
		this.totalAmount = AmountPresentationFormatter.yen(dto.getTotalAmount());
		//仕訳明細DTOを1件ずつViewモデルに詰め替える
		for (EntryDetailDto detailDto : dto.getEntryDetails()) {
			if (detailDto.isDebit()) {
				this.debitDetails.add(new EntryDetailView(detailDto));
			} else {
				this.creditDetails.add(new EntryDetailView(detailDto));
			}
		}
	}
	
}
