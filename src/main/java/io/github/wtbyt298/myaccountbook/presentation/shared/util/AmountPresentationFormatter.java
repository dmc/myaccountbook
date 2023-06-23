package io.github.wtbyt298.myaccountbook.presentation.shared.util;

import java.text.NumberFormat;

/**
 * 金額の表示形式を取り扱うクラス
 */
public class AmountPresentationFormatter {

	/**
	 * @return "0,000円"の形式の文字列
	 */
	public static String yen(int value) {
		return NumberFormat.getNumberInstance().format(value) + "円";
	}
	
}
