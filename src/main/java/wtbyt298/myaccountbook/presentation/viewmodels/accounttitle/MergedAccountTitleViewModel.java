package wtbyt298.myaccountbook.presentation.viewmodels.accounttitle;

import lombok.Getter;
import wtbyt298.myaccountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;

/**
 * 仕訳登録画面のセレクトボックスに表示するデータを保持するクラス
 * 勘定科目データと補助科目データを連結する
 */
@Getter
public class MergedAccountTitleViewModel {

	private final String mergedId;   //"勘定科目ID-補助科目ID"　例："401-0"のような形式
	private final String mergedName; //"勘定科目名：補助科目名"　例："食費：その他"のような形式
	
	public MergedAccountTitleViewModel(AccountTitleAndSubAccountTitleDto dto) {
		//補助科目が存在しない場合は勘定科目名を表示
		if (dto.getSubAccountTitleName().isBlank()) {
			this.mergedId = dto.getAccountTitleId() + "-" + dto.getSubAccountTitleId();
			this.mergedName = dto.getAccountTitleName();
		} else {
			this.mergedId = dto.getAccountTitleId() + "-" + dto.getSubAccountTitleId();
			this.mergedName = dto.getAccountTitleName() + "：" + dto.getSubAccountTitleName();	
		}
	}
	
}
