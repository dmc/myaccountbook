package io.github.wtbyt298.accountbook.application.usecase.user;

import lombok.Getter;

/**
 * ユーザ作成用のDTOクラス
 */
@Getter
public class CreateUserCommand {
	
	private final String id;
	private final String password;
	private final String mailAddress;
	
	public CreateUserCommand(String id, String password, String mailAddress) {
		this.id = id;
		this.password = password;
		this.mailAddress = mailAddress;
	}
	
}
