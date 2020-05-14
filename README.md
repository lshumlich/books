
This is the personal financial system for the Shumlich Family since January 2001. It has been used ever since. This was written as a reference project when I first learned Java and we have managed our financial life ever since. It first used mysql and in 2020 was converted to postgres.

In 2020 the code was upgraded to use maven and a newer version of Tomcat

One of many ways to run it:

Download the Java JDK from oracle

Download Eclipse. This video has helped me do that https://www.youtube.com/watch?v=rSFraftR4I4

Download Apache Tomcat. At the writing of this we are using 8.5.55. Follow the install instructions.

In Eclipse checkout the GitHub repository as follows:

- File / Import / Git / Projects from Git (with smart import)
- Clone URI 
- in the URI: https://github.com/lshumlich/books
(Note to self change the name to book in the maven project)

Setup Tomcat in eclipse:
- Open the Perspective window (small icon on top right hand side just below the x)
- Pick Java EE
- On the lower tabbed section of Eclipse look for a server tab
- Select Click to Create a new server
- Select the correct Apache server

This video helped: https://www.youtube.com/watch?v=PH-bK3g2YmU

You may need to setup the users in the tomcat-users.xml under the servers tree (far right hand side)

If you need a refresher on Maven this was a great resource and the video helped.

https://www.youtube.com/watch?v=sNEcpw8LPpo
ProgrammingKnowledge2 → Youtube dude was really good 

Download Postgres

Setup a user and database. This app will create all of the tables if you change the option in hibernate.cfg2.xml

copy src/main/resources/hibernate.cfg2.xml.backup to (remove backup)

