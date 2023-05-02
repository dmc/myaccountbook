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
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserStatus;

class CreateUserUseCaseTest {

	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private CreateUserUseCase createUseCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void ユーザID等を渡すとその値を使って新規作成されたユーザが保存される() {
		//given:ユーザはまだ存在していない
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		when(userRepository.exists(any())).thenReturn(false);
		
		//when:ユーザID等からコマンドオブジェクトを生成し、テスト対象メソッドを実行する
		String id = "TEST_USER";
		String password = "Test0123OK";
		String mailAddress = "test@example.com";
		CreateUserCommand command = new CreateUserCommand(id, password, mailAddress);
		createUseCase.execute(command);
		verify(userRepository).save(captor.capture()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then:渡された値をもとにユーザが作成されている
		User capturedUser = captor.getValue();
		assertEquals(id, capturedUser.id().value());
		assertTrue(capturedUser.acceptPassword(password));
		assertEquals(mailAddress, capturedUser.mailAddress());
		assertEquals(UserStatus.ACTIVE, capturedUser.userStatus());
	}
	
	@Test
	void 既に同一IDのユーザが存在している場合は例外発生() {
		//given:既にユーザが存在している
		when(userRepository.exists(any())).thenReturn(true);
		
		//when:ユーザID等からコマンドオブジェクトを生成し、テスト対象メソッドを実行する
		String id = "TEST_USER";
		String password = "Test0123OK";
		String mailAddress = "test@example.com";
		CreateUserCommand command = new CreateUserCommand(id, password, mailAddress);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> createUseCase.execute(command));
		
		//then:想定した例外が発生している
		assertEquals("既にユーザが存在しています。", exception.getMessage());
	}

}
