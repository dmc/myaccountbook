package io.github.wtbyt298.accountbook.domain.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;

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
		List<EntryDetail> debitDetails = filterToDebitDetails(entry.entryDetails());
		List<EntryDetail> creditDetails = filterToCreditDetails(entry.entryDetails());
		//借方の勘定科目に対して、貸方の勘定科目が組み合わせ可能かどうかを全て調べる
		for (EntryDetail debit : debitDetails) {
			for (EntryDetail credit : creditDetails) {
				AccountTitle debitAccountTitle = accountTitleRepository.findById(debit.accountTitleId());
				AccountTitle creditAccountTitle = accountTitleRepository.findById(credit.accountTitleId());
				if (! debitAccountTitle.canCombineWith(creditAccountTitle)) return false;
			}
		}
		return true;
	}
	
	private List<EntryDetail> filterToDebitDetails(List<EntryDetail> elements) {
		return elements.stream()
				       .filter(each -> each.isDebit())
					   .collect(Collectors.toList());
	}
	
	private List<EntryDetail> filterToCreditDetails(List<EntryDetail> elements) {
		return elements.stream()
				       .filter(each -> each.isCredit())
					   .collect(Collectors.toList());
	}
	
}