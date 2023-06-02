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

/**
 * 画面表示時に合計金額を表示する
 */
window.addEventListener('load', function() {
	showTotalAmount('debit');
	showTotalAmount('credit');
});

/**
 * 合計金額を表示用の文字列として取得する
 */
function showTotalAmount(type) {
	switch (type) {
		case 'debit':
			document.getElementById('debit-total').innerText = '借方合計：' + calculate(type).toLocaleString() + '円';
			break;
		case 'credit':
			document.getElementById('credit-total').innerText = '貸方合計：' + calculate(type).toLocaleString() + '円';
			break;
	}
}

/**
 * 合計金額を再計算する
 */
function calculate(type) {
	var total = 0;
	var className = '.' + type + '-amount';
	var forms = document.querySelectorAll(className);
	for (var i = 0; i < forms.length; i++) {
		var adding = forms[i].value;
		if (adding == '') continue;
		total += parseInt(adding);
	}
	return total;
}