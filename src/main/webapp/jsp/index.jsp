<!DOCTYPE HTML PUBLIC “-//W3C//DTD HTML 4.01//EN">
<%@ page import="java.util.List" %>
<%@ page import="jsp.Item" %>
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
        			<label for="removeName">Name: </label>
        			<input type="text" name="removeName" id="removeName" />
        			<input type="submit" name="remove" value="Remove item" />
    			</p>
			</div>
		</form>

		<table class = "item_table">
			<tr><td>Name</td><td>Comment</td><td>Author</td></tr>
			<% List<Item> itemList = (List<Item>) request.getAttribute("items");
			StringBuilder sb = new StringBuilder();
			for (Item item : itemList) {
				sb.append("<tr><td>");
				sb.append(item.getName());
				sb.append("</td><td>");
				sb.append(item.getComment());
				sb.append("</td><td>");
				sb.append(item.getAuthor());
				sb.append("</td></tr>\n");
			}
			out.print(sb.toString()); %>
		</table>

  <!-- this is used for first time display of clickstart - a template-->
  <div id="clickstart_content">
     <p>
      Congratulations, <a href="#CS_appManageUrl"><span>#CS_appName</span></a>, your <a href="#CS_docUrl"><span>#CS_name</span></a> application is now running.<br />
      To modify it, take the following steps to clone the source repository:
    </p>

    <div class="CB_codeSample">
      git clone #CS_source #CS_appName<br/>
          cd #CS_appName<br/>
          ---- do your magic edits ----<br/>
          git commit -m 'This is now even better !'<br/>
          git push origin master
    </div>

    <p>Manage your application components at the following URLs:</p>
    <ul>
      <li><strong>Core application:</strong> <a href="#CS_appManageUrl">#CS_appManageUrl</a></li>
      <li><strong>Database:</strong> <a href="#CS_appManageUrl">#CS_appManageUrl</a></li>
      <li><strong>Jenkins Build System:</strong> <a href="#CS_appManageUrl">#CS_appManageUrl</a></li>
    </ul>
  </div>

  <script type="text/javascript" src="https://s3.amazonaws.com/cloudbees-downloads/clickstart/clickstart_intro.js"></script>
  <!-- end clickstart intro section -->



	</body>
</html>