<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<!-- defer = execute scripts after page has finished parsing -->
	<script defer src="scripts/main.js"></script>
	<script defer src="scripts/io.js"></script>
	<link rel="stylesheet" href="css/style.css">
	<title>Lisää uusi vene</title>
</head>

<body onload="setFieldFocus('nimi')" onkeydown="checkKey(event, 'add')">
	<form id="item-form">
		<table>
			<thead>	
				<tr>
					<th colspan="6" class="navigation"><a id="link" href="listaaveneet.jsp">Takaisin listaukseen</a></th>
				</tr>		
				<tr>
					<th>Nimi</th>
					<th>Merkkimalli</th>
					<th>Pituus (metriä)</th>
					<th>Leveys (metriä)</th>
					<th>Hinta (euroa)</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" name="nimi" id="nimi" /></td>
					<td><input type="text" name="merkkimalli" id="merkkimalli" /></td>
					<td><input type="text" name="pituus" id="pituus" /></td>
					<td><input type="text" name="leveys" id="leveys" /></td>
					<td><input type="text" name="hinta" id="hinta" /></td>
					<td><input type="button" value="Lisää" onclick="checkInputAndAdd()" /></td>
				</tr>
			</tbody>
		</table>
	</form>
	
	<p id="notification"></p>
</body>

</html>