package com.github.wtbyt298.myaccountbook.helper.testdatacreator;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.helper.testfactory.AccountTitleTestFactory;

import static generated.tables.Accounttitles.*;

/**
 * DBにテスト用の勘定科目データを作成するクラス
 */
@Component
public class AccountTitleTestDataCreator {

	@Autowired
	private DSLContext jooq;
	
	public AccountTitle create(String id, String name, AccountingType type) {
		AccountTitle accountTitle = AccountTitleTestFactory.create(id, name, type);
		
		jooq.insertInto(ACCOUNTTITLES)
			.set(ACCOUNTTITLES.ACCOUNTTITLE_ID, id)
			.set(ACCOUNTTITLES.ACCOUNTTITLE_NAME, name)
			.set(ACCOUNTTITLES.ACCOUNTING_TYPE, type.toString())
			.set(ACCOUNTTITLES.LOAN_TYPE, type.loanType().toString())
			.set(ACCOUNTTITLES.SUMMARY_TYPE, type.summaryType().toString())
			.execute();
		
		return accountTitle;
	}
	
}
