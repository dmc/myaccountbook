package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserStatus;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testfactory.UserTestFactory;

@SpringBootTest
@Transactional
class UserJooqRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Test
	void 保存したユーザを検索メソッドで取得できる() {
		//given:新規作成されたユーザ
		User createdUser = UserTestFactory.create();
		
		//when:ユーザを保存し、その後IDで検索して取得する
		userRepository.save(createdUser);
		User foundUser = userRepository.findById(createdUser.id());
		
		//then:保存したユーザをリポジトリ経由で取得できる
		assertAll(
			() -> assertEquals(createdUser.id(), foundUser.id()),
			() -> assertEquals(createdUser.mailAddress(), foundUser.mailAddress()),
			() -> assertEquals(createdUser.userStatus(), foundUser.userStatus())
		);
	}

	@Test
	void 保存されているユーザの情報を更新できる() {
		//given:既にユーザが保存されている
		User savedUser = userTestDataCreator.create();
		assertEquals(UserStatus.ACTIVE, savedUser.userStatus()); //この時点では有効なユーザである
		
		//when:ユーザを無効化して保存する
		savedUser.disable();
		userRepository.update(savedUser);
		
		//then:リポジトリ経由で取得したユーザのステータスが変更されている
		User foundUser = userRepository.findById(savedUser.id());
		assertEquals(UserStatus.INACTIVE, foundUser.userStatus()); //確かに無効化されている
	}
	
	@Test
	void 存在チェックメソッドはDBにユーザが保存されていればtrueを返す() {
		//given:この時点ではユーザは保存されていないのでfalseを返す
		User user = UserTestFactory.create();
		assertFalse(userRepository.exists(user.id()));
		
		//when:ユーザを保存する
		userRepository.save(user);
		
		//then:存在チェックメソッドがtrueを返す
		assertTrue(userRepository.exists(user.id()));
	}
	
	@Test
	void 存在しないユーザを取得しようとすると例外発生() {
		//given:ユーザID「TEST_USER」は存在しない
		
		//when:ユーザID「TEST_USER」を指定してfindByIdメソッドを呼び出す
		UserId userId = UserId.valueOf("TEST_USER");
		Exception exception = assertThrows(RuntimeException.class, () -> userRepository.findById(userId));
		
		//then:想定した例外が発生している
		assertEquals("指定したユーザは存在しません。", exception.getMessage());
	}
	
}
