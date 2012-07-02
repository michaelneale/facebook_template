<!DOCTYPE HTML PUBLIC “-//W3C//DTD HTML 4.01//EN">
<html>
	<head>
		<meta http-equiv="content-type"
		content="text/html;charset=utf-8">
		<title>Java for the web.</title>
	</head>
	<body>
		<form action="/" method="post">
			<div class = "input">
				<p>
        			<label for="name">Name: </label>
        			<input type="text" name="name" id="name" />
        			<label for="id">Comments: </label>
        			<input type="text" name="comment" id="comment" />
        			<input type="submit" name="add" value="Add new item" />
    			</p>
			</div>
		</form>
		<form action="/" method="post">
			<div class = "remove">
				<p>
        			<label for="name">Name: </label>
        			<input type="text" name="removeName" id="removeName" />
        			<input type="submit" name="remove" value="Remove item" />
    			</p>
			</div>
		</form>

		<table class = "item_table">
			<div class = "item_properties">
				<tr><td>Name</td><td>Comment</td><td>Author</td></tr>
			</div>
			<% out.print(request.getAttribute("items")); %>
		</table>
	</body>
</html>