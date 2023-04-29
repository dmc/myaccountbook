package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;

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

}
