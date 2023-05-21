package io.github.wtbyt298.accountbook.presentation.controller.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * 全ての画面で共有するModelAttributeを生成するコントローラクラス
 */
@ControllerAdvice
public class ModelAttributeUtilityController {

	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * ログイン中のユーザ名を返す
	 */
	@ModelAttribute("loginUser")
	public String loginUser() {
		UserSession userSession = userSessionProvider.getUserSession();
		return userSession.userId().toString();
	}
	
}
