# SchedulerApp - Reporting Tool for export-import Oracle Database

Here is more examples to simplify your everyday life, making quick non-standard solutions, if you don’t have any tool for automation.

System requirements (Works fine on old Desktop):<br><br>
	min. 8 GB RAM or more<br>
	CPU Core i3 or higher<br>
	Win7 or Win10<br>
	Office 2007 or 2013 or 365<br>
	Glassfish 4.1.1<br>
	Java 8<br>
	OracleXE<br>

Functions:

	Exported from oracle databases in .xlsx, .xls or .csv format with link or attachment.

	If it is larger then 10 MB, the report is compressed in .gz file automatically, but if it still larger then 16 MB, it does not send the attachment but alerts in subject the unsuccessful attachment.

	Excel Macro scheduled run

	.exe, .cmd or other command line runs scheduled

	Import data from files, (csv or xslx) or from MySQL database

	File export from Oracle database via SSH Tunnel to xlsx csv or xls format

	From Oracle database import data to another Oracle database via SSH Tunnel

	Active Directory authentication at login

	Webservice features ready for Mobile client

	Application is logging into database, all the task runs and modifications too

It’s easy to redesign so that make your life more simple. Schedule your own java class or process by running a schedule or use one of the ready ones.

Easy to use input forms, and views with the Primefaces library.

Logs into a database with Oracle package:

	Tables and package create .sql --> SchedulerApp/SchedulerApp_Tables_Packages_SQL/


After table and package created, change in source this parameters:


	SchedulerApp/SchedulerApp-ejb/src/java/com/schedulerapp/FixClasses/JdbcHandler.java

		Set oracle databases connections:

		line 45 : if (dbname.equals("rep_1")) {
		line 48 : String URL = "jdbc:oracle:thin:@host.domain.com:1526:rep1";
		line 49 : conn = DriverManager.getConnection(URL, "user", "passwd");
	
		line 54 : } else if (dbname.equals("rep_2")) {
		line 57 : String URL = "jdbc:oracle:thin:@host.domain.com:1526:rep2";
		line 58 : conn = DriverManager.getConnection(URL, "user", "passwd");

		line 63 : } else if (dbname.equals("apex_1")) {
		line 66 : String URL = "jdbc:oracle:thin:@host.domain.com:1526:rep3";
		line 67 : conn = DriverManager.getConnection(URL, "user", "passwd");

	SchedulerApp/SchedulerApp-ejb/src/java/com/schedulerapp/FixClasses/Loggtodb.java

		Set database connection where create package and tables:
		
		line 37 : ods.setURL("jdbc:oracle:thin:@host.domain.com:1526:rep_1");
		line 38 : ods.setUser("user");
		line 39 : ods.setPassword("passwd");

	SchedulerApp/SchedulerApp-ejb/src/java/com/schedulerapp/FixClasses/MailSMTP.java

		Set smtp connection data:
		
		line 72 : prop.put("mail.smtp.auth", "false");
		line 73 : prop.put("mail.smtp.host", "mail.host.com");
		line 74 : prop.put("mail.smtp.ssl.trust", "mail.host.com");
		line 75 : prop.put("mail.smtp.starttls.enable", "true");

		If use sharepoint2013:
		
		line 87 : ripurl = ripurl.replace("//share.host.intra@SSL/DavWWWRoot" , "https://share.host.intra");
		line 89 : String mail_image ="'http://hostip:8080/jpg/charts-graphs-cholesterol.png'";

		Change message when send message with attachment and without:
		
		line 94 : email message
		line 98 : email message

		Reply mail button emailaddress:
		
		line 222 : mailto:reporting@host.com  reply mail address

		line 335 : set sender emailaddress

	SchedulerApp/SchedulerApp-ejb/src/java/com/schedulerapp/FixClasses/SendPushMail.java

		Set database connection where create package and tables:
		
		line 42 : ods.setURL("jdbc:oracle:thin:@host.domin.com:1526:rep_1");
		line 43 : ods.setUser("user");
		line 44 : ods.setPassword("pass");

	SchedulerApp/SchedulerApp-ejb/src/java/com/schedulerapp/ejb/JobSessionBean.java

		Choose database connection dbname where create package and tables:
		
		line 100 : conn = Hand.getConnection("rep_1");
		line 168 : conn = Hand.getConnection("rep_1");
		line 548 : conn = Hand.getConnection("rep_1");
		line 645 : conn = Hand.getConnection("rep_1");
		line 862 : conn = Hand.getConnection("rep_1");
		line 965 : conn = Hand.getConnection("rep_1");
		line 1061 : conn = Hand.getConnection("rep_1");
		line 1246 : conn = Hand.getConnection("rep_1");
		line 1378 : conn = Hand.getConnection("rep_1");
		line 1484 : conn = Hand.getConnection("rep_1");
		line 1523 : conn = Hand.getConnection("rep_1");
		line 1614 : conn = Hand.getConnection("rep_1");

	SchedulerApp/SchedulerApp-war/src/java/com/schedulerapp/web/LoginDAO.java

		Set data for Active Directory authentication:
	
		line 80 : env.put(Context.PROVIDER_URL, "ldap://ldaphost:port");
		line 84 : env.put(Context.SECURITY_PRINCIPAL, UserName+"@ldaphost");

	SchedulerApp/SchedulerApp-war/src/java/com/schedulerapp/web/JobMBean.java

		Choose database connection dbname where create package and tables:
		
		line 107 : conn = Hand.getConnection("rep_1");
		line 219 : conn = Hand.getConnection("rep_1");
		line 668 : conn = Hand.getConnection("rep_1");

	SchedulerApp/SchedulerApp-war/src/java/com/schedulerapp/ws_classes/LoginDAO_ws.java

		Set data for Active Directory authentication:
		
		line 79 : env.put(Context.PROVIDER_URL, "ldap://ldaphost:389");
		line 83 : env.put(Context.SECURITY_PRINCIPAL, UserName+"@ldaphost");



