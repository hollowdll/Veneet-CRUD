// DOM ilmoitusten resettaaminen. 3 sekunnin viive.
const notificationResetTimer = {
	timeout: null,
	
	start() {
		this.timeout = setTimeout(() => {
			document.getElementById("notification").innerHTML = "";
			this.timeout = null;
		}, 3000);
	},
	
	cancel() {
		if (this.timeout !== null) {
			clearTimeout(this.timeout);
			// console.log("Cleared timeout!")
		}
	}
}

//funktio lomaketietojen muuttamiseksi JSON-stringiksi
const serializeForm = form => {
	return JSON.stringify(
	    Array.from(new FormData(form).entries())
	        .reduce((m, [ key, value ]) => Object.assign(m, { [key]: value }), {})
	        );	
}

//funktio arvon lukemiseen urlista avaimen perusteella
const requestURLParam = strParam => {
    let strPageURL = window.location.search.substring(1);
    // console.log(strPageURL);
    let strURLVariables = strPageURL.split("&");
    for (let i = 0; i < strURLVariables.length; i++) {
        let strParameterName = strURLVariables[i].split("=");
        if (strParameterName[0] == strParam) {
            return strParameterName[1];
        }
    }
}

// Tyhjennä ilmoituskenttä. Peruuttaa ensin nykyisen timeoutin.
const resetNotification = () => {
	notificationResetTimer.cancel();
	notificationResetTimer.start();
}

//Tutkitaan lisättävät tiedot ennen niiden lähettämistä backendiin
const checkInputAndAdd = () => {
	if (checkInputFields()) {
		addItem();
	}
}

//Tutkitaan lisättävät tiedot ennen niiden lähettämistä backendiin
const checkInputAndUpdate = () => {
	if (checkInputFields()) {
		updateItem();
	}
}

//funktio syöttötietojen tarkistamista varten
const checkInputFields = () => {
	let notification = "";
	let nimi = document.getElementById("nimi");
	let merkkimalli = document.getElementById("merkkimalli");
	let pituus = document.getElementById("pituus");
	let leveys = document.getElementById("leveys");
	let hinta = document.getElementById("hinta");
	
	// nimi
	if (nimi.value.length < 1) {
		notification = "Nimi ei voi olla tyhjä!";
		nimi.focus();
	} else if (nimi.value.match(/[0-9]/g)) {
		notification = "Nimessä ei voi olla numeroita!";
		nimi.focus();
		
	// merkkimalli
	} else if (merkkimalli.value.length < 1) {
		notification = "Merkkimalli ei voi olla tyhjä!";
		merkkimalli.focus();
		
	// pituus
	} else if (pituus.value.length < 1) {
		notification = "Pituus ei voi olla tyhjä!";
		pituus.focus();
	} else if (pituus.value.match(/[A-ZÅÄÖ]/gi)) {
		notification = "Pituudessa ei voi olla kirjaimia!";
		pituus.focus();
		
	// leveys
	} else if (leveys.value.length < 1) {
		notification = "Leveys ei voi olla tyhjä!";
		leveys.focus();
	} else if (leveys.value.match(/[A-ZÅÄÖ]/gi)) {
		notification = "Leveydessä ei voi olla kirjaimia!";
		leveys.focus();
		
	// hinta
	} else if (hinta.value.length < 1) {
		notification = "Hinta ei voi olla tyhjä!";
		hinta.focus();
	} else if (hinta.value.match(/[A-ZÅÄÖ]/gi)) {
		notification = "Hinnassa ei voi olla kirjaimia!";
		hinta.focus();
	}
	
	// ilmoitus
	if (notification != "") {
		document.getElementById("notification").innerHTML = notification;
		resetNotification();
		return false;
	} else {
		// siivoa arvot
		nimi.value = sanitizeInputField(nimi.value);
		merkkimalli.value = sanitizeInputField(merkkimalli.value);
		pituus.value = parseFloat(pituus.value);
		pituus.value = sanitizeInputField(pituus.value);
		leveys.value = parseFloat(leveys.value);
		leveys.value = sanitizeInputField(leveys.value);
		hinta.value = parseInt(hinta.value);
		hinta.value = sanitizeInputField(hinta.value);
		return true;
	}
}

//Funktio XSS-hyökkäysten estämiseksi (Cross-site scripting)
const sanitizeInputField = text => {
	text = text.replace(/[\[\]{}<>:;/\\ ]/g, "").trim();	// poista ei-halutut merkit
	text = text.replace(/'/g, "''");
	text = text.replace(/,/g, ".");		// desimaaliluvuille
	return text;
}

// Kun poista-nappia painetaan
const confirmDeletion = (tunnus, name) => {
	if (confirm("Poista vene " + decodeURI(name) + "?")) { //decodeURI() muutetaan enkoodatut merkit takaisin normaaliksi kirjoitukseksi
		deleteItem(tunnus, name);
	}
}

// Keskitä syöttökenttä
const setFieldFocus = target => {
	document.getElementById(target).focus();
}

//Funktio Enter-nappiin. Kutsu bodyn onkeydown()-metodista.
const checkKey = (event, target) => {	
	if (event.keyCode == 13) {	//13=Enter
		if (target == "list") {
			fetchItems();
		} else if (target == "add") {
			checkInputAndAdd();
		} else if (target == "update") {
			checkInputAndUpdate();
		}
	}
}