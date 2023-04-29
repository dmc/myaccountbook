package io.github.wtbyt298.accountbook.application.usecase.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.wtbyt298.accountbook.domain.model.user.EncodedUserPassword;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserStatus;

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
		//given
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		UserId userId = UserId.valueOf("TEST_USER");
		when(userRepository.exists(userId)).thenReturn(true); //ユーザIDに該当するユーザは存在している
		when(userRepository.findById(userId)).thenReturn(createActiveUser(userId)); 
		
		//when
		disableUseCase.execute(userId);
		verify(userRepository).update(captor.capture()); //リポジトリのupdateメソッドに渡される値をキャプチャする
		
		//then
		User capturedUser = captor.getValue();
		assertEquals(UserStatus.INACTIVE.toString(), capturedUser.userStatus());
	}
	
	@Test
	void 指定したユーザIDに該当するユーザが見つからない場合は例外発生() {
		//given
		UserId userId = UserId.valueOf("TEST_USER");
		when(userRepository.exists(userId)).thenReturn(false); //ユーザIDに該当するユーザは存在しない
		
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> disableUseCase.execute(userId));
		
		//then
		assertEquals("指定したユーザは存在しません。", exception.getMessage());
	}
	
	/**
	 * テスト用のユーザインスタンスを生成する
	 */
	private User createActiveUser(UserId userId) {
		return User.reconstruct(userId, 
			EncodedUserPassword.valueOf("TEST"), 
			"test:example.com", 
			UserStatus.ACTIVE
		);
	}

}
