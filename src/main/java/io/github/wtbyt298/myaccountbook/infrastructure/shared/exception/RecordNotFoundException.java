package io.github.wtbyt298.myaccountbook.infrastructure.shared.exception;

/**
 * DBからレコードを取得できなかった場合に投げる例外クラス
 */
public class RecordNotFoundException extends InfrastructureException {

	private static final long serialVersionUID = 1L;

	public RecordNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
}
