package io.github.wtbyt298.accountbook.helper.testfactory;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleName;

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
