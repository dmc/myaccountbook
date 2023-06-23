package io.github.wtbyt298.myaccountbook.helper.testfactory;

import java.util.HashMap;
import java.util.Map;

import io.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleName;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitles;

public class SubAccountTitlesTestFactory {
	
	/**
	 * テスト用の補助科目のコレクションオブジェクトを生成する
	 * 初期値として以下の2つが追加されている
	 * 「0：その他」
	 * 「1：三菱UFJ銀行」
	 */
	public static SubAccountTitles create() {
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();
		map.put(SubAccountTitleId.valueOf("0"), createSubAccountTitle("0", "その他"));
		map.put(SubAccountTitleId.valueOf("1"), createSubAccountTitle("1", "三菱UFJ銀行"));
		
		AccountTitleId parentId = AccountTitleId.valueOf("102");
		
		return new SubAccountTitles(map, parentId);
	}
	
	private static SubAccountTitle createSubAccountTitle(String id, String name) {
		return new SubAccountTitle(
			SubAccountTitleId.valueOf(id), 
			SubAccountTitleName.valueOf(name)
		);
	}
}