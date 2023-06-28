package wtbyt298.myaccountbook.application.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wtbyt298.myaccountbook.application.shared.exception.UseCaseException;
import wtbyt298.myaccountbook.domain.model.user.User;
import wtbyt298.myaccountbook.domain.model.user.UserId;
import wtbyt298.myaccountbook.domain.model.user.UserRepository;

/**
 * ユーザ退会処理クラス
 */
@Service
public class DisableUserUseCase {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * ユーザステータスを有効から無効に変更する
	 * @param userId 対象ユーザのユーザID
	 */
	public void execute(UserId userId) {
		if (! userRepository.exists(userId)) {
			throw new UseCaseException("指定したユーザは存在しません。");
		}
		
		//ドメインオブジェクトを生成
		User user = userRepository.findById(userId);
		
		//ユーザを無効化する
		user.disable();
		
		//リポジトリに保存する
		userRepository.update(user);
	}
	
}
