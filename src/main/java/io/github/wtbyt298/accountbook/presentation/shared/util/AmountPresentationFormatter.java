package io.github.wtbyt298.accountbook.presentation.shared.util;

/**
 * 金額の表示形式を取り扱うクラス
 */
public class AmountPresentationFormatter {

	/**
	 * @return "0,000円"の形式の文字列
	 */
	public static String yen(int value) {
		return String.format("%,d", value) + "円";
	}
	
}
