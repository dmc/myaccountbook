package io.github.wtbyt298.accountbook.presentation.shared.usersession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import lombok.Getter;

@Component
public class SpringSecurityUserSessionProvider implements UserSessionProvider {

	/**
	 * ログインしているユーザの情報を返す
	 */
	@Override
	public UserSession getUserSession() {
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		UserSession userSession = new SpringSecurityUserSession(currentUser);
		return userSession;
	}
	
	@Getter
	private class SpringSecurityUserSession implements UserSession {
		
		private final String userId;

		public SpringSecurityUserSession(String userId) {
			this.userId = userId;
		}
		
		public String userId() {
			return userId;
		}
		
	}

}
