package io.github.wtbyt298.accountbook.helper.testdatacreator;

import org.springframework.beans.factory.annotation.Autowired;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.helper.testfactory.SubAccountTitlesTestFactory;

/**
 * DBにテスト用の補助科目データを作成するクラス
 */
public class SubAccountTitleTestDataCreator {
	
	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;

	public SubAccountTitles create() {
		SubAccountTitles subAccountTitles = SubAccountTitlesTestFactory.create();
		UserId userId = UserId.valueOf("TEST_USER");
		subAccountTitleRepository.save(subAccountTitles, userId);
		return subAccountTitles;
	}
	
}
