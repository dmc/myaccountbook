package io.github.wtbyt298.accountbook.helper.testdatacreator;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static generated.Tables.SUB_ACCOUNTTITLES;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.helper.testfactory.SubAccountTitleTestFactory;

/**
 * DBにテスト用の補助科目データを作成するクラス
 */
@Component
public class SubAccountTitleTestDataCreator {

	@Autowired
	private DSLContext jooq;
	
	public SubAccountTitle create(AccountTitleId parentId, String id, String name, UserId userId) {
		jooq.insertInto(SUB_ACCOUNTTITLES)
			.set(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID, id)
			.set(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID, parentId.value())
			.set(SUB_ACCOUNTTITLES.USER_ID, userId.value())
			.set(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, name)
			.execute();
		return SubAccountTitleTestFactory.create(id, name);
	}
	
}
