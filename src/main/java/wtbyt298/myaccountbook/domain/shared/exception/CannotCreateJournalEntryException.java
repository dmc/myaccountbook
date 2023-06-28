package wtbyt298.myaccountbook.domain.shared.exception;

/**
 * バリデーションエラー等により仕訳を生成できなかった場合に投げる例外クラス
 */
public class CannotCreateJournalEntryException extends DomainException {
	
	private static final long serialVersionUID = 1L;

	public CannotCreateJournalEntryException(String errorMessage) {
		super(errorMessage);
	}
	
}
