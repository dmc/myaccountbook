package com.github.wtbyt298.myaccountbook.application.usecase.subaccounttitle;

import lombok.Getter;

/**
 * 補助科目名変更用のDTOクラス
 */
@Getter
public class RenameSubAccountTitleCommand {

	private final String parentId;
	private final String subId;
	private final String newName;
	
	public RenameSubAccountTitleCommand(String parentId, String subId, String newName) {
		this.parentId = parentId;
		this.subId = subId;
		this.newName = newName;
	}
	
}
