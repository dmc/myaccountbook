package io.github.wtbyt298.accountbook.presentation.response;

import io.github.wtbyt298.accountbook.application.query.model.journalentry.EntryDetailDto;
import io.github.wtbyt298.accountbook.presentation.shared.AmountPresentationFormatter;
import lombok.Getter;

/**
 * 仕訳明細の画面表示用クラス
 */
@Getter
public class EntryDetailView {

	private final String mergedAccountTitleName;
	private final String amount;
	
	public EntryDetailView(EntryDetailDto dto) {
		this.mergedAccountTitleName = mergedName(dto.getAccountTitleName(), dto.getSubAccountTitleName());
		this.amount = AmountPresentationFormatter.yen(dto.getAmount());
	}
	
	/**
	 * 勘定科目名と補助科目名を結合する
	 */
	private String mergedName(String accountTitleName, String subAccountTitleName) {
		if (subAccountTitleName.isBlank()) {
			return accountTitleName;
		}
		return accountTitleName + "：" + subAccountTitleName;
	}
	
}