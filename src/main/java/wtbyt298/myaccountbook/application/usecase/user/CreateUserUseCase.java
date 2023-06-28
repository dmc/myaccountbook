package wtbyt298.myaccountbook.application.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wtbyt298.myaccountbook.application.shared.exception.UseCaseException;
import wtbyt298.myaccountbook.domain.model.user.EncodedUserPassword;
import wtbyt298.myaccountbook.domain.model.user.User;
import wtbyt298.myaccountbook.domain.model.user.UserId;
import wtbyt298.myaccountbook.domain.model.user.UserRepository;

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
			throw new UseCaseException("同一IDのユーザが既に存在しています。");
		}
		
		//ドメインオブジェクトを生成
		User user = mapCommandToEntity(command);
		
		//リポジトリに保存する
		userRepository.save(user);
	}
	
	/**
	 * ユーザのインスタンスを組み立てる
	 */
	private User mapCommandToEntity(CreateUserCommand command) {
		UserId userId = UserId.valueOf(command.getId());
		EncodedUserPassword userPassword = EncodedUserPassword.fromRawPassword(command.getPassword());
		String mailAddress = command.getMailAddress();
		
		return User.create(userId, userPassword, mailAddress);		
	}
	
}
