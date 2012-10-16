mvn -e clean help:active-profiles gae:deploy -Dgae -DTOMCAT6  -Dmaven.test.skip=true -Dhttp.proxyHost=proxy.host -Dhttp.proxyPort=8080 
rem mvn -X clean gae:deploy -Dgae  -Dmaven.test.skip=true 
rem -Dhttp.proxyHost=proxy.host -Dhttp.proxyPort=8080
