$(document).ready(function () {
	let dataset = document.getElementById('jsVariables').dataset;
	let email = dataset.email;

	function onMainInfoInput() {
		var actualEmail = document.getElementById("emailInput").value;
		if(email.localeCompare(actualEmail)){
		    document.getElementById("saveChangesButton").disabled = false;
		}
		else{
		    document.getElementById("saveChangesButton").disabled = true;
		}
	}

	document.getElementById("emailInput").oninput = onMainInfoInput;

	function onPasswordInput(){
		var pswd1 = document.getElementById("passwordOneInput").value;
		var pswd2 = document.getElementById("passwordTwoInput").value;
		if(!pswd1.localeCompare(pswd2)){
			if (!document.getElementById("passwordForm").checkValidity()) {
			    document.getElementById("savePswdButton").disabled = true;
		    }
			else{
			    document.getElementById("savePswdButton").disabled = false;
			}
		}
		else{
		    document.getElementById("savePswdButton").disabled = true;
		}
	}

	document.getElementById("passwordOneInput").oninput  = onPasswordInput;
	document.getElementById("passwordTwoInput").oninput  = onPasswordInput;

	if(document.getElementById("fileChooser")){
		document.getElementById("fileChooser").onchange = function() {
		    document.getElementById("uploadForm").submit();
		};
	}
});