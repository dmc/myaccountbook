package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
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
	private FetchJournalEntryDataQueryService fetchQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 仕訳一覧画面を表示する
	 */
	@GetMapping("/entry/entries/{selectedYearMonth}")
	public String load(@PathVariable String selectedYearMonth, @RequestParam(name = "orderKey", required = false) Optional<String> orderKey, Model model) {
		//引数のselectedYearMonthには"yyyy-MM"形式の文字列を想定
		model.addAttribute("selectedYearMonth", selectedYearMonth);
		//デフォルトでは日付順で取得する
		List<JournalEntryViewModel> viewModels = fetchJournalEntries(selectedYearMonth, orderKey);
		model.addAttribute("entries", viewModels);
		return "/entry/entries";
	}
	
	/**
	 * 年月を指定して仕訳の一覧を取得する
	 */
	private List<JournalEntryViewModel> fetchJournalEntries(String selectedYearMonth, Optional<String> orderKey) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		//デフォルトでは日付順で取得する
		JournalEntryOrderKey journalEntryOrderKey = JournalEntryOrderKey.valueOf(orderKey.orElse("DEAL_DATE"));
		UserSession userSession = userSessionProvider.getUserSession();
		List<JournalEntryDto> data = fetchQueryService.fetchAll(yearMonth, journalEntryOrderKey, userSession.userId());
		return mapDtoToViewModel(data);
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
	
	@ExceptionHandler(RuntimeException.class)
	public String handleException(RuntimeException exception, Model model) {
		model.addAttribute("errorMessage", exception.getMessage());
		return "/entry/entries";
	}
	
}
