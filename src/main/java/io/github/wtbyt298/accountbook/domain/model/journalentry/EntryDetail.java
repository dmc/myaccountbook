package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.util.*;
import java.util.stream.Collectors;

import io.github.wtbyt298.accountbook.domain.model.shared.Amount;

/**
 * 仕訳明細クラス
 * 明細行のコレクションオブジェクト
 */
public class EntryDetail {

	final List<DetailRow> detailRows;
	
	public EntryDetail(List<DetailRow> detailRows) {
		if (detailRows.stream().allMatch(each -> each.isDebit()) || detailRows.stream().allMatch(each -> each.isCredit())) {
			throw new IllegalArgumentException("貸借それぞれに少なくとも1件ずつの明細が必要です。");
		}
		this.detailRows = detailRows;
	}

	/**
	 * 明細行の会計要素の貸借組み合わせをチェックする
	 * @return 全ての明細行が貸借の組み合わせ条件を満たしている場合true
	 */
	boolean isCollectCombination() {
		List<DetailRow> debitRows = detailRows.stream()
											  .filter(each -> each.isDebit())
											  .collect(Collectors.toList());
		List<DetailRow> creditRows = detailRows.stream()
											  .filter(each -> each.isCredit())
											  .collect(Collectors.toList());
		//借方明細行に対して、貸方明細行が組み合わせ可能かどうかを全て調べる
		for (DetailRow debit : debitRows) {
			for (DetailRow credit : creditRows) {
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
		for (DetailRow each : detailRows) {
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
		for (DetailRow each : detailRows) {
			if (each.isDebit()) continue;
			total = total.plus(each.amount);
		}
		return total;
	}
	
}
