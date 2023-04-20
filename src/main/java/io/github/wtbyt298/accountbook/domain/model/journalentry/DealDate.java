package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 取引日クラス
 */
public class DealDate {

	final LocalDate value;
	
	private DealDate(LocalDate value) {
		this.value = value;
	}
	
	public static DealDate valueOf(LocalDate value) {
		return new DealDate(value);
	}
	
	/**
	 * yyyyMM形式の文字列を返す
	 */
	String yearMonth() {
		return value.format(DateTimeFormatter.ofPattern("yyyyMM"));
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
}
