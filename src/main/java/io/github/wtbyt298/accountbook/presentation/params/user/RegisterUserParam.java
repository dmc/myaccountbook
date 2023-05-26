package io.github.wtbyt298.accountbook.presentation.params.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ユーザ登録画面のフォームクラス
 */
@Data
public class RegisterUserParam {
	
	@NotBlank(message = "ユーザIDを入力してください。")
	private String id;
	
	@NotBlank(message = "パスワードを入力してください。")
	private String password;
	
	@NotBlank(message = "メールアドレスを入力してください。")
	private String mailAddress;
	
}
