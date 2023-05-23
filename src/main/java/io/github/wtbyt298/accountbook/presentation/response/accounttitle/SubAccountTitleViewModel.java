package io.github.wtbyt298.accountbook.presentation.response.accounttitle;

import lombok.Getter;

/**
 * 補助科目の表示用データを保持するクラス
 */
@Getter
public class SubAccountTitleViewModel {

	private final String id;
	private final String name;
	
	public SubAccountTitleViewModel(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
}
