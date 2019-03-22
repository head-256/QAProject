let isChanged = false;
let dataset = document.getElementById('jsVariables').dataset;
let defaultTextPlaceholder = dataset.text_placeholder;
document.getElementById("questionTitleInput").oninput = function() {
	if(isChanged == false){
		if(document.getElementById("questionTitleInput").value.length > 0){
    		document.getElementById("questionTextInput").placeholder = document.getElementById("questionTitleInput").value;
    	}
    	else{
    		document.getElementById("questionTextInput").placeholder = defaultTextPlaceholder;
    	}
	}
};

document.getElementById("questionTextInput").oninput = function() {
	if(document.getElementById("questionTextInput").value.length == 0){
    	document.getElementById("questionTextInput").placeholder = document.getElementById("questionTitleInput").value;
		isChanged = false;
	}
	else{
		isChanged = true;
	}
};