package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SubAccountTitleTest {

	@Test
	void 補助科目IDと補助科目名で初期化できる() {
		//when
		SubAccountTitle subAccountTitle = new SubAccountTitle(
			SubAccountTitleId.valueOf("1"),
			SubAccountTitleName.valueOf("食料品")
		);
		
		//then
		assertEquals("1：食料品", subAccountTitle.toString());
	}
	
	@Test
	void 補助科目名を変更できる() {
		//given
		SubAccountTitle subAccountTitle = new SubAccountTitle(
			SubAccountTitleId.valueOf("1"),
			SubAccountTitleName.valueOf("食料品")
		);
		
		//when
		subAccountTitle.rename(SubAccountTitleName.valueOf("外食"));
		
		//then
		assertEquals("1：外食", subAccountTitle.toString());
	}
	
	@Test
	void 保持している補助科目IDと補助科目名が等価なら等価判定() {
		//when
		SubAccountTitle subAccountTitle1 = new SubAccountTitle(
			SubAccountTitleId.valueOf("0"), 
			SubAccountTitleName.valueOf("その他")
		);
		SubAccountTitle subAccountTitle2 = new SubAccountTitle(
			SubAccountTitleId.valueOf("0"), 
			SubAccountTitleName.valueOf("その他")
		);
		SubAccountTitle subAccountTitle3 = new SubAccountTitle(
			SubAccountTitleId.valueOf("1"), 
			SubAccountTitleName.valueOf("食料品")
		);
		
		//then
		assertEquals(subAccountTitle1, subAccountTitle2);
		assertNotEquals(subAccountTitle1, subAccountTitle3);
	}
	
	@Test
	void 等価と判定されるならハッシュ値も等しくなる() {
		//when
		SubAccountTitle subAccountTitle1 = new SubAccountTitle(
			SubAccountTitleId.valueOf("0"), 
			SubAccountTitleName.valueOf("その他")
		);
		SubAccountTitle subAccountTitle2 = new SubAccountTitle(
			SubAccountTitleId.valueOf("0"), 
			SubAccountTitleName.valueOf("その他")
		);
		SubAccountTitle subAccountTitle3 = new SubAccountTitle(
			SubAccountTitleId.valueOf("1"), 
			SubAccountTitleName.valueOf("食料品")
		);
		
		//then
		assertEquals(subAccountTitle1.hashCode(), subAccountTitle2.hashCode());
		assertNotEquals(subAccountTitle1.hashCode(), subAccountTitle3.hashCode());
	}

}
