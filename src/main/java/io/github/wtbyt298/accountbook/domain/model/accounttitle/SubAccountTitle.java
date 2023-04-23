package io.github.wtbyt298.accountbook.domain.model.accounttitle;

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
	 * @return 補助科目ID
	 */
	public SubAccountTitleId id() {
		return id;
	}
	
	/**
	 * 補助科目名を変更する
	 */
	void changeName(SubAccountTitleName newName) {
		name = newName;
	}
	
	@Override
	public String toString() {
		return id.toString() + "：" + name.toString();
	}
	
}
