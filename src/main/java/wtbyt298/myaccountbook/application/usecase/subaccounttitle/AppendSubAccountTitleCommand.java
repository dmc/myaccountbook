package wtbyt298.myaccountbook.application.usecase.subaccounttitle;

import lombok.Getter;

/**
 * 補助科目追加用のDTOクラス
 */
@Getter
public class AppendSubAccountTitleCommand {

	private final String parentId;
	private final String newName;
	
	public AppendSubAccountTitleCommand(String parentId, String newName) {
		this.parentId = parentId;
		this.newName = newName;
	}
	
}
