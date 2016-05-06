# Setup local test

## 1. Get libs

    mkdir -p ~/gcd-network 
    cd ~/gcd-network && curl -o gcd-v1beta3-1.0.0.zip http://storage.googleapis.com/gcd/tools/gcd-v1beta3-1.0.0.zip
    unzip gcd-v1beta3-1.0.0.zip
    ~/gcd-network/gcd/gcd.sh create -p sample-ds ~/gcd-network/sample-ds

## 2. Start local ds

    ~/gcd-network/gcd/gcd.sh start --host=0.0.0.0 --port=8000 ~/gcd-network/sample-ds

## 3. Run tests

Passes:

    mvn clean test -Dgcp_ip=localhost

Fails under 0.1.7:

    mvn clean test -Dgcp_ip=127.0.0.1

with error:

````
com.google.gcloud.datastore.DatastoreException: Non-protobuf error: <html><head><title>Error 404</title></head>
<body><h2>Error 404</h2></body>
</html>. HTTP status code was 404.
	at com.google.gcloud.datastore.spi.DefaultDatastoreRpc.translate(DefaultDatastoreRpc.java:102)
	at com.google.gcloud.datastore.spi.DefaultDatastoreRpc.lookup(DefaultDatastoreRpc.java:139)
	at com.google.gcloud.datastore.DatastoreOptions.normalize(DatastoreOptions.java:116)
	at com.google.gcloud.datastore.DatastoreOptions.access$300(DatastoreOptions.java:34)
	at com.google.gcloud.datastore.DatastoreOptions$Builder.build(DatastoreOptions.java:81)
	at gcd.TestGcdConnect.testConnect_localhost(TestGcdConnect.java:49)
	at gcd.TestGcdConnect.testConnect_includeScheme(TestGcdConnect.java:23)
Caused by: com.google.datastore.v1beta3.client.DatastoreException: Non-protobuf error: <html><head><title>Error 404</title></head>
<body><h2>Error 404</h2></body>
</html>. HTTP status code was 404., code=INTERNAL
	at com.google.datastore.v1beta3.client.RemoteRpc.makeException(RemoteRpc.java:126)
	at com.google.datastore.v1beta3.client.RemoteRpc.makeException(RemoteRpc.java:140)
	at com.google.datastore.v1beta3.client.RemoteRpc.call(RemoteRpc.java:89)
	at com.google.datastore.v1beta3.client.Datastore.lookup(Datastore.java:92)
	at com.google.gcloud.datastore.spi.DefaultDatastoreRpc.lookup(DefaultDatastoreRpc.java:137)
	... 38 more

testConnect_noScheme(gcd.TestGcdConnect)  Time elapsed: 0 sec  <<< FAILURE!
java.lang.IllegalArgumentException: java.net.URISyntaxException: Illegal character in scheme name at index 0: 172.16.10.157:8000/v1beta3/projects/sample-ds
	at com.google.datastore.v1beta3.client.DatastoreFactory.validateUrl(DatastoreFactory.java:121)
	at com.google.datastore.v1beta3.client.DatastoreOptions$Builder.projectEndpoint(DatastoreOptions.java:127)
	at com.google.gcloud.datastore.spi.DefaultDatastoreRpc.<init>(DefaultDatastoreRpc.java:61)
	at com.google.gcloud.datastore.DatastoreOptions$DefaultDatastoreRpcFactory.create(DatastoreOptions.java:59)
	at com.google.gcloud.datastore.DatastoreOptions$DefaultDatastoreRpcFactory.create(DatastoreOptions.java:53)
	at com.google.gcloud.ServiceOptions.rpc(ServiceOptions.java:493)
	at com.google.gcloud.datastore.DatastoreOptions.normalize(DatastoreOptions.java:116)
	at com.google.gcloud.datastore.DatastoreOptions.access$300(DatastoreOptions.java:34)
	at com.google.gcloud.datastore.DatastoreOptions$Builder.build(DatastoreOptions.java:81)
	at gcd.TestGcdConnect.testConnect_localhost(TestGcdConnect.java:49)
	at gcd.TestGcdConnect.testConnect_noScheme(TestGcdConnect.java:28)
Caused by: java.net.URISyntaxException: Illegal character in scheme name at index 0: 172.16.10.157:8000/v1beta3/projects/sample-ds
	at java.net.URI$Parser.fail(URI.java:2848)
	at java.net.URI$Parser.checkChars(URI.java:3021)
	at java.net.URI$Parser.checkChar(URI.java:3031)
	at java.net.URI$Parser.parse(URI.java:3047)
	at java.net.URI.<init>(URI.java:588)
	at com.google.datastore.v1beta3.client.DatastoreFactory.validateUrl(DatastoreFactory.java:119)
	... 43 more
````

But if you use 0.1.6 it PASSES:

    mvn -f pom-0.1.6.xml clean test -Dgcp_ip=127.0.0.1


# Setup docker-machine test

## 1. Build image

	docker build -t gcd-dev .
	docker run --rm -p 8000:8000 gcd-dev

## 2. Start docker ds

	docker run --rm -p 8000:8000 gcd-dev

## 3. Run tests

Below assumes docker-machine is called 'dev':

    mvn clean test -Dgcp_ip=`docker-machine ip dev`
    
It fails.

Output from v1beta3 is:

````
May 06, 2016 12:59:36 AM com.google.appengine.tools.development.LocalResourceFileServlet doGet
WARNING: No file found for: /v1beta3/projects/sample-ds:lookup
````

But if you use 0.1.6 it passes:

    mvn -f pom-0.1.6.xml clean test -Dgcp_ip=`docker-machine ip dev`

If you use a DNS lookup (here hardwired hostname "docker" to `docker-machine ip dev` )

    mvn clean test -Dgcp_ip=docker

It FAILS.  But will pass under 0.1.6

    mvn -f pom-0.1.6.xml clean test -Dgcp_ip=docker
    

