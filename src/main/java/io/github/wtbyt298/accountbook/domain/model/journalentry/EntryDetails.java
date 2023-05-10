package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.util.*;
import io.github.wtbyt298.accountbook.domain.shared.exception.DomainException;

/**
 * 仕訳明細のコレクションオブジェクト
 */
public class EntryDetails {

	final List<EntryDetail> elements;
	
	public EntryDetails(List<EntryDetail> elements) {
		if (elements.stream().allMatch(each -> each.isDebit()) || elements.stream().allMatch(each -> each.isCredit())) {
			throw new DomainException("貸借それぞれに少なくとも1件ずつの明細が必要です。");
		}
		this.elements = elements;
	}
	
	/**
	 * @return 借方合計と貸方合計が一致している場合true
	 */
	boolean isSameTotal() {
		return debitSum().equals(creditSum());
	}
	
	/**
	 * @return 借方合計金額
	 */
    Amount debitSum() {
		Amount total = Amount.valueOf(0);
		for (EntryDetail each : elements) {
			if (each.isCredit()) continue;
			total = total.plus(each.amount);
		}
		return total;
	}
	
	/**
	 * @return 貸方合計金額
	 */
	Amount creditSum() {
		Amount total = Amount.valueOf(0);
		for (EntryDetail each : elements) {
			if (each.isDebit()) continue;
			total = total.plus(each.amount);
		}
		return total;
	}
	
	/**
	 * 仕訳明細の貸借区分を反転させる
	 */
	EntryDetails transpose() {
		List<EntryDetail> transposed = new ArrayList<>();
		for (EntryDetail each : elements) {
			transposed.add(each.transposeLoanType());
		}
		return new EntryDetails(transposed);
	}
	
}
