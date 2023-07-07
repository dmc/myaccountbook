package wtbyt298.myaccountbook.presentation.controller.journalentry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import wtbyt298.myaccountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import wtbyt298.myaccountbook.application.query.model.journalentry.EntryDetailDto;
import wtbyt298.myaccountbook.application.query.model.journalentry.JournalEntryDto;
import wtbyt298.myaccountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import wtbyt298.myaccountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterEntryDetailForm;
import wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterJournalEntryForm;
import wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import wtbyt298.myaccountbook.presentation.viewmodels.accounttitle.MergedAccountTitleViewModel;

/**
 * 仕訳編集画面のコントローラクラス
 */
@Controller
public class FetchJournalEntryController {

	@Autowired
	private FetchJournalEntryDataQueryService fetchJournalEntryDataQueryService;
	
	@Autowired
	private FetchListOfAccountTitleAndSubAccountTitleQueryService fetchListQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
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
	
	/**
	 * 勘定科目のセレクトボックスに表示するデータを取得する
	 */
	@ModelAttribute("selectBoxElements")
	public List<MergedAccountTitleViewModel> selectBoxElements() {
		UserSession userSession = userSessionProvider.getUserSession();
		List<AccountTitleAndSubAccountTitleDto> data = fetchListQueryService.fetchAll(userSession.userId());
		
		return mapDtoToViewModel(data);
	}
	
	/**
	 * DTOをビューモデルに詰め替える
	 */
	private List<MergedAccountTitleViewModel> mapDtoToViewModel(List<AccountTitleAndSubAccountTitleDto> data) {
		return data.stream()
			.map(each -> new MergedAccountTitleViewModel(each))
			.toList();
	}
	
}
