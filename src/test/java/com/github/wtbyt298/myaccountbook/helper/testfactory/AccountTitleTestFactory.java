package com.github.wtbyt298.myaccountbook.helper.testfactory;

import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleName;

/**
 * テスト用の勘定科目インスタンスを生成するクラス
 */
public class AccountTitleTestFactory {

	public static AccountTitle create(String id, String name, AccountingType type) {
		return new AccountTitle(
			AccountTitleId.valueOf(id), 
			AccountTitleName.valueOf(name), 
			type
		);
	}
	
}
