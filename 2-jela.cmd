rem mvn jelastic:deploy
rem http://code.google.com/p/jelastic-maven-plugin/
mvn -e  package  jelastic:deploy  -Djelastic -Dcccache  -Dmaven.test.skip=true 
rem mvn -X clean gae:deploy -Dgae  -Dmaven.test.skip=true 
rem -Dhttp.proxyHost=proxy.host -Dhttp.proxyPort=8080
