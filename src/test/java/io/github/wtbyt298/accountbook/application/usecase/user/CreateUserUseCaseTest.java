package io.github.wtbyt298.accountbook.application.usecase.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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
		//given
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		
		//when
		String id = "TEST_USER";
		String password = "Test0123OK";
		String mailAddress = "test@example.com";
		CreateUserCommand command = new CreateUserCommand(id, password, mailAddress);
		createUseCase.execute(command);
		verify(userRepository).save(captor.capture()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then
		User capturedUser = captor.getValue();
		assertEquals(id, capturedUser.id().value());
		assertTrue(capturedUser.acceptPassword(password));
		assertEquals(mailAddress, capturedUser.mailAddress());
		assertEquals(UserStatus.ACTIVE, capturedUser.userStatus());
	}

}
