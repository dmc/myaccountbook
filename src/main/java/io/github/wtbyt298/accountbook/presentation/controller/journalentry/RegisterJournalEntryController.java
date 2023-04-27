package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.AccountTitleAndSubAccountTitleListQueryService;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryUseCase;
import io.github.wtbyt298.accountbook.presentation.request.journalentry.RegisterEntryDetailParam;
import io.github.wtbyt298.accountbook.presentation.request.journalentry.RegisterJournalEntryParam;
import io.github.wtbyt298.accountbook.presentation.response.MergedAccountTitleView;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import jakarta.validation.Valid;

/**
 * 仕訳登録処理のコントローラクラス
 */
@Controller
public class RegisterJournalEntryController {
	
	@Autowired
	private AccountTitleAndSubAccountTitleListQueryService listQueryService;
	
	@Autowired
	private RegisterJournalEntryUseCase registerUseCase;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
		
	/**
	 * ページ読み込み時の処理
	 */
	@GetMapping("/entryregisterform")
	public String entryRegisterForm(@ModelAttribute("entryParam") RegisterJournalEntryParam param) {
		return "entryregisterform";
	}
	
	/**
	 * フォームに入力した内容で仕訳を登録する
	 */
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("entryParam") RegisterJournalEntryParam param, BindingResult result) {
		if (result.hasErrors()) {
			return entryRegisterForm(param);
		}
		RegisterJournalEntryCommand command = mapRequestToCommand(param);
		UserSession userSession = userSessionProvider.getUserSession();
		registerUseCase.execute(command, userSession);
		return "redirect:/entryregisterform";
	}
	
	/**
	 * クライアントから受け取ったデータを登録用DTOに詰め替える
	 */
	private RegisterJournalEntryCommand mapRequestToCommand(RegisterJournalEntryParam param) {
		List<RegisterEntryDetailCommand> detailCommands = new ArrayList<>();
		//仕訳明細データ（ネストした要素）を詰め替える
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
	public List<MergedAccountTitleView> selectBoxElements() {
		UserSession userSession = userSessionProvider.getUserSession();
		List<AccountTitleAndSubAccountTitleDto> fetchedData = listQueryService.findAll(userSession);
		List<MergedAccountTitleView> views = new ArrayList<>();
		//DBから取得したデータを表示用のモデルに詰め替える
		for (AccountTitleAndSubAccountTitleDto dto : fetchedData) {
			views.add(new MergedAccountTitleView(dto));
		}
		return views;
	}

}
