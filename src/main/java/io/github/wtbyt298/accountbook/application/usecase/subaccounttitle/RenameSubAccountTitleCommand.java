package io.github.wtbyt298.accountbook.application.usecase.subaccounttitle;

import lombok.Getter;

/**
 * 補助科目名変更用のDTOクラス
 */
@Getter
public class RenameSubAccountTitleCommand {

	private final String parentId;
	private final String childId;
	private final String newName;
	
	public RenameSubAccountTitleCommand(String parentId, String childId, String newName) {
		this.parentId = parentId;
		this.childId = childId;
		this.newName = newName;
	}
	
}
