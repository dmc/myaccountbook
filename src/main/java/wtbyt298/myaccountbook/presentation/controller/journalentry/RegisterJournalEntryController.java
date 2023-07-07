package wtbyt298.myaccountbook.presentation.controller.journalentry;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import wtbyt298.myaccountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import wtbyt298.myaccountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import wtbyt298.myaccountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import wtbyt298.myaccountbook.application.usecase.journalentry.RegisterJournalEntryUseCase;
import wtbyt298.myaccountbook.domain.shared.exception.CannotCreateJournalEntryException;
import wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterEntryDetailForm;
import wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterJournalEntryForm;
import wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import wtbyt298.myaccountbook.presentation.viewmodels.accounttitle.MergedAccountTitleViewModel;

/**
 * 仕訳登録処理のコントローラクラス
 */
@Controller
public class RegisterJournalEntryController {
	
	@Autowired
	private RegisterJournalEntryUseCase registerJournalEntryUseCase;
	
	@Autowired
	private FetchListOfAccountTitleAndSubAccountTitleQueryService fetchListQueryService;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
		
	/**
	 * 仕訳登録画面を表示する
	 */
	@GetMapping("/entry/register")
	public String load(@ModelAttribute("entryForm") RegisterJournalEntryForm form, Model model) {
		//画面読み込み時に少なくとも1行の仕訳明細フォームを表示する
		form.initList();
		return "/entry/register";
	}
	
	/**
	 * 仕訳を登録する
	 */
	@PostMapping("/entry/register")
	public String register(@Valid @ModelAttribute("entryForm") RegisterJournalEntryForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "/entry/register";
		}
		
		RegisterJournalEntryCommand command = mapFormToCommand(form);
		UserSession userSession = userSessionProvider.getUserSession();
		
		try {
			registerJournalEntryUseCase.execute(command, userSession);
			return "redirect:/entry/register";
		} catch (CannotCreateJournalEntryException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			return "/entry/register";
		}
	}
	
	/**
	 * フォームクラスをコマンドオブジェクトに詰め替える（仕訳）
	 */
	private RegisterJournalEntryCommand mapFormToCommand(RegisterJournalEntryForm form) {
		//仕訳明細データを詰め替える
		List<RegisterEntryDetailCommand> detailCommands = form.getEntryDetailParams().stream()
			.map(each -> mapFormToCommand(each))
			.toList();
		
		return new RegisterJournalEntryCommand(
			form.getDealDate(),   
			form.getDescription(),
			detailCommands
		);
	}
	
	/**
	 * フォームクラスをコマンドオブジェクトに詰め替える（仕訳明細）
	 */
	private RegisterEntryDetailCommand mapFormToCommand(RegisterEntryDetailForm form) {
		return new RegisterEntryDetailCommand(
			form.getAccountTitleId(),    
			form.getSubAccountTitleId(), 
			form.getDetailLoanType(),    
			form.getAmount()
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
