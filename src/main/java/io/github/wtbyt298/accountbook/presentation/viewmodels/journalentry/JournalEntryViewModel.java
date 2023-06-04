package io.github.wtbyt298.accountbook.presentation.viewmodels.journalentry;

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
public class JournalEntryViewModel {
	
	private final String entryId;
	private final String dealDate;
	private final String description;
	private final String totalAmount;
	private final List<EntryDetailViewModel> debitDetails = new ArrayList<>();
	private final List<EntryDetailViewModel> creditDetails = new ArrayList<>();
	
	public JournalEntryViewModel(JournalEntryDto dto) {
		this.entryId = dto.getEntryId();
		this.dealDate = dto.getDealDate().format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
		this.description = dto.getDescription();
		this.totalAmount = AmountPresentationFormatter.yen(dto.getTotalAmount());
		
		//仕訳明細DTOを1件ずつViewモデルに詰め替える
		for (EntryDetailDto detailDto : dto.getEntryDetails()) {
			if (detailDto.isDebit()) {
				this.debitDetails.add(new EntryDetailViewModel(detailDto));
			} 
			if (detailDto.isCredit()) {
				this.creditDetails.add(new EntryDetailViewModel(detailDto));
			}
		}
	}
	
}
