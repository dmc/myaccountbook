package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.util.*;
import java.util.stream.Collectors;
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
	 * 仕訳明細の会計要素の貸借組み合わせをチェックする
	 * @return 全ての仕訳明細が貸借の組み合わせ条件を満たしている場合true
	 */
	boolean isCorrectCombination() {
		List<EntryDetail> debitDetails = elements.stream()
													 .filter(each -> each.isDebit())
													 .collect(Collectors.toList());
		List<EntryDetail> creditDetails = elements.stream()
													  .filter(each -> each.isCredit())
													  .collect(Collectors.toList());
		//借方の仕訳明細に対して、貸方の仕訳明細が組み合わせ可能かどうかを全て調べる
		for (EntryDetail debit : debitDetails) {
			for (EntryDetail credit : creditDetails) {
				if (! debit.canCombinate(credit)) return false;
			}
		}
		return true;
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
	
}
