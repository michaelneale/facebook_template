To build and deploy this on CloudBees, follow those steps:

Create application:
	bees app:create MYAPP_ID

Create database:
	bees db:create -u DB_USER -p DB_PASSWORD DBNAME

Bind database as datasource:
	bees app:bind -db DBNAME -a MYAPP_ID -as ExampleDS

Create a new software project in Jenkins, changing the following:
	- Add this git repository (or yours with this code) on Jenkins
	- In build, Add build step -> Invoke Ant with no target.
	- Also check "Deploy to CloudBees" with those parameters:
		Application Id: MYAPP_ID
		Filename Pattern: target/*.war

To build this locally:

In the ant_template directory, open a command line, and invoke ant by typing "ant" to build the war file, then deploy it on cloudbees typing:
	bees app:deploy -a MYAPP_ID target/*.war