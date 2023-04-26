package io.github.wtbyt298.accountbook.presentation.response;

import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import lombok.Getter;

/**
 * 仕訳登録画面のセレクトボックスに表示するデータを保持するクラス
 * 勘定科目データと補助科目データを連結する
 */
@Getter
public class MergedAccountTitleView {

	private final String mergedId;       //"勘定科目ID-補助科目ID"　例："401-0"のような形式
	private final String nameForDisplay; //"勘定科目名：補助科目名"　例："食費：その他"のような形式
	
	public MergedAccountTitleView(AccountTitleAndSubAccountTitleDto dto) {
		if (dto.getSubAccountTitleName().isBlank()) {
			this.mergedId = dto.getAccountTitleId() + "-" + dto.getSubAccountTitleId();
			this.nameForDisplay = dto.getAccountTitleName(); //補助科目が存在しない場合は勘定科目名を表示
		} else {
			this.mergedId = dto.getAccountTitleId() + "-" + dto.getSubAccountTitleId();
			this.nameForDisplay = dto.getAccountTitleName() + "：" + dto.getSubAccountTitleName();	
		}
	}
	
}
