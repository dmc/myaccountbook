package io.github.wtbyt298.accountbook.domain.shared.exception;

/**
 * ドメイン層で投げる例外クラス
 * プレゼンテーション層の例外ハンドラでキャッチする
 */
public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DomainException(String errorMessage) {
		super(errorMessage);
	}
	
}
