package com.github.wtbyt298.myaccountbook.helper.testdatacreator;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.user.UserId;
import com.github.wtbyt298.myaccountbook.helper.testfactory.SubAccountTitleTestFactory;

import static generated.Tables.SUB_ACCOUNTTITLES;

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
