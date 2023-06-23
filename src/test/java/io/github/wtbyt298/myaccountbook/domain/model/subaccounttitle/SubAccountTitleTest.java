package io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.myaccountbook.helper.testfactory.SubAccountTitleTestFactory;

class SubAccountTitleTest {

	@Test
	void 補助科目IDと補助科目名で初期化できる() {
		//when:
		SubAccountTitle subAccountTitle = new SubAccountTitle(
			SubAccountTitleId.valueOf("1"),
			SubAccountTitleName.valueOf("食料品")
		);
		
		//then:
		assertEquals("補助科目ID：1 補助科目名：食料品", subAccountTitle.toString());
	}
	
	@Test
	void 補助科目名を変更できる() {
		//given:テスト用の補助科目
		SubAccountTitle subAccountTitle = SubAccountTitleTestFactory.create("1", "食料品");
		assertEquals("補助科目ID：1 補助科目名：食料品", subAccountTitle.toString());
		
		//when:補助科目名を変更する（"食料品"→"外食"）
		subAccountTitle.rename(SubAccountTitleName.valueOf("外食"));
		
		//then:補助科目名が変更されている
		assertEquals("補助科目ID：1 補助科目名：外食", subAccountTitle.toString());
	}
	
}
