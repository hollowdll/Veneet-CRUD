// hae tiedot GET-metodilla
const fetchItems = () => {
	const url = "veneet?search=" + document.getElementById("search-field").value;
	const requestOptions = {
        method: "GET",
        headers: { "Content-Type": "application/x-www-form-urlencoded" }       
    };
     
    fetch(url, requestOptions)
    .then(response => response.json())	//Muutetaan vastausteksti JSON-objektiksi
   	.then(response => printItems(response)) 
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//Kirjoitetaan tiedot taulukkoon JSON-objektilistasta
const printItems = respObjList => {
	//console.log(respObjList);
	let htmlStr = "";
	for (let item of respObjList) {
    	htmlStr += "<tr id='row_"+item.tunnus+"'>";
    	htmlStr += "<td>"+item.tunnus+"</td>";
    	htmlStr += "<td>"+item.nimi+"</td>";
    	htmlStr += "<td>"+item.merkkimalli+"</td>";
    	htmlStr += "<td>"+item.pituus+"</td>";
    	htmlStr += "<td>"+item.leveys+"</td>";
    	htmlStr += "<td>"+item.hinta+"</td>";
    	htmlStr += "<td><a class='update' href='muutavene.jsp?tunnus="+ item.tunnus + "'>Muuta</a></td>";
		htmlStr += "<td><span class='delete' onclick=confirmDeletion(" + item.tunnus + ",'" + encodeURI(item.nimi + " " + item.merkkimalli) + "')>Poista</span></td>"; //encodeURI() muutetaan erikoismerkit, välilyönnit jne. UTF-8 merkeiksi.
    	htmlStr += "</tr>";    	
	}	
	document.getElementById("table-body").innerHTML = htmlStr;
	
	if (respObjList.length < 1) {
		document.getElementById("notification").innerHTML = "Ei hakutuloksia.";
		resetNotification();	// Tyhjennä ilmoitus. Katso main.js
	}
}

//funktio tietojen lisäämistä varten. Kutsutaan backin POST-metodia ja välitetään kutsun mukana tiedot json-stringinä.
const addItem = () => {
	const formData = serializeForm(document.getElementById("item-form")); //Haetaan tiedot lomakkeelta ja muutetaan JSON-stringiksi. Katso main.js
	//console.log(formData);
	const url = "veneet";
    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json; charset=UTF-8" },
    	body: formData
    };
      
    fetch(url, requestOptions)
    .then(response => response.json())	//Muutetaan vastausteksti JSON-objektiksi
   	.then(responseObj => {	
   		//console.log(responseObj);
   		if (responseObj.response == 0) {
   			document.getElementById("notification").innerHTML = "Veneen lisäys epäonnistui.";	
        } else if (responseObj.response == 1) { 
        	document.getElementById("notification").innerHTML = "Veneen lisäys onnistui.";
			document.getElementById("item-form").reset(); //Tyhjennetään lomake
			setFieldFocus("nimi");
		}
		resetNotification();
   	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//Poistetaan kutsumalla backin DELETE-metodia ja välittämällä sille poistettava id
const deleteItem = (tunnus, name) => {
	const url = "veneet?tunnus=" + tunnus;
    const requestOptions = {
        method: "DELETE"
    };
    
    fetch(url, requestOptions)
    .then(response => response.json())	//Muutetaan vastausteksti JSON-objektiksi
   	.then(responseObj => {	
   		//console.log(responseObj);
   		if (responseObj.response == 0) {
			alert("Veneen poisto epäonnistui.");	        	
        } else if(responseObj.response == 1) {
			alert("Veneen " + decodeURI(name) + " poisto onnistui."); //decodeURI() muutetaan enkoodatut merkit takaisin normaaliksi kirjoitukseksi
			fetchItems();
		}
   	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//Haetaan muutettavan tiedot. Kutsutaan backin GET-metodia ja välitetään kutsun mukana muutettavan tiedon id
const fetchSingleItem = () => {		
    const url = "veneet?tunnus=" + requestURLParam("tunnus"); //requestURLParam() on funktio, jolla voidaan hakea urlista arvo avaimen perusteella. Löytyy main.js -tiedostosta 	
	//console.log(url);
    const requestOptions = {
        method: "GET",
        headers: { "Content-Type": "application/x-www-form-urlencoded" }
    };
    
    fetch(url, requestOptions)
    .then(response => response.json())	//Muutetaan vastausteksti JSON-objektiksi
   	.then(response => {
   		//console.log(response);
   		document.getElementById("tunnus").value = response.tunnus;
   		document.getElementById("nimi").value = response.nimi;
   		document.getElementById("merkkimalli").value = response.merkkimalli;
   		document.getElementById("pituus").value = response.pituus;
   		document.getElementById("leveys").value = response.leveys;
   		document.getElementById("hinta").value = response.hinta;
   	}) 
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}	

//funktio tietojen päivittämistä varten. Kutsutaan backin PUT-metodia ja välitetään kutsun mukana uudet tiedot json-stringinä.
const updateItem = () => {	
	const formData = serializeForm(document.getElementById("item-form")); //Haetaan tiedot lomakkeelta ja muutetaan JSON-stringiksi
	//console.log(formData);	
	const url = "veneet";    
    const requestOptions = {
        method: "PUT",
        headers: { "Content-Type": "application/json; charset=UTF-8" },  
    	body: formData
    };  
      
    fetch(url, requestOptions)
    .then(response => response.json())	//Muutetaan vastausteksti JSON-objektiksi
   	.then(responseObj => {	
   		//console.log(responseObj);
   		if (responseObj.response == 0) {
   			document.getElementById("notification").innerHTML = "Veneen muutos epäonnistui.";	
        } else if (responseObj.response == 1) { 
        	document.getElementById("notification").innerHTML = "Veneen muutos onnistui.";
        	document.getElementById("item-form").reset();
			setFieldFocus("nimi");
		}
		resetNotification();
   	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}