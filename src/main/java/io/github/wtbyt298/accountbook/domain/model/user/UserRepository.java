package io.github.wtbyt298.accountbook.domain.model.user;

/**
 * ユーザ集約のリポジトリクラス
 * 実装クラスはインフラ層に置く
 */
public interface UserRepository {
	
	void save(User user);
	
	void update(User user);
	
	User findById(UserId userId);
	
	boolean exists(UserId userId);
	
}
