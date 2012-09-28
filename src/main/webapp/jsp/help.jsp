<!DOCTYPE HTML PUBLIC “-//W3C//DTD HTML 4.01//EN">
<html>
	<head>
		<meta http-equiv="content-type"
		content="text/html;charset=utf-8">
		<title>Java for the web.</title>
	</head>
	<body>
		Welcome to your Facebook app! <br>
		You still have to define your Facebook application API keys 
		and URL, please refer to the README.md in the root directory of your project. Or, alternatively, click 
		<a href="https://github.com/CloudBees-community/facebook_template/blob/master/README.md">here</a>.

		
	  <!-- this is used for first time display of clickstart - a template-->
	  <div id="clickstart_content" style="display:none">
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