package wtbyt298.myaccountbook.helper.testfactory;

import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitle;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleName;

/**
 * テスト用の補助科目インスタンスを生成するクラス
 */
public class SubAccountTitleTestFactory {

	public static SubAccountTitle create(String id, String name) {
		return new SubAccountTitle(
			SubAccountTitleId.valueOf(id), 
			SubAccountTitleName.valueOf(name)
		);
	}
	
}
