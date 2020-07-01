let selectedNumber = 0;

function selectAll() {
	let result = document.querySelector("#select-all").checked;
	let selectors = document.querySelectorAll("tbody input[type=checkbox]");
	for(let s of selectors) {
		s.checked = result;
	}
	if(result) {
		setToMultiMode();
	} else {
		setToSingleMode();
	}
	selectedNumber = result ? selectors.length : 0;
}

function selectSingleCheckbox(id) {
	if(document.querySelector('#cb' + id).checked) {
		selectedNumber++;
		if(selectedNumber == 1) {
			setToMultiMode();
		}
	} else {
		selectedNumber--;
		if(selectedNumber == 0) {
			setToSingleMode();
		}
	}
}

function setToMultiMode() {
	document.querySelector("#select-all").checked = true;
	for(let b of document.querySelectorAll(".btn-group")) {
		b.style.visibility = 'hidden';
	}
	document.querySelector("#accept-all-selected").style.visibility = 'visible';
}

function setToSingleMode() {
	document.querySelector("#select-all").checked = false;
	for(let b of document.querySelectorAll(".btn-group")) {
		b.style.visibility = 'visible';
	}
	document.querySelector("#accept-all-selected").style.visibility = 'hidden';
}