package io.github.wtbyt298.myaccountbook.application.shared.usersession;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.wtbyt298.myaccountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.myaccountbook.domain.model.user.User;
import io.github.wtbyt298.myaccountbook.domain.model.user.UserId;
import io.github.wtbyt298.myaccountbook.domain.model.user.UserRepository;

/**
 * ユーザ認証処理クラス
 */
@Service
public class SpringSecurityUserAuthenticator implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * SpringSecurityの仕組みを利用して認証を行う
	 * @param ユーザID
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//ユーザが見つからない場合の処理はSpringSecurityの仕組みにより自動的に行われる
		UserId userId = UserId.valueOf(username);
		if (! userRepository.exists(userId)) {
			throw new UsernameNotFoundException(username + "は存在しません。");
		}
		
		User user = userRepository.findById(userId);
		if (! user.isActive()) {
			throw new UseCaseException("ユーザが退会済みです。");
		}
		
		return new org.springframework.security.core.userdetails.User(
			user.id().value(), 
			user.password().value(), 
			new ArrayList<>() //ROLEの設定は行わない
		);
	}

}