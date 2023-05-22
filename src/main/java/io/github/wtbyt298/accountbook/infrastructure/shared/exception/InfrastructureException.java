package io.github.wtbyt298.accountbook.infrastructure.shared.exception;

/**
 * インフラ層で投げる例外クラス
 * プレゼンテーション層の例外ハンドラでキャッチする
 */
public class InfrastructureException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorMessage;

	public InfrastructureException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return errorMessage;
	}
	
}
