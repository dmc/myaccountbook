package com.github.wtbyt298.myaccountbook.application.shared.exception;

/**
 * ユースケース内で投げる例外クラス
 * プレゼンテーション層の例外ハンドラでキャッチする
 */
public class UseCaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UseCaseException(String errorMessage) {
		super(errorMessage);
	}
	
}
