package io.github.wtbyt298.accountbook.domain.model.shared.exception;

/**
 * ドメイン層で投げる例外クラス
 * プレゼンテーション層の例外ハンドラでキャッチする
 */
public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorMessage;
	
	public DomainException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
