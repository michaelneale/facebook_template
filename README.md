# Facebook Template with Hibernate for CloudBees.

[![Git](https://app.soluble.cloud/api/v1/public/badges/f19f39ea-70b6-49bf-b36c-2314d007f8d3.svg?orgId=451115019187)](https://app.soluble.cloud/repos/details/github.com/michaelneale/facebook_template?orgId=451115019187)  

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/CloudBees-community/facebook_template/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>

This can be a starting point for building your Facebook app in Java. 

Step 1: click the button above
Step 2: Set up your SSH keys in Grand Central if you haven't already: https://grandcentral.cloudbees.com/user/ssh_keys
Step 3: clone the repo that was created for you when you pressed that button.
Step 4: read on.


## Configuration changes

Before doing anything, make sure to edit the three following lines in src/main/java/jsp/ItemController.java with respectively your Facebook API ID, secret key, and your app's URL.

    String APP_ID = "";
    String APP_SECRET = "";
    String REDIRECT_URL = "";

If you do not have this data available yet, you will need to go to http://facebook.com/developers to create a new Facebook app.

## Note before deploying this on JBoss 7:

There are configuration changes to do in the following file, comments will indicate which lines to change and how.

    src/main/webapp/WEB-INF/web.xml

## To build and deploy this on CloudBees, follow those steps:

Create application:

    bees app:create MYAPP_ID

Create database:

    bees db:create -u DB_USER -p DB_PASSWORD DBNAME

Bind database as datasource:

    bees app:bind -db DBNAME -a MYAPP_ID -as ExampleDS

Create a new Maven project in Jenkins, changing the following:

* Add this git repository (or yours, with this code) on Jenkins
* Also check "Deploy to CloudBees" with those parameters:

        Applications: First Match
        Application Id: MYAPP_ID
        Filename Pattern: target/*.war

* Optionally change the application container to Java EE, if you wish to deploy on JBoss. 

## To build this locally:

In the facebook_template directory, open a command line, and invoke maven by typing "mvn package" to build the war file, then deploy it on cloudbees typing:

    bees app:deploy -t tomcat -a MYAPP_ID target/*.war

Or, for JBoss:

    bees app:deploy -t jboss -a MYAPP_ID target/*.war

## To deploy this locally:

Make sure you have a MySQL database bound to java:comp/env/jdbc/ExampleDS, and then deploy in your favorite container.