/**
 * 削除ボタン押下時に確認メッセージを表示する
 */
function showConfirmMessage() {
	var ret = window.confirm('この仕訳を削除します。よろしいですか？');
	if (ret == false) {
		document.getElementById('form-register').onsubmit = function() {return false};
		return;
	}
	document.getElementById('form-id').disabled = false;
	document.getElementById('form-register').onsubmit = function() {return true};
}

/**
 * submitを有効にする
 */
function activate() {
	document.getElementById('form-register').onsubmit = function() {return true};
}