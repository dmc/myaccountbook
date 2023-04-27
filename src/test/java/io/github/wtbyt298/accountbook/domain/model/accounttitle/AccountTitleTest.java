package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;

class AccountTitleTest {
	
	@Test
	void 勘定科目IDと勘定科目名と会計要素および補助科目のMapで初期化できる() {
		//when
		AccountTitle accountTitle = new AccountTitle(
			AccountTitleId.valueOf("101"), 
			AccountTitleName.valueOf("現金"), 
			AccountingType.ASSETS, 
			new HashMap<>()
		);
		
		//then
		assertEquals("101：現金", accountTitle.toString());		
	}
	
	@Test
	void 補助科目を追加できる() {
		//given 補助科目を持たない勘定科目
		AccountTitle accountTitle = subIsEmpty();
		
		//when
		SubAccountTitle sub = new SubAccountTitle(
			SubAccountTitleId.valueOf("0"), 
			SubAccountTitleName.valueOf("その他")
		);
		accountTitle.addSubAccountTitle(sub);
		
		//then
		SubAccountTitle added = accountTitle.findChild(SubAccountTitleId.valueOf("0"));
		assertEquals(sub.toString(), added.toString());
	}
	
	@Test
	void 既に保持している補助科目の数が10の場合は追加できない() {
		//given 補助科目が10個追加されている
		AccountTitle accountTitle = subIsFull();
		
		//when
		SubAccountTitle sub = createSub("A", "TEST");
		Exception exception = assertThrows(RuntimeException.class, () -> accountTitle.addSubAccountTitle(sub));
		
		//then
		assertEquals("これ以上補助科目を追加できません。", exception.getMessage());
	}

	@Test
	void 補助科目IDで指定した補助科目を取得できる() {
		//given
		AccountTitle accountTitle1 = subIsEmpty(); //補助科目を持たない勘定科目
		AccountTitle accountTitle2 =subIsFull();   //補助科目が10個追加された勘定科目
		
		//when
		SubAccountTitle sub1 = accountTitle1.findChild(SubAccountTitleId.valueOf("0"));
		SubAccountTitle sub2 = accountTitle2.findChild(SubAccountTitleId.valueOf("0"));
		Exception exception = assertThrows(IllegalArgumentException.class, () -> accountTitle2.findChild(SubAccountTitleId.valueOf("A")));
		
		//then
		assertEquals("0：補助科目なし", sub1.toString());                         //補助科目を保持していない場合はEMPTYを返す
		assertEquals("0：その他", sub2.toString());                               //保持している補助科目に合致すればそれを返す
		assertEquals("指定した補助科目は存在しません。", exception.getMessage()); //存在しない補助科目を指定すると例外発生
	}
	
	/*
	 * 補助科目を持たない勘定科目を返す
	 */
	private AccountTitle subIsEmpty() {
		return new AccountTitle(
			AccountTitleId.valueOf("401"), 
			AccountTitleName.valueOf("食費"), 
			AccountingType.EXPENSES, 
			new HashMap<>()
		);
	}
	
	/**
	 * 補助科目が最大数まで追加されている勘定科目を返す
	 */
	private AccountTitle subIsFull() {
		AccountTitle accountTitle = subIsEmpty();
		accountTitle.addSubAccountTitle(createSub("0", "その他"));
		accountTitle.addSubAccountTitle(createSub("1", "食料品"));
		accountTitle.addSubAccountTitle(createSub("2", "外食"));
		accountTitle.addSubAccountTitle(createSub("3", "お菓子"));
		accountTitle.addSubAccountTitle(createSub("4", "カフェ"));
		accountTitle.addSubAccountTitle(createSub("5", "てすと"));
		accountTitle.addSubAccountTitle(createSub("6", "テスト"));
		accountTitle.addSubAccountTitle(createSub("7", "TEST"));
		accountTitle.addSubAccountTitle(createSub("8", "test"));
		accountTitle.addSubAccountTitle(createSub("9", "これで最大数となる"));
		return accountTitle;
	}
	
	/**
	 * 補助科目を生成する
	 */
	private SubAccountTitle createSub(String id, String name) {
		return new SubAccountTitle(SubAccountTitleId.valueOf(id), SubAccountTitleName.valueOf(name));
	}
	
}
