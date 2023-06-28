package com.github.wtbyt298.myaccountbook.application.usecase.subaccounttitle;

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

import com.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import com.github.wtbyt298.myaccountbook.application.usecase.subaccounttitle.AppendSubAccountTitleCommand;
import com.github.wtbyt298.myaccountbook.application.usecase.subaccounttitle.AppendSubAccountTitleUseCase;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitles;
import com.github.wtbyt298.myaccountbook.helper.testfactory.SubAccountTitlesTestFactory;

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
		//given:
		ArgumentCaptor<SubAccountTitles> captor = ArgumentCaptor.forClass(SubAccountTitles.class);
	
		//依存オブジェクトの設定
		//既に「0：その他」「1：三菱UFJ銀行」という2つの補助科目が存在している
		when(subAccountTitleRepository.findCollectionByParentId(any(), any())).thenReturn(SubAccountTitlesTestFactory.create());
		
		//when:コマンドオブジェクトを渡してテスト対象メソッドを実行する
		AppendSubAccountTitleCommand command = new AppendSubAccountTitleCommand("102", "三井住友銀行");
		appendUseCase.execute(command, userSession);
		verify(subAccountTitleRepository).save(captor.capture(), any()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then:新規作成された補助科目が追加された状態でリポジトリに渡されている
		SubAccountTitleId added = SubAccountTitleId.valueOf("2");
		SubAccountTitles captured = captor.getValue();
		assertEquals(3, captured.elements().size()); //要素数2 → 3
		assertEquals("補助科目ID：2 補助科目名：三井住友銀行", captured.find(added).toString());
	}

}
