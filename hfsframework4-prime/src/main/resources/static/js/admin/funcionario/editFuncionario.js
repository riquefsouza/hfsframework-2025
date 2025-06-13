class EditFuncionario extends HFSSystemUtil {
	constructor()
	{
		super();
		
		this.hideQueryString();
		
		this._page = $('#funcionarioView');
	}
	
	btnCancelClick(event) {
		event.preventDefault();
		this._page[0].click();
	}
	
}

$(function() {
	const editFuncionario = new EditFuncionario();
	
	$('#btnCancel').click(editFuncionario.btnCancelClick.bind(editFuncionario));
	
});
