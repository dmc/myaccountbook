package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryUseCase;
import io.github.wtbyt298.accountbook.presentation.request.journalentry.RegisterEntryDetailParam;
import io.github.wtbyt298.accountbook.presentation.request.journalentry.RegisterJournalEntryParam;
import io.github.wtbyt298.accountbook.presentation.response.accounttitle.MergedAccountTitleViewModel;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import jakarta.validation.Valid;

/**
 * 仕訳登録処理のコントローラクラス
 */
@Controller
public class RegisterJournalEntryController {
	
	@Autowired
	private FetchListOfAccountTitleAndSubAccountTitleQueryService fetchListQueryService;
	
	@Autowired
	private RegisterJournalEntryUseCase registerJournalEntryUseCase;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
		
	/**
	 * 仕訳登録画面を表示する
	 */
	@GetMapping("/entry/registerform")
	public String load(@ModelAttribute("entryParam") RegisterJournalEntryParam param) {
		return "/entry/registerform";
	}
	
	/**
	 * 仕訳を登録する
	 */
	@PostMapping("/entry/register")
	public String register(@Valid @ModelAttribute("entryParam") RegisterJournalEntryParam param, BindingResult result) {
		if (result.hasErrors()) {
			return load(param);
		}
		RegisterJournalEntryCommand command = mapRequestToCommand(param);
		UserSession userSession = userSessionProvider.getUserSession();
		registerJournalEntryUseCase.execute(command, userSession);
		return "redirect:/entry/registerform";
	}
	
	/**
	 * クライアントから受け取ったデータを登録用DTOに詰め替える
	 */
	private RegisterJournalEntryCommand mapRequestToCommand(RegisterJournalEntryParam param) {
		List<RegisterEntryDetailCommand> detailCommands = new ArrayList<>();
		//仕訳明細データを詰め替える
		for (RegisterEntryDetailParam detailParam : param.getDetailParams()) {
			RegisterEntryDetailCommand detailCommand = new RegisterEntryDetailCommand(
				detailParam.getAccountTitleId(),    //勘定科目ID
				detailParam.getSubAccountTitleId(), //補助科目ID
				detailParam.getDetailLoanType(),    //明細の貸借
				detailParam.getAmount()             //仕訳金額
			);
			detailCommands.add(detailCommand);
		}
		return new RegisterJournalEntryCommand(
			param.getDealDate(),    //取引日
			param.getDescription(), //摘要
			detailCommands          //仕訳明細のリスト
		);
	}
	
	/**
	 * 勘定科目のセレクトボックスに表示するデータを取得する
	 */
	@ModelAttribute("selectBoxElements")
	public List<MergedAccountTitleViewModel> selectBoxElements() {
		UserSession userSession = userSessionProvider.getUserSession();
		List<AccountTitleAndSubAccountTitleDto> data = fetchListQueryService.findAll(userSession.userId());
		return mapDtoToViewModel(data);
	}
	
	/**
	 * DBから取得したデータをViewモデルに詰め替える
	 */
	private List<MergedAccountTitleViewModel> mapDtoToViewModel(List<AccountTitleAndSubAccountTitleDto> data) {
		List<MergedAccountTitleViewModel> viewModels = new ArrayList<>();
		for (AccountTitleAndSubAccountTitleDto dto : data) {
			viewModels.add(new MergedAccountTitleViewModel(dto));
		}
		return viewModels;
	}

}
