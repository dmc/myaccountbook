package io.github.wtbyt298.accountbook.helper.testfactory;

import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;

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
