package io.github.wtbyt298.myaccountbook.presentation.shared.usersession;

import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;

/**
 * ユーザ認証情報を提供するインタフェース
 */
public interface UserSessionProvider {

	UserSession getUserSession();
	
}
