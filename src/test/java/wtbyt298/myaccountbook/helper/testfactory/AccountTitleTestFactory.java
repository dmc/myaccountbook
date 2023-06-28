package wtbyt298.myaccountbook.helper.testfactory;

import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleName;

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
