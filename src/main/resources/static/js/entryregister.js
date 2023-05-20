/**
 * 現在の年月日を表す文字列を作成する
 */
window.addEventListener('load', function() {
	var now = new Date();
    var yyyy = now.getFullYear();
    var mm = ('0' + (now.getMonth() + 1)).slice(-2);
    var dd = ('0' + now.getDate()).slice(-2);
    
    var form_date = document.getElementById('form-date');
	form_date.value = yyyy + '-' + mm + '-' + dd;
});

/**
 * フォーム追加・削除の処理
 * 後で実装し直す
 */
function addForm(id) {
	var clone = document.getElementById(id).cloneNode(true);
	
	var td_select = clone.children[0];
	td_select.children[1].setAttribute('id', 'debitParams[1].mergedId');
	td_select.children[1].setAttribute('name', 'debitParams[1].mergedId');
	td_select.children[2].setAttribute('id', 'debitParams[1].detailLoanType')
	td_select.children[2].setAttribute('name', 'debitParams[1].detailLoanType')
	td_select.children[1].setAttribute('class', 'form-select');
	
	var td_amount = clone.children[1];
	td_amount.children[1].setAttribute('id', 'debitParams[1].amount');
	td_amount.children[1].setAttribute('name', 'debitParams[1].amount');
	td_amount.children[1].setAttribute('class', 'form-control');
	
	var td_delete = clone.children[2];
	
	var newForm = document.createElement('tr');
	newForm.appendChild(td_select);
	newForm.appendChild(td_amount);
	newForm.appendChild(td_delete);
	
	var addArea = document.getElementById('debit-detail-wrapper');
	addArea.appendChild(newForm);
}