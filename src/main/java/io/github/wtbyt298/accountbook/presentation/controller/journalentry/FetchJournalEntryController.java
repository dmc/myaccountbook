package io.github.wtbyt298.accountbook.presentation.controller.journalentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.EntryDetailDto;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.presentation.request.journalentry.RegisterEntryDetailParam;
import io.github.wtbyt298.accountbook.presentation.request.journalentry.RegisterJournalEntryParam;

/**
 * 単一の仕訳の取得処理のコントローラクラス
 */
@Controller
public class FetchJournalEntryController {

	@Autowired
	private FetchJournalEntryDataQueryService fetchQueryService;
	
	/**
	 * 仕訳詳細画面を表示する
	 * フォームに取得したデータを表示する
	 */
	@GetMapping("/entry/entry/{id}")
	public String load(@PathVariable String id, Model model) {
		JournalEntryDto dto = fetchJournalEntry(id);
		RegisterJournalEntryParam param = mapDtoToParam(dto);
		model.addAttribute("entryId", dto.getEntryId());
		model.addAttribute("entryParam", param);
		return "/entry/entry";
	}
	
	/**
	 * IDを指定して仕訳データを取得する
	 */
	private JournalEntryDto fetchJournalEntry(String id) {
		EntryId entryId = EntryId.fromString(id);
		return fetchQueryService.fetchOne(entryId);
	}
	
	/**
	 * DTOをフォームクラスに詰め替える
	 */
	private RegisterJournalEntryParam mapDtoToParam(JournalEntryDto dto) {
		RegisterJournalEntryParam param = new RegisterJournalEntryParam();
		param.setDealDate(dto.getDealDate());
		param.setDescription(dto.getDescription());
		for (EntryDetailDto each : dto.getEntryDetails()) {
			RegisterEntryDetailParam detailParam = new RegisterEntryDetailParam();
			detailParam.setMergedId(each.getAccountTitleId() + each.getSubAccountTitleId());
			detailParam.setDetailLoanType(each.getDetailLoanType().toString());
			detailParam.setAmount(each.getAmount());
			if (each.isDebit()) {
				param.getDebitParams().add(detailParam);
			}
			if (each.isCredit()) {
				param.getCreditParams().add(detailParam);
			}
		}
		return param;
	}
	
}
