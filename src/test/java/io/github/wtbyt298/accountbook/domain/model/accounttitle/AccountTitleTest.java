package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;

class AccountTitleTest {
	
	@Test
	void 勘定科目IDと勘定科目名と会計要素で初期化できる() {
		//when
		AccountTitle accountTitle = new AccountTitle(
			AccountTitleId.valueOf("101"), 
			AccountTitleName.valueOf("現金"), 
			AccountingType.ASSETS
		);
		
		//then
		assertEquals("101：現金", accountTitle.toString());		
	}
	
}
