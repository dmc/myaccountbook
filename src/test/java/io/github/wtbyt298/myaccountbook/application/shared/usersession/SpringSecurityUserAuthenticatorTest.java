package io.github.wtbyt298.myaccountbook.application.shared.usersession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import io.github.wtbyt298.myaccountbook.domain.model.user.User;
import io.github.wtbyt298.myaccountbook.domain.model.user.UserRepository;
import io.github.wtbyt298.myaccountbook.helper.testfactory.UserTestFactory;

class SpringSecurityUserAuthenticatorTest {

	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private SpringSecurityUserAuthenticator userAuthenticator;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void ユーザIDに該当するユーザが存在する場合認証が成功する() {
		//given:ユーザは存在している
		//ユーザステータスは有効
		User user = UserTestFactory.create("TEST_USER");
		assertTrue(user.isActive());
		
		//依存オブジェクトの設定
		when(userRepository.exists(user.id())).thenReturn(true);
		when(userRepository.findById(user.id())).thenReturn(user);
		
		//when:ユーザIDを指定してテスト対象メソッドを実行する
		UserDetails userDetails = userAuthenticator.loadUserByUsername("TEST_USER");
		
		//then:認証されたユーザのIDが渡したIDと一致する
		assertEquals("TEST_USER", userDetails.getUsername());
	}
	
	@Test
	void ユーザIDに該当するユーザが存在しない場合は例外発生() {
		//given:ユーザは存在していない
		when(userRepository.exists(any())).thenReturn(false);
		
		//when:ユーザIDを指定してテスト対象メソッドを実行する
		Exception exception = assertThrows(UsernameNotFoundException.class, () -> userAuthenticator.loadUserByUsername("TEST_USER"));
		
		//then:想定した例外が発生している
		assertEquals("TEST_USERは存在しません。", exception.getMessage());
	}
	
	@Test
	void ユーザステータスが無効の場合は例外発生() {
		//given:ユーザステータスは無効
		User user = UserTestFactory.create("TEST_USER");
		user.disable();
		assertFalse(user.isActive());
		
		//ユーザは存在している
		when(userRepository.exists(user.id())).thenReturn(true);
		when(userRepository.findById(user.id())).thenReturn(user);
		
		//when:ユーザIDを指定してテスト対象メソッドを実行する
		Exception exception = assertThrows(RuntimeException.class, () -> userAuthenticator.loadUserByUsername(user.id().value()));
		
		//then:想定した例外が発生している
		assertEquals("ユーザが退会済みです。", exception.getMessage());
	}

}
