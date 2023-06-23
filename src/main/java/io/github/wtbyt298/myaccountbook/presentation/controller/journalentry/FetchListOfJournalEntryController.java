package io.github.wtbyt298.myaccountbook.presentation.controller.journalentry;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.wtbyt298.myaccountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.myaccountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import io.github.wtbyt298.myaccountbook.application.query.service.journalentry.JournalEntryOrderKey;
import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.infrastructure.shared.exception.RecordNotFoundException;
import io.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.myaccountbook.presentation.viewmodels.journalentry.JournalEntryViewModel;

/**
 * 仕訳一覧取得処理のコントローラクラス
 */
@Controller
public class FetchListOfJournalEntryController {

	@Autowired
	private FetchJournalEntryDataQueryService fetchQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 仕訳一覧画面を表示する
	 */
	@GetMapping("/entry/entries/{selectedYearMonth}")
	public String load(@PathVariable @ModelAttribute String selectedYearMonth, @RequestParam(name = "orderKey", required = false) Optional<String> orderKey, Model model) {
		try {
			List<JournalEntryDto> data = fetchJournalEntries(selectedYearMonth, orderKey);
			List<JournalEntryViewModel> viewModels = mapDtoToViewModel(data);
			model.addAttribute("entries", viewModels);
			
			return "/entry/entries";
		} catch (RecordNotFoundException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			return "/entry/entries";
		}
	}
	
	/**
	 * 年月とソート順を指定して仕訳データの一覧を取得する
	 */
	private List<JournalEntryDto> fetchJournalEntries(String selectedYearMonth, Optional<String> orderKey) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		
		//デフォルトでは日付順で取得する
		JournalEntryOrderKey journalEntryOrderKey = JournalEntryOrderKey.valueOf(orderKey.orElse("DEAL_DATE"));
		UserSession userSession = userSessionProvider.getUserSession();
		
		return fetchQueryService.fetchAll(yearMonth, journalEntryOrderKey, userSession.userId());
	}
	
	/**
	 * DTOをビューモデルに詰め替える
	 */
	private List<JournalEntryViewModel> mapDtoToViewModel(List<JournalEntryDto> data) {
		return data.stream()
			.map(each -> new JournalEntryViewModel(each))
			.toList();
	}
	
}
