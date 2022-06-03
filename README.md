This is the spring authorization server with plain spring i.e., without spring boot. The code base is same. Means the configurations use in Spring boot are using here. You can test it with the spring boot client. You just need to change the spring boot client urls in the properties file. Like the following

messages.base-uri = http://127.0.0.1:9090/OAuth2AuthorizationServerSpring/rest/messages

oauth2.authorization.uri = http://127.0.0.1:9090/OAuth2AuthorizationServerSpring/oauth2/authorize 

oauth2.token.uri = http://127.0.0.1:9090/OAuth2AuthorizationServerSpring/oauth2/token

http://127.0.0.1:9090/OAuth2AuthorizationServerSpring is {protocol}://{host}:{port}/{contextpath}.

You can import this project in eclipse. It's a maven project as appose to Spring Boot. Spring Boot is using gradle.

Change your port and context path accordingy after importing this project into eclipse. I test it with tomcat 9.

For h2 console the url is the following. No password is needed.

http://127.0.0.1:9090/OAuth2AuthorizationServerSpring/h2-console

url     : jdbc:h2:mem:testdb

username: sa

The server port and context path is change in Spring boot client project. Also note that for rest controller your url should contain rest like /rest/mesages.

This is because I configure the rest servlet to invoke for urls that starts with rest.

Both Authorization and Resource server are in the same project. This project is just for demonstration. If someone wants to configure it with plain spring. Sometimes there are cases that we can not use Spring boot. I just recently had this case. So I think it would be useful for others too.

Right now it is using authorization server 0.2.3 and Java 8.

I am not sure about 0.3.0 and java 8 compatibilty. That's why I am using 0.2.3 authorization server version with Java 8.

Thanks
