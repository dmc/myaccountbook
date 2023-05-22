package io.github.wtbyt298.accountbook.application.shared.exception;

/**
 * アプリケーション層で投げる例外クラス
 * プレゼンテーション層の例外ハンドラでキャッチする
 */
public class UseCaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public UseCaseException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return errorMessage;
	}

	
	
}
