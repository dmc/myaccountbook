package io.github.wtbyt298.accountbook.application.usecase.user;

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
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserStatus;
import io.github.wtbyt298.accountbook.helper.testfactory.UserTestFactory;

class DisableUserUseCaseTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private DisableUserUseCase disableUseCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void 指定したユーザIDに該当するユーザを無効化できる() {
		//given:テスト用のユーザ
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		User user = UserTestFactory.create("TEST_USER");
		
		//依存オブジェクトの設定
		when(userRepository.exists(any())).thenReturn(true); //ユーザIDに該当するユーザは存在している
		when(userRepository.findById(any())).thenReturn(user); 
		
		//when:IDを指定してテスト対象メソッドを実行する
		UserId userId = user.id();
		disableUseCase.execute(userId);
		verify(userRepository).update(captor.capture()); //リポジトリのupdateメソッドに渡される値をキャプチャする
		
		//then:ユーザステータスが有効→無効に変更されている
		User capturedUser = captor.getValue();
		assertEquals(UserStatus.INACTIVE, capturedUser.userStatus());
	}
	
	@Test
	void 指定したユーザIDに該当するユーザが見つからない場合は例外発生() {
		//given:ユーザIDに該当するユーザは存在しない
		when(userRepository.exists(any())).thenReturn(false);
		
		//when:IDを指定してテスト対象メソッドを実行する
		UserId userId = UserId.valueOf("TEST_USER");
		Exception exception = assertThrows(RuntimeException.class, () -> disableUseCase.execute(userId));
		
		//then:想定した例外が発生している
		assertEquals("指定したユーザは存在しません。", exception.getMessage());
	}
	
}
