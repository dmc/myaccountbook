package io.github.wtbyt298.accountbook.domain.model.journalentry;

import io.github.wtbyt298.accountbook.domain.model.shared.Amount;

/**
 * 仕訳伝票クラス
 * このクラスは仕訳集約の集約ルートとして機能する
 */
public class JournalEntry {

	private final EntryId entryId;         //仕訳番号
	private final DealDate dealDate;       //取引日
	private final Description description; //摘要
	private final EntryDetail entryDetail; //仕訳明細
	
	private JournalEntry(EntryId entryId, DealDate dealDate, Description description, EntryDetail entryDetail) {
		if (! entryDetail.isSameTotal()) {
			throw new IllegalArgumentException("明細の貸借合計が一致していません。");
		}
		if (! entryDetail.isCollectCombination()) {
			throw new IllegalArgumentException("明細の貸借組み合わせが正しくありません。");
		}
		this.entryId = entryId;
		this.dealDate = dealDate;
		this.description = description;
		this.entryDetail = entryDetail;
	}
	
	/**
	 * 新規作成用のファクトリメソッド
	 */
	public static JournalEntry create(DealDate dealDate, Description description, EntryDetail entryDetail) {
		return new JournalEntry(EntryId.newInstance(), dealDate, description, entryDetail);
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static JournalEntry reconstruct(EntryId entryId, DealDate dealDate, Description description, EntryDetail entryDetail) {
		return new JournalEntry(entryId, dealDate, description, entryDetail);
	}
	
	/**
	 * @return 仕訳合計金額
	 */
	public Amount totalAmount() {
		return entryDetail.debitSum(); //貸借で金額は一致するので借方合計を返している
	}
	
	/**
	 * 仕訳伝票の内部データを永続化用のDTOのビルダーに通知する
	 */
	public void notifyState(JournalEntryNotification note) {
		note.entryId(entryId.value);
		note.dealDate(dealDate.value);
		note.description(description.value);
		note.fiscalYearMonth(dealDate.yearMonth());
	}
	
	/**
	 * 仕訳明細の内部データを永続化用のDTOのビルダーに通知する
	 */
	public void notifyState(EntryDetailNotification note) {
		for (DetailRow row : entryDetail.detailRows) {
			note.entryId(entryId.value);
			note.accountTitleId(row.accountTitleId.toString());
			note.subAccountTitleId(row.subAccountTitleId.toString());
			note.loanType(row.detailLoanType.toString());
			note.amount(row.amount.value());
		}
	}
	
}
