package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import io.github.wtbyt298.accountbook.domain.shared.exception.CannotCreateJournalEntryException;

/**
 * 仕訳伝票クラス
 * このクラスは仕訳集約の集約ルートとして機能する
 */
public class JournalEntry {

	private final EntryId entryId;           //仕訳ID
	private final DealDate dealDate;         //取引日
	private final Description description;   //摘要
	private final EntryDetails entryDetails; //仕訳明細のコレクション
	
	private JournalEntry(EntryId entryId, DealDate dealDate, Description description, EntryDetails entryDetails) {
		this.entryId = entryId;
		this.dealDate = dealDate;
		this.description = description;
		this.entryDetails = entryDetails;
	}
	
	/**
	 * 新規作成用のファクトリメソッド
	 */
	public static JournalEntry create(DealDate dealDate, Description description, EntryDetails entryDetails) {
		if (! entryDetails.isSameTotal()) {
			throw new CannotCreateJournalEntryException("明細の貸借合計が一致していません。");
		}
		return new JournalEntry(EntryId.newInstance(), dealDate, description, entryDetails);
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static JournalEntry reconstruct(EntryId enetryId, DealDate dealDate, Description description, EntryDetails entryDetails) {
		return new JournalEntry(enetryId, dealDate, description, entryDetails);
	}	
	
	/**
	 * 明細の貸借を逆にした仕訳を生成する
	 */
	public JournalEntry toReversingJournalEntry() {
		return new JournalEntry(entryId, dealDate, description, entryDetails.transpose());
	}
		
	/**
	 * @return 仕訳合計金額
	 */
	public Amount totalAmount() {
		return entryDetails.debitSum(); //貸借で金額は一致するので借方合計を返している
	}
	
	/**
	 * @return 会計年月
	 */
	public YearMonth fiscalYearMonth() {
		return dealDate.toYearMonth();
	}
	
	/**
	 * @return 仕訳ID
	 */
	public EntryId id() {
		return entryId;
	}
	
	/**
	 * @return 取引日
	 */
	public DealDate dealDate() {
		return dealDate;
	}
	
	/**
	 * @return 摘要
	 */
	public Description description() {
		return description;
	}
	
	/**
	 * @return 明細行のリスト
	 */
	public List<EntryDetail> entryDetails() {
		return Collections.unmodifiableList(entryDetails.elements); //外部から変更できないように不変にする
	}
	
	@Override
	public String toString() {
		return entryId.toString() + " " +  dealDate.toString() + " " + description.toString();
	}
	
}
