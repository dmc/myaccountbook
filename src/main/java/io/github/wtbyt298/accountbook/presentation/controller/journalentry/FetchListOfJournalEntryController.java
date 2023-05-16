package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryQueryService;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.JournalEntryOrderKey;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.presentation.response.journalentry.JournalEntryViewModel;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * 仕訳一覧取得処理のコントローラクラス
 */
@Controller
public class FetchListOfJournalEntryController {

	@Autowired
	private FetchJournalEntryQueryService fetchQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 仕訳一覧画面を表示する
	 */
	@GetMapping("/entry/entries/{selectedYearMonth}")
	public String load(@PathVariable String selectedYearMonth, String orderkey, Model model) {
		//引数のselectedYearMonthには"yyyy-MM"形式の文字列を想定
		try {
			List<JournalEntryDto> data = fetchJournalEntries(selectedYearMonth, orderkey);
			List<JournalEntryViewModel> viewModels = mapDtoToViewModel(data);
			model.addAttribute("entries", viewModels);
			return "/entry/entries";
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "/entry/entries";
		}
	}
	
	/**
	 * 年月を指定して仕訳の一覧を取得する
	 */
	private List<JournalEntryDto> fetchJournalEntries(String selectedYearMonth, String orderKey) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		JournalEntryOrderKey journalEntryOrderKey = JournalEntryOrderKey.valueOf(orderKey);
		UserSession userSession = userSessionProvider.getUserSession();
		return fetchQueryService.fetchAll(yearMonth, journalEntryOrderKey, userSession.userId());
	}
	
	/**
	 * DBから取得したデータをViewモデルに詰め替える
	 */
	private List<JournalEntryViewModel> mapDtoToViewModel(List<JournalEntryDto> data) {
		List<JournalEntryViewModel> viewModels = new ArrayList<>();
		for (JournalEntryDto dto : data) {
			viewModels.add(new JournalEntryViewModel(dto));
		}
		return viewModels;
	}
	
}
