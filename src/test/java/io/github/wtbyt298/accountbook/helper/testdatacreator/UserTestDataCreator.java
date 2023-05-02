package io.github.wtbyt298.accountbook.helper.testdatacreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;
import io.github.wtbyt298.accountbook.helper.testfactory.UserTestFactory;

/**
 * DBにテスト用のユーザデータを作成するクラス
 */
@Component
public class UserTestDataCreator {

	@Autowired
	private UserRepository userRepository;
	
	public User create() {
		User user = UserTestFactory.create();
		userRepository.save(user);
		return user;
	}
	
}
