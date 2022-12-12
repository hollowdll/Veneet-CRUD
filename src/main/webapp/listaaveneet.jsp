<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="css/style.css">
	<title>Venehakemisto</title>
</head>
	
<body onload="setFieldFocus('search-field')">
	<table id="list">
		<thead>
			<tr>
				<th colspan="8" class="navigation"><a id="link" href="lisaavene.jsp">Lisää uusi vene</a></th>
			</tr>
			<tr>
				<th colspan="3" id="search-text">Hakusana:</th>
				<th colspan="2"><input type="text" size="30vw" id="search-field" placeholder="Kirjoita tähän" onkeydown="checkKey(event, 'list')"></th>
				<th colspan="3"><input type="button" value="Hae" id="search-button" onclick="fetchItems()"></th>
			</tr>
			<tr>
				<th>Tunnus</th>
				<th>Nimi</th>
				<th>Merkkimalli</th>
				<th>Pituus (metriä)</th>
				<th>Leveys (metriä)</th>
				<th>Hinta (euroa)</th>
				<th colspan="2"></th>
			</tr>
		</thead>
		<tbody id="table-body">
		</tbody>
	</table>
	
	<p id="notification"></p>
	
	<script src="scripts/main.js"></script>
	<script src="scripts/io.js"></script>
	<script>fetchItems();</script>
</body>

</html>