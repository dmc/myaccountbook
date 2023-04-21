package io.github.wtbyt298.accountbook.domain.model.journalentry;

/**
 * 仕訳明細の永続化用の通知オブジェクト 
 * 実装クラスはインフラ層に置く
 */
public interface EntryDetailNotification {

	void entryId(String entryId);
	void accountTitleId(String accountTitleId);
	void subAccountTitleId(String subAccountTitleId);
	void loanType(String loanType);	
	void amount(int amount);
	
}
