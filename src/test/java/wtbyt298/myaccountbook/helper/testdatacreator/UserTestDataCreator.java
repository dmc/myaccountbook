package wtbyt298.myaccountbook.helper.testdatacreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wtbyt298.myaccountbook.domain.model.user.User;
import wtbyt298.myaccountbook.domain.model.user.UserRepository;
import wtbyt298.myaccountbook.helper.testfactory.UserTestFactory;

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
	
	public User create(String userId) {
		User user = UserTestFactory.create(userId);
		userRepository.save(user);
		
		return user;
	}
	
}
