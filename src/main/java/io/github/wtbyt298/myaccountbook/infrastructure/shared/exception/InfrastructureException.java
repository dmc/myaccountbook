package io.github.wtbyt298.myaccountbook.infrastructure.shared.exception;

/**
 * インフラ層で投げる例外クラス
 * プレゼンテーション層の例外ハンドラでキャッチする
 */
public class InfrastructureException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InfrastructureException(String errorMessage) {
		super(errorMessage);
	}
	
}
