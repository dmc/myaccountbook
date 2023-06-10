package io.github.wtbyt298.accountbook.presentation.shared.usersession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import lombok.Getter;

@Component
public class SpringSecurityUserSessionProvider implements UserSessionProvider {

	/**
	 * 認証済みのユーザ情報を返す
	 */
	@Override
	public UserSession getUserSession() {
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		UserSession userSession = new SpringSecurityUserSession(currentUser);
		
		return userSession;
	}
	
	/**
	 * 認証済みのユーザIDを保持するクラス
	 */
	@Getter
	private class SpringSecurityUserSession implements UserSession {
		
		private static final String ANONYMOUS = "anonymousUser";
		private final String value;
		
		public SpringSecurityUserSession(String value) {
			this.value = value;
		}
		
		@Override
		public UserId userId() {
			return UserId.valueOf(value);
		}
		
		@Override
		public boolean isEmpty() {
			return value.equals(ANONYMOUS);
		}
		
	}

}
