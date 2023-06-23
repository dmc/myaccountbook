package io.github.wtbyt298.myaccountbook.presentation.controller.journalentry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterJournalEntryForm;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 仕訳明細フォームの追加・削除処理のコントローラクラス
 */
@Controller
public class EditRegisterJournalEntryFormController {

	/**
	 * 仕訳明細の入力フォームを追加する
	 * 上限は10
	 */
	@PostMapping(value = {"/entry/register", "/entry/correct"}, params = "add")
	public String addForm(@ModelAttribute("entryId") String id, @ModelAttribute("entryForm") RegisterJournalEntryForm form, @RequestParam("add") String value, Model model, HttpServletRequest request) {
		final String ERROR_MESSAGE = "これ以上追加できません。";
		if (form.isFull(value)) {
			model.addAttribute("error_" + value, ERROR_MESSAGE);
		}
		
		form.addList(value);
		
		return returnPath(request);
	}
	
	/**
	 * 仕訳明細の入力フォームを削除する
	 * 下限は1
	 */
	@PostMapping(value = {"/entry/register", "/entry/correct"}, params = "remove")
	public String removeForm(@ModelAttribute("entryId") String id, @ModelAttribute("entryForm") RegisterJournalEntryForm form, @RequestParam("remove") String value, HttpServletRequest request) {
		//「DEBIT-0」から「DEBIT」を取り出す
		final String type = value.substring(0, value.indexOf("-"));
		//「DEBIT-0」から「0」を取り出す
		final int index = Integer.valueOf(request.getParameter("remove").substring(value.indexOf("-") + 1));
		
		form.removeList(type, index);
		
		return returnPath(request);
	}
	
	/**
	 * リクエスト元に対応するパスを生成する
	 */
	private String returnPath(HttpServletRequest request) {
		if (request.getRequestURI().equals("/entry/register")) return "/entry/register";
		if (request.getRequestURI().equals("/entry/correct")) return "/entry/edit";
		return "/error";
	}
	
}
