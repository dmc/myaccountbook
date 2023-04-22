package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
	
	//再構築用のメソッドは必要になったら実装する
	//現状、内部状態の変更はせず、参照時は参照用のモデルを使っているため
	
	/**
	 * @return 仕訳合計金額
	 */
	public Amount totalAmount() {
		return entryDetail.debitSum(); //貸借で金額は一致するので借方合計を返している
	}
	
	/**
	 * @return 会計年月
	 */
	public String fiscalYearMonth() {
		return dealDate.yearMonth();
	}
	
	/**
	 * 以下、永続化用のメソッド定義
	 * ※リポジトリクラスで内部データの取得のために呼び出す以外には使用しない
	 */
	public String id() {
		return entryId.value;
	}
	
	public LocalDate dealDate() {
		return dealDate.value;
	}
	
	public String description() {
		return description.value;
	}
	
	public List<DetailRow> entryDetail() {
		return Collections.unmodifiableList(entryDetail.detailRows);
	}
	
}
