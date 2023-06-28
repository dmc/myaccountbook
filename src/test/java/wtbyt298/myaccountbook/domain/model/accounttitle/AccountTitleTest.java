package wtbyt298.myaccountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleName;

class AccountTitleTest {
	
	@Test
	void 勘定科目IDと勘定科目名と会計要素で初期化できる() {
		//when:
		AccountTitle accountTitle = new AccountTitle(
			AccountTitleId.valueOf("101"), 
			AccountTitleName.valueOf("現金"), 
			AccountingType.ASSETS
		);
		
		//then:
		assertEquals("勘定科目ID：101 勘定科目名：現金", accountTitle.toString());		
	}
	
}
