package io.github.wtbyt298.accountbook.application.usecase.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

class AppendSubAccountTitleUseCaseTest {
	
	@Mock
	private SubAccountTitleRepository subAccountTitleRepository;
	
	@Mock
	private UserSession userSession;
	
	@InjectMocks
	private AppendSubAccountTitleUseCase appendUseCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 引数に渡したDTOをもとに作成された補助科目が追加される() {
		//given
		ArgumentCaptor<SubAccountTitles> captor = ArgumentCaptor.forClass(SubAccountTitles.class);
		AccountTitleId parentId = AccountTitleId.valueOf("102");
		//依存オブジェクトの設定
		when(userSession.userId()).thenReturn(UserId.valueOf("TEST_USER"));
		when(subAccountTitleRepository.findCollectionByParentId(parentId, userSession.userId())).thenReturn(prepareTestObject());
		
		//when
		AppendSubAccountTitleCommand command = new AppendSubAccountTitleCommand("102", "三井住友銀行");
		appendUseCase.execute(command, userSession);
		verify(subAccountTitleRepository).save(captor.capture(), any()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then
		SubAccountTitleId added = SubAccountTitleId.valueOf("2");
		SubAccountTitles captured = captor.getValue();
		assertEquals(3, captured.elements().size()); //要素数2 → 3
		assertEquals("2：三井住友銀行", captured.find(added).toString());
	}
	
	/**
	 * テスト用の補助科目のコレクションオブジェクトを生成する
	 * 初期値として以下の2つが追加されている
	 * 「0：その他」
	 * 「1：三菱UFJ銀行」
	 */
	private SubAccountTitles prepareTestObject() {
		Map<SubAccountTitleId, SubAccountTitle> collection = new HashMap<>();
		AccountTitleId parentId = AccountTitleId.valueOf("102");
		collection.put(SubAccountTitleId.valueOf("0"), createSubAccountTitle("0", "その他"));
		collection.put(SubAccountTitleId.valueOf("1"), createSubAccountTitle("1", "三菱UFJ銀行"));
		return new SubAccountTitles(collection, parentId);
	}
	
	private SubAccountTitle createSubAccountTitle(String id, String name) {
		return new SubAccountTitle(
			SubAccountTitleId.valueOf(id), 
			SubAccountTitleName.valueOf(name)
		);
	}

}
