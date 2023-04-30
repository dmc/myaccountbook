package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SubAccountTitlesTest {
	
	@Test
	void 補助科目を追加できる() {
		//given 補助科目が追加されていない or 2個追加されている
		SubAccountTitles empty = hasNoElement();
		SubAccountTitles two = hasTwoElements();
		
		//when
		SubAccountTitle subAccountTitle = createSubAccountTitle("9", "TEST");
		empty.add(subAccountTitle);
		two.add(subAccountTitle);
		
		//then
		assertEquals(2, empty.elements().size()); //元々0の場合は、引数で与えた補助科目に加えて「0：その他」が追加される
		assertEquals(3, two.elements().size());
	}
	
	@Test
	void 既に補助科目が10個追加されている場合は追加できない() {
		//given 補助科目が10個追加されている
		SubAccountTitles full = hasMaxElements();
		
		//when
		SubAccountTitle subAccountTitle = createSubAccountTitle("A", "OVER");
		Exception exception = assertThrows(RuntimeException.class, () -> full.add(subAccountTitle));
		
		//then
		assertEquals("これ以上補助科目を追加できません。", exception.getMessage());
	}
	
	@Test
	void 既に同一の補助科目が存在する場合は追加できない() {
		//given
		SubAccountTitles empty = hasNoElement();
		
		//when
		empty.add(createSubAccountTitle("2", "外食"));
		Exception exception = assertThrows(IllegalArgumentException.class, () -> empty.add(createSubAccountTitle("2", "外食"))); //同じ補助科目をもう一度追加
	
		//then
		assertEquals("指定した補助科目は既に存在しています。", exception.getMessage());
	}
	
	@Test
	void IDで検索して一致する補助科目を取得できる() {
		//given
		SubAccountTitles empty = hasNoElement();
		SubAccountTitles two = hasTwoElements();
		
		//when
		SubAccountTitleId key = SubAccountTitleId.valueOf("1");
		SubAccountTitle found = two.find(key);
		
		//then
		SubAccountTitle expected = new SubAccountTitle(
			SubAccountTitleId.valueOf("1"), 
			SubAccountTitleName.valueOf("食料品")
		);
		assertEquals(SubAccountTitle.EMPTY, empty.find(key)); //要素を持たない場合はEMPTYを返す
		assertEquals(expected, found);
	}
	
	@Test
	void IDを指定して補助科目名を変更できる() {
		//given
		SubAccountTitles two = hasTwoElements();
		
		//when
		SubAccountTitleId id = SubAccountTitleId.valueOf("1");
		SubAccountTitleName newName = SubAccountTitleName.valueOf("変更後の補助科目名");
		two.changeSubAccountTitleName(id, newName);
		
		//then
		assertEquals("1：変更後の補助科目名", two.find(id).toString());
	}
	
	@Test
	void 指定したIDに該当する補助科目が存在しない場合は例外発生() {
		//given
		SubAccountTitles two = hasTwoElements();
		
		//when
		SubAccountTitleId key = SubAccountTitleId.valueOf("5");
		Exception exception = assertThrows(IllegalArgumentException.class, () -> two.find(key));
		
		//then
		assertEquals("指定した補助科目は存在しません。", exception.getMessage());
	}
	
	/**
	 * 補助科目のインスタンスを生成する
	 */
	private SubAccountTitle createSubAccountTitle(String id, String name) {
		return new SubAccountTitle(
			SubAccountTitleId.valueOf(id), 
			SubAccountTitleName.valueOf(name)
		);
	}
	
	/**
	 * 要素数0のインスタンスを生成する
	 */
	private SubAccountTitles hasNoElement() {
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();
		return new SubAccountTitles(map);
	}
	
	/**
	 * 要素数2のインスタンスを生成する 
	 */
	private SubAccountTitles hasTwoElements() {
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();
		SubAccountTitleId id1 = SubAccountTitleId.valueOf("0");
		map.put(id1, createSubAccountTitle("0", "その他"));
		SubAccountTitleId id2= SubAccountTitleId.valueOf("1");
		map.put(id2, createSubAccountTitle("1", "食料品"));
		return new SubAccountTitles(map);
	}
	
	/**
	 * 要素数10のインスタンスを生成する
	 */
	private SubAccountTitles hasMaxElements() {
		String [] names = {"その他" ,"食料品", "外食", "軽食", "科目5", "科目6", "科目7", "科目8", "科目9", "科目10"};
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();
		for (int i = 0; i < names.length; i++) {
			String index = String.valueOf(i);
			SubAccountTitleId id = SubAccountTitleId.valueOf(index);
			map.put(id, createSubAccountTitle(index, names[i]));
		}
		return new SubAccountTitles(map);
	}
	
}
