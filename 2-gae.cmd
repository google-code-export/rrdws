mvn -X clean gae:deploy -Dgae  -Dmaven.test.skip=true -Dhttp.proxyHost=proxy.host -Dhttp.proxyPort=8080
