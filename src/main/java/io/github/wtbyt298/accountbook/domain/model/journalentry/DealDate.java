package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 取引日クラス
 */
public class DealDate {

	final LocalDate value;
	
	private DealDate(LocalDate value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 */
	public static DealDate valueOf(LocalDate value) {
		return new DealDate(value);
	}
	
	/**
	 * yyyyMM形式の文字列を返す
	 */
	String yearMonth() {
		return value.format(DateTimeFormatter.ofPattern("yyyyMM"));
	}
	
	public LocalDate value() {
		return value;
	}
	
	@Override
	public String toString() {
		return "取引日：" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof DealDate)) return false;
		DealDate other = (DealDate) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}
