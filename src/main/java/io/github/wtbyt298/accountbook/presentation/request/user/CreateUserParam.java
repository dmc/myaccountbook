package io.github.wtbyt298.accountbook.presentation.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ユーザ登録画面のフォームクラス
 */
@Data
public class CreateUserParam {
	
	@NotBlank(message = "ユーザIDを入力してください。")
	private String id;
	
	@NotBlank(message = "パスワードを入力してください。")
	private String password;
	
	@NotBlank(message = "メールアドレスを入力してください。")
	@Email(message = "メールアドレスの形式が正しくありません。")
	private String mailAddress;
	
}
