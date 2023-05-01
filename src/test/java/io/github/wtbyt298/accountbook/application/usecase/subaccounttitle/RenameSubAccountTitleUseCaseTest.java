package io.github.wtbyt298.accountbook.application.usecase.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;

class RenameSubAccountTitleUseCaseTest {
	
	@Mock
	private SubAccountTitleRepository subAccountTitleRepository;
	
	@Mock
	private UserSession userSession;
	
	@InjectMocks
	private RenameSubAccountTitleUseCase renameUseCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 補助科目IDを指定して補助科目名を変更できる() {
		//given
		ArgumentCaptor<SubAccountTitles> captor = ArgumentCaptor.forClass(SubAccountTitles.class);
		//依存オブジェクトの設定
		//既に「0：その他」「1：三菱UFJ銀行」という2つの補助科目が存在している
		when(subAccountTitleRepository.findCollectionByParentId(any(), any())).thenReturn(SubAccountTitlesTestDataCreator.prepareTestObject());
		
		//when
		RenameSubAccountTitleCommand command = new RenameSubAccountTitleCommand("102", "1", "ゆうちょ銀行");
		renameUseCase.execute(command, userSession);
		verify(subAccountTitleRepository).save(captor.capture(), any()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then
		SubAccountTitleId changed = SubAccountTitleId.valueOf("1");
		SubAccountTitles captured = captor.getValue();
		assertEquals("補助科目ID：1 補助科目名：ゆうちょ銀行", captured.find(changed).toString());
		assertNotEquals("補助科目ID：1 補助科目名：三菱UFJ銀行", captured.find(changed).toString());
	}

}
