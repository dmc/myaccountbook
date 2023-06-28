package com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleName;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitles;
import com.github.wtbyt298.myaccountbook.helper.testfactory.SubAccountTitleTestFactory;

class SubAccountTitlesTest {
	
	@Test
	void 補助科目を追加できる() {
		//given:補助科目が追加されていない or 2個追加されている
		SubAccountTitles empty = hasNoElement();
		SubAccountTitles two = hasTwoElements();
		assertEquals(0, empty.elements().size());
		assertEquals(2, two.elements().size());
		
		//when:それぞれに補助科目を追加する
		SubAccountTitleName newName = SubAccountTitleName.valueOf("新規追加");
		empty.add(newName);
		two.add(newName);
		
		//then:保持している補助科目の数が増加している
		assertEquals(2, empty.elements().size()); //元々0の場合は、引数で与えた補助科目に加えて「0：その他」が追加される
		assertEquals(3, two.elements().size());
	}
	
	@Test
	void 既に補助科目が10個追加されている場合は追加できない() {
		//given:補助科目が10個追加されている
		SubAccountTitles full = hasMaxElements();
		
		//when:補助科目を追加する
		SubAccountTitleName newName = SubAccountTitleName.valueOf("新規追加");
		Exception exception = assertThrows(RuntimeException.class, () -> full.add(newName));
		
		//then:想定した例外が発生している
		assertEquals("これ以上補助科目を追加できません。", exception.getMessage());
 	}
	
	@Test
	void 既に同名の補助科目が存在する場合は追加できない() {
		//given:既に補助科目が存在する　※この例の場合、「0：その他」「1：新規追加」が存在する
		SubAccountTitles empty = hasNoElement();
		SubAccountTitleName newName = SubAccountTitleName.valueOf("新規追加");
		empty.add(newName);
		
		//when:同名の補助科目を追加する
		SubAccountTitleName same = SubAccountTitleName.valueOf("新規追加");
		Exception exception = assertThrows(RuntimeException.class, () -> empty.add(same));
	
		//then:想定した例外が発生している
		assertEquals("指定した補助科目は既に存在しています。", exception.getMessage());
	}
	
	@Test
	void IDで検索して一致する補助科目を取得できる() {
		//given:
		SubAccountTitles empty = hasNoElement(); //補助科目なし
		SubAccountTitles two = hasTwoElements(); //「0：その他」「1：食料品」が既に追加されている
		
		//when:補助科目IDをキーに補助科目を取得する
		SubAccountTitleId id = SubAccountTitleId.valueOf("1");
		SubAccountTitle foundFromEmpty = empty.find(id);
		SubAccountTitle foundFromTwo = two.find(id);
		
		//then:IDに一致する補助科目を取得できる
		SubAccountTitle expected = SubAccountTitleTestFactory.create("1", "食料品");
		assertEquals(SubAccountTitle.EMPTY.toString(), foundFromEmpty.toString()); //要素を持たない場合はEMPTYを返す
		assertEquals(expected.toString(), foundFromTwo.toString());
	}
	
	@Test
	void IDを指定して補助科目名を変更できる() {
		//given:「0：その他」「1：食料品」が既に追加されている
		SubAccountTitles two = hasTwoElements();
		SubAccountTitleId id = SubAccountTitleId.valueOf("1");
		assertEquals("補助科目ID：1 補助科目名：食料品", two.find(id).toString());
		
		//when:補助科目名を変更する
		SubAccountTitleName newName = SubAccountTitleName.valueOf("外食");
		two.changeSubAccountTitleName(id, newName);
		
		//then:補助科目名が変更されている
		assertEquals("補助科目ID：1 補助科目名：外食", two.find(id).toString());
	}
	
	@Test
	void 指定したIDに該当する補助科目が存在しない場合は例外発生() {
		//given:「0：その他」「1：食料品」が既に追加されている
		SubAccountTitles two = hasTwoElements();
		
		//when:存在しない補助科目IDを引数に渡してfindメソッドを呼び出す
		SubAccountTitleId id = SubAccountTitleId.valueOf("5");
		Exception exception = assertThrows(RuntimeException.class, () -> two.find(id));
		
		//then:想定した例外が発生している
		assertEquals("指定した補助科目は存在しません。", exception.getMessage());
	}
	
	@Test
	void 既に同名の補助科目が存在している場合は名前を変更できない() {
		//given:「0：その他」「1：食料品」が既に追加されている
		SubAccountTitles two = hasTwoElements();
		
		//when:「1：食料品」の補助科目名を「その他」に変更する
		SubAccountTitleName newName = SubAccountTitleName.valueOf("その他");
		Exception exception = assertThrows(RuntimeException.class, () -> two.changeSubAccountTitleName(SubAccountTitleId.valueOf("1"), newName));
		
		//then:想定した例外が発生している
		assertEquals("同名の補助科目が既に存在しています。", exception.getMessage());
	}
	
	//以下ヘルパーメソッド
	
	/**
	 * 要素数0のインスタンスを生成する
	 */
	private SubAccountTitles hasNoElement() {
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();
		AccountTitleId parentId = AccountTitleId.valueOf("401");
		
		return new SubAccountTitles(map, parentId);
	}
	
	/**
	 * 要素数2のインスタンスを生成する 
	 */
	private SubAccountTitles hasTwoElements() {
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();
		AccountTitleId parentId = AccountTitleId.valueOf("401");
		
		SubAccountTitleId id1 = SubAccountTitleId.valueOf("0");
		map.put(id1, SubAccountTitleTestFactory.create("0", "その他"));
		
		SubAccountTitleId id2= SubAccountTitleId.valueOf("1");
		map.put(id2, SubAccountTitleTestFactory.create("1", "食料品"));
		
		return new SubAccountTitles(map, parentId);
	}
	
	/**
	 * 要素数10のインスタンスを生成する
	 */
	private SubAccountTitles hasMaxElements() {
		String [] names = {"その他" ,"食料品", "外食", "軽食", "科目5", "科目6", "科目7", "科目8", "科目9", "科目10"};
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();
		AccountTitleId parentId = AccountTitleId.valueOf("401");
		
		for (int i = 0; i < names.length; i++) {
			String index = String.valueOf(i);
			SubAccountTitleId id = SubAccountTitleId.valueOf(index);
			map.put(id, SubAccountTitleTestFactory.create(index, names[i]));
		}
		
		return new SubAccountTitles(map, parentId);
	}
	
}
