package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

/**
 * 補助科目クラス
 */
public class SubAccountTitle {
	
	static final SubAccountTitle EMPTY = new SubAccountTitle(SubAccountTitleId.valueOf("0"), SubAccountTitleName.valueOf("補助科目なし"));
	private final SubAccountTitleId id;
	private SubAccountTitleName name;
	
	public SubAccountTitle(SubAccountTitleId id, SubAccountTitleName name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * 補助科目名を変更する
	 */
	void rename(SubAccountTitleName newName) {
		name = newName;
	}
	
	/**
	 * @return 補助科目ID
	 */
	public SubAccountTitleId id() {
		return id;
	}
	
	/**
	 * @return 補助科目名
	 */
	public SubAccountTitleName name() {
		return name;
	}
	
	@Override
	public String toString() {
		return id.toString() + " " + name.toString();
	}
	
}
