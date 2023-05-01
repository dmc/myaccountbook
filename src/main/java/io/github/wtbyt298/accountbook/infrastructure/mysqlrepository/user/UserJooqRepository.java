package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.user;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.tables.Users.*;
import io.github.wtbyt298.accountbook.domain.model.user.EncodedUserPassword;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserStatus;

/**
 * ユーザのリポジトリクラス
 * ユーザ集約の永続化と再構築の詳細を記述する
 */
@Repository
public class UserJooqRepository implements UserRepository {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * ユーザを保存する
	 */
	@Override
	public void save(User user) {
		jooq.insertInto(USERS)
			.set(USERS.USER_ID, user.id().value())
			.set(USERS.HASHED_PASSWORD, user.password().value())
			.set(USERS.MAIL_ADDRESS, user.mailAddress())
			.set(USERS.USER_STATUS, user.userStatus().toString())
			.execute();
	}
	
	/**
	 * ユーザ情報を更新する
	 */
	@Override
	public void update(User user) {
		jooq.update(USERS)
			.set(USERS.USER_ID, user.id().value())
			.set(USERS.HASHED_PASSWORD, user.password().value())
			.set(USERS.MAIL_ADDRESS, user.mailAddress())
			.set(USERS.USER_STATUS, user.userStatus().toString())
			.execute();
	}
	
	/**
	 * IDに一致するユーザを取得する
	 */
	@Override
	public User findById(UserId userId) {
		Record result = jooq.select()
							.from(USERS)
							.where(USERS.USER_ID.eq(userId.value()))
							.fetchOne();
		User user = mapRecordToEntity(result);
		return user;
	}

	/**
	 * ユーザが存在するかどうかを判断する
	 */
	@Override
	public boolean exists(UserId userId) {
		final int resultCount = jooq.select()
									.from(USERS)
									.where(USERS.USER_ID.eq(userId.value()))
									.execute();
		if (resultCount >= 1) return true;
		return false;
	}
	
	/**
	 * ユーザのインスタンスを組み立てる
	 */
	private User mapRecordToEntity(Record record) {
		UserId userId = UserId.valueOf(record.get(USERS.USER_ID));
		EncodedUserPassword encodedUserPassword = EncodedUserPassword.valueOf(record.get(USERS.HASHED_PASSWORD));
		String mailAddress = record.get(USERS.MAIL_ADDRESS);
		UserStatus userStatus = UserStatus.valueOf(record.get(USERS.USER_STATUS));
		return User.reconstruct(
			userId,
			encodedUserPassword,
			mailAddress,
			userStatus
		);
	}

}
