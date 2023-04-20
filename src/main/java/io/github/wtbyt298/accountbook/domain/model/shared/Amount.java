package io.github.wtbyt298.accountbook.domain.model.shared;

import java.util.Objects;

/**
 * 金額クラス
 */
public class Amount implements Comparable<Amount> {
	
	private static final int MIN = 0;
	private final int value;
	
	private Amount (int value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 * 金額は0以上とする（例えば、食費：マイナス100円　のような場合はありえないため）
	 */
	public static Amount valueOf(int value) {
		if (value < MIN) {
			throw new IllegalArgumentException("金額にはゼロまたは正の数を指定してください。");
		}
		return new Amount(value);
	}
	
	/**
	 * 保持している金額を返す
	 * 永続化の際を除いてむやみに使ってはならない
	 */
	public int value() {
		return this.value;
	}
	
	/**
	 * 金額を加算する
	 */
	public Amount plus(Amount other) {
		final int result = this.value + other.value;
		return new Amount(result);
	}
	
	/**
	 * 金額を減算する
	 * 結果が負になる場合は例外発生
	 */
	public Amount minus(Amount other) {
		if (! canMinus(other)) {
			throw new IllegalArgumentException("結果が負の値となるため計算できません。");
		}
		final int result = this.value - other.value;
		return new Amount(result);
	}
	
	/**
	 * 減算結果が0以上かどうかを判断する
	 */
	private boolean canMinus(Amount other) {
		return (this.value - other.value) >= MIN;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof Amount)) return false;
		Amount other = (Amount) obj;
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
	@Override
	public int compareTo(Amount other) {
		return Integer.compare(this.value, other.value);
	}

}
