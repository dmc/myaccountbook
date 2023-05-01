package io.github.wtbyt298.accountbook.application.usecase.subaccounttitle;

import java.util.HashMap;
import java.util.Map;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;

class TestObjectCreator {
	
	/**
	 * テスト用の補助科目のコレクションオブジェクトを生成する
	 * 初期値として以下の2つが追加されている
	 * 「0：その他」
	 * 「1：三菱UFJ銀行」
	 */
	static SubAccountTitles prepareTestObject() {
		Map<SubAccountTitleId, SubAccountTitle> collection = new HashMap<>();
		AccountTitleId parentId = AccountTitleId.valueOf("102");
		collection.put(SubAccountTitleId.valueOf("0"), createSubAccountTitle("0", "その他"));
		collection.put(SubAccountTitleId.valueOf("1"), createSubAccountTitle("1", "三菱UFJ銀行"));
		return new SubAccountTitles(collection, parentId);
	}
	
	private static SubAccountTitle createSubAccountTitle(String id, String name) {
		return new SubAccountTitle(
			SubAccountTitleId.valueOf(id), 
			SubAccountTitleName.valueOf(name)
		);
	}
}
