package io.github.wtbyt298.accountbook.application.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.model.user.EncodedUserPassword;
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;

/**
 * ユーザ作成処理クラス
 */
@Service
public class CreateUserUseCase {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * ユーザを作成する
	 * @param ユーザ作成用のDTO
	 */
	@Transactional
	public void execute(CreateUserCommand command) {
		UserId userId = UserId.valueOf(command.getId());
		if (userRepository.exists(userId)) {
			throw new IllegalArgumentException("既にユーザが存在しています。");
		}
		User user = createFromCommand(command);
		userRepository.save(user);
	}
	
	/**
	 * ユーザのインスタンスを組み立てる
	 */
	private User createFromCommand(CreateUserCommand command) {
		UserId userId = UserId.valueOf(command.getId());
		EncodedUserPassword userPassword = EncodedUserPassword.fromRawPassword(command.getPassword());
		String mailAddress = command.getMailAddress();
		return User.create(
			userId, 
			userPassword, 
			mailAddress
		);		
	}
	
}
