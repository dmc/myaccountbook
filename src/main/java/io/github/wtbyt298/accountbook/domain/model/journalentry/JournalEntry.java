package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 仕訳伝票クラス
 * このクラスは仕訳集約の集約ルートとして機能する
 */
public class JournalEntry {

	private final EntryId entryId;         //仕訳番号
	private final DealDate dealDate;       //取引日
	private final Description description; //摘要
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
			throw new IllegalArgumentException("明細の貸借合計が一致していません。");
		}
		if (! entryDetails.isCollectCombination()) {
			throw new IllegalArgumentException("明細の貸借組み合わせが正しくありません。");
		}
		return new JournalEntry(EntryId.newInstance(), dealDate, description, entryDetails);
	}
	
	//再構築用のメソッドは必要になったら実装する
	//現状、内部状態の変更はせず、参照時は参照用のモデルを使っているため
	
	/**
	 * @return 仕訳合計金額
	 */
	public Amount totalAmount() {
		return entryDetails.debitSum(); //貸借で金額は一致するので借方合計を返している
	}
	
	/**
	 * @return 会計年月
	 */
	public String fiscalYearMonth() {
		return dealDate.yearMonth();
	}
	
	 //以下、永続化用のメソッド定義
	 //※リポジトリクラスで内部データの取得のために呼び出す以外には極力使用しない
	 
	/**
	 * @return 仕訳ID
	 */
	public String id() {
		return entryId.value;
	}
	
	/**
	 * @return 取引日
	 */
	public LocalDate dealDate() {
		return dealDate.value;
	}
	
	/**
	 * @return 摘要
	 */
	public String description() {
		return description.value;
	}
	
	/**
	 * @return 明細行のリスト
	 */
	public List<EntryDetail> entryDetails() {
		return Collections.unmodifiableList(entryDetails.entryDetails);
	}
	
}
