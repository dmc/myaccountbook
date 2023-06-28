package com.github.wtbyt298.myaccountbook.domain.model.journalentry;

import java.time.LocalDate;
import java.time.YearMonth;
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
	 * 年月に変換する
	 */
	YearMonth toYearMonth() {
		return YearMonth.of(value.getYear(), value.getMonth());
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
