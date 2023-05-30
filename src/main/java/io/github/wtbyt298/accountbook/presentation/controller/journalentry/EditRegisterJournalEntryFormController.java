package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.wtbyt298.accountbook.presentation.params.journalentry.RegisterJournalEntryParam;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 仕訳登録画面のコントローラクラス
 */
@Controller
public class EditRegisterJournalEntryFormController {

	/**
	 * 仕訳明細の入力フォームを追加する
	 * 上限は10
	 */
	@PostMapping(value = {"/entry/register", "/entry/correct"}, params = "add")
	public String addForm(@ModelAttribute("entryId") String id, @ModelAttribute("entryParam") RegisterJournalEntryParam param, @RequestParam("add") String value, Model model, HttpServletRequest request) {
		final String ERROR_MESSAGE = "これ以上追加できません。";
		if (param.isFull(value)) {
			model.addAttribute("error_" + value, ERROR_MESSAGE);
		}
		param.addList(value);
		return returnPath(request);
	}
	
	/**
	 * 仕訳明細の入力フォームを削除する
	 * 下限は1
	 */
	@PostMapping(value = {"/entry/register", "/entry/correct"}, params = "remove")
	public String removeForm(@ModelAttribute("entryId") String id, @ModelAttribute("entryParam") RegisterJournalEntryParam param, @RequestParam("remove") String value, HttpServletRequest request) {
		final String type = value.substring(0, value.indexOf("-"));
		final int index = Integer.valueOf(request.getParameter("remove").substring(value.indexOf("-") + 1));
		param.removeList(type, index);
		return returnPath(request);
	}
	
	/**
	 * リクエスト元に対応するパスを生成する
	 */
	private String returnPath(HttpServletRequest request) {
		if (request.getRequestURI().equals("/entry/register")) return "/entry/register";
		if (request.getRequestURI().equals("/entry/correct")) return "/entry/entry";
		return "/error";
	}
	
}
