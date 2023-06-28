package wtbyt298.myaccountbook.presentation.viewmodels.journalentry;

import lombok.Getter;
import wtbyt298.myaccountbook.application.query.model.journalentry.EntryDetailDto;
import wtbyt298.myaccountbook.presentation.shared.util.AmountPresentationFormatter;

/**
 * 仕訳明細の画面表示用クラス
 */
@Getter
public class EntryDetailViewModel {

	private final String mergedAccountTitleName;
	private final String amount;
	
	public EntryDetailViewModel(EntryDetailDto dto) {
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
