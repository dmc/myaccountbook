package io.github.wtbyt298.accountbook.application.query.model.accounttitle;

import lombok.Getter;

/**
 * DBから取得した勘定科目IDと勘定科目名を保持するクラス
 */
@Getter
public class AccountTitleIdAndNameDto {

	private final String id;
	private final String name;
	
	public AccountTitleIdAndNameDto(String id, String name) {
		this.id = id;
		this.name = name;
	}

}
