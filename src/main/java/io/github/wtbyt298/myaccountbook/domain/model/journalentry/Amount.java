package io.github.wtbyt298.myaccountbook.domain.model.journalentry;

import java.util.Objects;

import io.github.wtbyt298.myaccountbook.domain.shared.exception.DomainException;

/**
 * 仕訳金額クラス
 */
public class Amount {
	
	private static final int MIN = 0;
	final int value;
	
	private Amount (int value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 * 金額は0以上とする（例えば、食費：マイナス100円　のような場合はありえないため）
	 */
	public static Amount valueOf(int value) {
		if (value < MIN) {
			throw new DomainException("金額には" + MIN + "以上の数値を指定してください。");
		}
		return new Amount(value);
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
			throw new DomainException("結果が負の値となるため計算できません。");
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
	
	/**
	 * @return 金額
	 */
	public int value() {
		return value;
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
		return Objects.equals(this.value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

}
