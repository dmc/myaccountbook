package wtbyt298.myaccountbook.domain.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;

/**
 * 仕訳の仕様を定めるクラス
 * 仕訳インスタンスの生成時の整合性チェックを行う
 */
@Component
public class JournalEntrySpecification {
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;

	/**
	 * 仕訳明細の勘定科目の貸借組み合わせをチェックする
	 * @return 全ての仕訳明細が貸借の組み合わせ条件を満たしている場合true
	 */
	public boolean isSatisfied(JournalEntry entry) {
		List<AccountTitle> debitAccountTitles = fetchDebitAccountTitles(entry.entryDetails());
		List<AccountTitle> creditAccountTitles = fetchCreditAccountTitles(entry.entryDetails());

		return debitAccountTitles.stream()
			.allMatch(each -> ok(each, creditAccountTitles));
	}
	
	/**
	 * 借方の勘定科目に対して、貸方の勘定科目が組み合わせ可能かどうかを全て調べる
	 * @param debit 借方勘定科目
	 * @param creditAccountTitles 貸方勘定科目のリスト
	 * @return 全て組み合わせ可能な場合true
	 */
	private boolean ok(AccountTitle debit, List<AccountTitle> creditAccountTitles) {
		return creditAccountTitles.stream().allMatch(each -> debit.canCombineWith(each));
	}
	
	/**
	 * 仕訳明細の借方の勘定科目のみを取り出したリストを返す
	 */
	private List<AccountTitle> fetchDebitAccountTitles(List<EntryDetail> elements) {
		return elements.stream()
	       .filter(each -> each.isDebit())
	       .map(each -> accountTitleRepository.findById(each.accountTitleId()))
	       .toList();
	}
	
	/**
	 * 仕訳明細の貸方の勘定科目のみを取り出したリストを返す
	 */
	private List<AccountTitle> fetchCreditAccountTitles(List<EntryDetail> elements) {
		return elements.stream()
	       .filter(each -> each.isCredit())
	       .map(each -> accountTitleRepository.findById(each.accountTitleId()))
	       .toList();
	}
	
}
