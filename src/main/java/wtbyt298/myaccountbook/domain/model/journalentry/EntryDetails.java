package wtbyt298.myaccountbook.domain.model.journalentry;

import java.util.*;

import wtbyt298.myaccountbook.domain.shared.exception.CannotCreateJournalEntryException;

/**
 * 仕訳明細のコレクションオブジェクト
 */
public class EntryDetails {

	final List<EntryDetail> elements;
	
	public EntryDetails(List<EntryDetail> elements) {
		if (elements.stream().allMatch(each -> each.isDebit()) || elements.stream().allMatch(each -> each.isCredit())) {
			throw new CannotCreateJournalEntryException("貸借それぞれに少なくとも1件ずつの明細が必要です。");
		}
		this.elements = elements;
	}
	
	/**
	 * @return 仕訳合計金額
	 */
	Amount totalAmount() {
		return debitSum();
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
    private Amount debitSum() {
    	final int total = elements.stream()
    		.filter(each -> each.isDebit())
    		.mapToInt(each -> each.amount().value())
    		.sum();
    	
		return Amount.valueOf(total);
	}
	
	/**
	 * @return 貸方合計金額
	 */
	private Amount creditSum() {
    	final int total = elements.stream()
    		.filter(each -> each.isCredit())
    		.mapToInt(each -> each.amount().value())
    		.sum();
    	
		return Amount.valueOf(total);
	}
	
	/**
	 * 仕訳明細の貸借区分を反転させる
	 */
	EntryDetails transpose() {
		List<EntryDetail> transposed = elements.stream()
			.map(each -> each.transposeLoanType())
			.toList();
		
		return new EntryDetails(transposed);
	}
	
}
