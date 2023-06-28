package wtbyt298.myaccountbook.domain.model.budget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import wtbyt298.myaccountbook.domain.shared.exception.DomainException;

/**
 * 月次予算クラス
 */
public class MonthlyBudget {

	private static final int MIN = 0;
	private static final int MAX = 1_000_000;
	private final int value;
	
	private MonthlyBudget(int value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 */
	public static MonthlyBudget valueOf(int value) {
		if (value < MIN) {
			throw new DomainException("予算は" + MIN + "以上で設定してください。");
		}
		if (value > MAX) {
			throw new DomainException("予算は" + MAX + "以下で設定してください。");
		}
		return new MonthlyBudget(value);
	}
	
	/**
	 * 引数に与えた数値が予算に占める比率を返す
	 * @return 実績値 / 予算
	 */
	public BigDecimal ratioOfBudget(int amount) {
		//予算額がゼロの場合は計算を行わず、0を返す
		if (value == 0) return BigDecimal.ZERO;
		
		BigDecimal budget = BigDecimal.valueOf(value);
		BigDecimal actual = BigDecimal.valueOf(amount);
		
		return actual.divide(budget, 4, RoundingMode.HALF_UP);
	}
	
	/**
	 * 予算額が0かどうかを返す
	 */
	public boolean isZero() {
		return value == 0;	
	}
	
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
		if (! (obj instanceof MonthlyBudget)) return false;
		MonthlyBudget other = (MonthlyBudget) obj;
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

}
