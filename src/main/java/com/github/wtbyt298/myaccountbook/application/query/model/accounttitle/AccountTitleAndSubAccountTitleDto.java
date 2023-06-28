package com.github.wtbyt298.myaccountbook.application.query.model.accounttitle;

import lombok.Getter;

/**
 * DBから取得した勘定科目と補助科目のデータを保持するクラス
 */
@Getter
public class AccountTitleAndSubAccountTitleDto {

	private final String accountTitleId;
	private final String accountTitleName;
	private final String subAccountTitleId;
	private final String subAccountTitleName;
	
	public AccountTitleAndSubAccountTitleDto(String accountTitleId, String accountTitleName, String subAccountTitleId, String subAccountTitleName) {
		this.accountTitleId =  accountTitleId;
		this.accountTitleName = accountTitleName;
		this.subAccountTitleId = subAccountTitleId;
		this.subAccountTitleName = subAccountTitleName;
	}

}
