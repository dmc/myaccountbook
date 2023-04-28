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
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryListQueryService;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.JournalEntryOrderKey;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.presentation.response.JournalEntryView;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * 仕訳一覧取得処理のコントローラクラス
 */
@Controller
public class FetchJournalEntryListController {

	@Autowired
	private FetchJournalEntryListQueryService fetchQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 年月を指定して仕訳の一覧を取得する
	 */
	@GetMapping("/entries/{selectedYearMonth}")
	public String entries(@PathVariable String selectedYearMonth, Model model) {
		//引数のselectedYearMonthには"yyyy-MM"形式の文字列を想定
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		UserSession userSession = userSessionProvider.getUserSession();
		List<JournalEntryDto> journalEntryDtoList = fetchQueryService.findAll(yearMonth, JournalEntryOrderKey.DEAL_DATE ,userSession);
		List<JournalEntryView> views = mapAllDtoToViews(journalEntryDtoList);
		model.addAttribute("entries", views);
		return "entries";
	}
	
	/**
	 * 仕訳のDTOをViewモデルに詰め替えたリストを返す
	 */
	private List<JournalEntryView> mapAllDtoToViews(List<JournalEntryDto> dtoList) {
		List<JournalEntryView> views = new ArrayList<>();
		for (JournalEntryDto dto : dtoList) {
			views.add(new JournalEntryView(dto));
		}
		return views;
	}
	
}
