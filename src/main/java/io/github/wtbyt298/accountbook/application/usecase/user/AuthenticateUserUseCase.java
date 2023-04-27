package io.github.wtbyt298.accountbook.application.usecase.user;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.model.user.UserRepository;

/**
 * ユーザ認証処理クラス
 */
@Service
public class AuthenticateUserUseCase implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * SpringSecurityの仕組みを利用して認証を行う
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserId userId = UserId.valueOf(username);
		if (! userRepository.exists(userId)) {
			throw new UsernameNotFoundException(username);
		}
		User user = userRepository.findById(userId);
		return new org.springframework.security.core.userdetails.User(
			user.id(), 
			user.encodedPassword(), 
			new ArrayList<>() //ROLEの設定は行わない
		);
	}

}