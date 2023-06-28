package wtbyt298.myaccountbook.helper.testfactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import wtbyt298.myaccountbook.domain.model.journalentry.DealDate;
import wtbyt298.myaccountbook.domain.model.journalentry.Description;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetails;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import wtbyt298.myaccountbook.domain.shared.types.LoanType;

/**
 * テスト用の仕訳インスタンスを生成するクラス
 */
public class JournalEntryTestFactory {
	
	private final DealDate dealDate;
	private final Description description;
	private final EntryDetails entryDetails;
	
	private JournalEntryTestFactory(Builder builder) {
		this.dealDate = builder.dealDate;
		this.description = builder.description;
		
		//デフォルトでは貸借それぞれ1件ずつの明細をセットする
		//明細が追加されていれば、それを使う
		if (builder.elements.isEmpty()) {
			List<EntryDetail> defaultElements = new ArrayList<>();
			defaultElements.add(EntryDetailTestFactory.create("401", "0", LoanType.DEBIT, 1000));
			defaultElements.add(EntryDetailTestFactory.create("101", "0", LoanType.CREDIT, 1000));
			this.entryDetails = new EntryDetails(defaultElements);
		} else {
			this.entryDetails = new EntryDetails(builder.elements);
		}
	}
	
	private JournalEntry create() {
		return JournalEntry.create(dealDate, description, entryDetails);
	}
	
	public static class Builder {
		
		private DealDate dealDate = DealDate.valueOf(LocalDate.now());
		private Description description = Description.valueOf("テスト用に作成した仕訳です。");
		private List<EntryDetail> elements;
		
		public Builder() {
			this.elements = new ArrayList<>();
		}
		
		public JournalEntry build() {
			return new JournalEntryTestFactory(this).create();
		}
		
		public Builder dealDate(LocalDate dete) {
			this.dealDate = DealDate.valueOf(dete);
			return this;
		}
		
		public Builder description(String description) {
			this.description = Description.valueOf(description);
			return this;
		}
		
		public Builder addDetail(String parentId, String subId, LoanType loanType, int amount) {
			this.elements.add(EntryDetailTestFactory.create(parentId, subId, loanType, amount));
			return this;
		}
		
	}
	
}
