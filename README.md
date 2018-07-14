# SpotHero Rate Server 

### Setup
Clone project and use mvn to build.
```mvn clean test```

Start the server with:
```mvn jetty:run```

Then you can query rates with a GET request to the rate/ endpoint with start and end dates. 

ex: ```curl -X GET 'http://localhost:8080/spothero/rate?start=2018-07-13T09:00:00Z&end=2018-07-13T18:00:00Z'```

To preload cache with rates to query against you can modify the 
init.json file located in the project root. Note that the service currently does not validate that the rates entered in this JSON are valid.

You can use the POST rate/ endpoint to add a rate to cache or to clear cache and start with a different json you can pass a list of rate objects to the POST rate/new endpoint


### API Docs
Endpoint documentation can be viewed through swagger at the endpoint:
"swagger.json" or "swagger.yaml"
ex: ```curl http://localhost:8080/spothero/swagger.json```

### Tests
Unit tests are located and in src/test package and integration tests are located in src/integration package

### Metrics
You can view endpoint metrics through the /metrics endpoint,
ex: ```curl http://localhost:8080/spothero/metrics/metrics?pretty=true```


### Deploy To Google Cloud
Use the ```mvn appengine:deploy``` command to deploy to Google Cloud.
You will have to setup Google Cloud SDK (https://cloud.google.com/sdk/docs/).
 
