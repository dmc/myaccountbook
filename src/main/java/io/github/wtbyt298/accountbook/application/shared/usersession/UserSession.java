package io.github.wtbyt298.accountbook.application.shared.usersession;

import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * ユーザ認証情報を表すインタフェース
 * 実装クラスはプレゼン層に置く
 */
public interface UserSession {
	
	UserId userId();
	
	boolean isAuthenticated();
	
}
