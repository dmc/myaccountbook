package io.github.wtbyt298.accountbook.helper.testdatacreator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.journalentry.DealDate;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Description;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetails;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * DBにテスト用の仕訳データを作成するクラス
 */
@Component
public class JournalEntryTestDataCreator {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private SubAccountTitleTestDataCreator subAccountTitleTestDataCreator;
	
	public JournalEntry create(User user) {
		JournalEntry journalEntry = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳データです。"), 
			testEntryDetails(user.id())
		);
		journalEntryRepository.save(journalEntry, user.id());
		return journalEntry;
	}
	
	private EntryDetails testEntryDetails(UserId userId) {
		List<EntryDetail> list = new ArrayList<>();
		list.add(testEntryDetail(LoanType.DEBIT, userId));
		
		return new EntryDetails(list);
	}
	
	private EntryDetail testEntryDetail(LoanType loanType, UserId userId) {
		AccountTitle accountTitle = accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		return new EntryDetail(
			accountTitle.id(), 
			subAccountTitleTestDataCreator.create(accountTitle, "0", "その他", userId).id(), 
			loanType, 
			Amount.valueOf(10000)
		);
	}
	
}
