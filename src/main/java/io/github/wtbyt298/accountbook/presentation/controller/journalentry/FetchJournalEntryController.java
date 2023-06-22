package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.EntryDetailDto;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.presentation.forms.journalentry.RegisterEntryDetailForm;
import io.github.wtbyt298.accountbook.presentation.forms.journalentry.RegisterJournalEntryForm;

/**
 * 仕訳編集画面のコントローラクラス
 */
@Controller
public class FetchJournalEntryController {

	@Autowired
	private FetchJournalEntryDataQueryService fetchJournalEntryDataQueryService;
	
	/**
	 * 仕訳編集画面を表示する
	 * フォームに取得したデータを表示する
	 */
	@GetMapping("/entry/edit/{id}")
	public String load(@PathVariable String id, Model model) {
		EntryId entryId = EntryId.fromString(id);
		model.addAttribute("entryId", entryId.value());
		
		JournalEntryDto dto =fetchJournalEntryDataQueryService.fetchOne(entryId);
		RegisterJournalEntryForm form = mapDtoToForm(dto);
		model.addAttribute("entryForm", form);
		
		return "/entry/edit";
	}
	
	/**
	 * DTOをフォームクラスに詰め替える（仕訳）
	 */
	private RegisterJournalEntryForm mapDtoToForm(JournalEntryDto dto) {
		//借方仕訳明細
		List<RegisterEntryDetailForm> debitParams = dto.getEntryDetails().stream()
			.filter(each -> each.isDebit())
			.map(each -> mapDtoToForm(each))
			.toList();
		
		//貸方仕訳明細
		List<RegisterEntryDetailForm> creditParams = dto.getEntryDetails().stream()
			.filter(each -> each.isCredit())
			.map(each -> mapDtoToForm(each))
			.toList();
		
		return new RegisterJournalEntryForm(
			dto.getDealDate(), 
			dto.getDescription(), 
			debitParams,
			creditParams
		);
	}
	
	/**
	 * DTOをフォームクラスに詰め替える（仕訳明細）
	 */
	private RegisterEntryDetailForm mapDtoToForm(EntryDetailDto dto) {
		//勘定科目IDと補助科目IDを結合し、「101-0」の形式にする
		final String mergedId =dto.getAccountTitleId() + "-" + dto.getSubAccountTitleId(); 
		
		return new RegisterEntryDetailForm(
			dto.getDetailLoanType().toString(), 
			mergedId,
			dto.getAmount()
		);
	}
	
}
