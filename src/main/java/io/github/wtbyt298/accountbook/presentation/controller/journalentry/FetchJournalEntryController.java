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
 * 単一の仕訳の取得処理のコントローラクラス
 */
@Controller
public class FetchJournalEntryController {

	@Autowired
	private FetchJournalEntryDataQueryService fetchJournalEntryDataQueryService;
	
	/**
	 * 仕訳詳細画面を表示する
	 * フォームに取得したデータを表示する
	 */
	@GetMapping("/entry/entry/{id}")
	public String load(@PathVariable String id, Model model) {
		EntryId entryId = EntryId.fromString(id);
		JournalEntryDto dto =fetchJournalEntryDataQueryService.fetchOne(entryId);
		RegisterJournalEntryForm form = mapDtoToParameter(dto);
		model.addAttribute("entryId", entryId.value());
		model.addAttribute("entryForm", form);
		return "/entry/entry";
	}
	
	/**
	 * DTOをフォームクラスに詰め替える（仕訳）
	 */
	private RegisterJournalEntryForm mapDtoToParameter(JournalEntryDto dto) {
		List<RegisterEntryDetailForm> debitParams = dto.getEntryDetails().stream()
			.filter(each -> each.isDebit())
			.map(each -> mapDtoToParameter(each))
			.toList();
		List<RegisterEntryDetailForm> creditParams = dto.getEntryDetails().stream()
			.filter(each -> each.isCredit())
			.map(each -> mapDtoToParameter(each))
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
	private RegisterEntryDetailForm mapDtoToParameter(EntryDetailDto dto) {
		final String mergedId =dto.getAccountTitleId() + "-" + dto.getSubAccountTitleId(); 
		return new RegisterEntryDetailForm(
			dto.getDetailLoanType().toString(), 
			mergedId,
			dto.getAmount()
		);
	}
	
}
