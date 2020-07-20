# miw-springboot-api

This project is created to implement to springboot apis (item list api  and order api). 
Technical stack used in this project consists of:
1. Spring framework(spring-boot, spring-security, spring-boot rest)
2. Java 9
3. Maven
4. Junit 5

The application can be run from the command line without any
dependencies or database’s required to run application. (Other than maven for spring boot dependencies  and packaging and Java 9). There is no dependency on databases as H2(in memory database ) has been used to initialise all the test data.

## Steps to build the application

Prerequisits to build and deploy the application:
1. Java-9 should be installed
2. Maven should be installed.

steps to build the application

1. run command: git clone https://github.com/gonits/miw-springboot-api.git
2. cd to project (project checked out location)
3. run command : mvn package or mvn clean install
This will download all the dependencies and run test cases. Once test cases are successful, a jar file will be generated in target folder.

steps to run the applcation:

1. mvn spring-boot:run
 OR
1. java -jar target/server-0.0.1-SNAPSHOT.jar

## Surge Pricing:

Items Api uses surge pricing strategy to change the price of items during high demand like we see in case of using Uber or any other cab services.

Surge pricing will apply when an item has been viewed for certain number of times(e.g.10 times) in certain time period(e.g 1 hour). If this condition meets for an item, then its price will increase by certian ammount (e.g 10%). Order placed during surge window will also use increased prices to place order.

To implement surge pricing in development server, an in memory data structure (ConcurrentMap<Long,CoptOnWriteArrayList>) can be used to store all the views of each item in concurrentHashMap(thread safe). Everytime an item is viewed, this map will be updated to store the view(timestamp) and will be checked for all the views stored. The timestamps of all stored views if is within range of 1 hour and count of such views are greater than or equal to 10 then surge pricing will apply to that item.

Orders api is hit in this timeframe will also fetch the same surge price but will not update the concurrent haspmap to add a view.

## Choice of Data format:

Restful apis have been implemented using json data format for sending request data and receiving response.Json apis help to create consistent data model which can be leveraged by multiple api consumers.
JSON is a completely language-independent text format that is mainly used to transmit data between a server and a browser.Also JSON is more lightweight and less verbose format, and it’s easier to read and write as well.

Example of Request and Reponse:

Request: 
curl -X POST -d "{\"userName\": \"user\", \"password\": \"password\"}" -H "Content-Type:application/json" http://localhost:8080/auth/signIn

Respone:

{"userName":"user","token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTU5NTIyMzMxOSwiZXhwIjoxNTk1MzA5NzE5fQ.iTdVU8RGAPAdSUqJlhAWPKOY-0SeV9l2Sq7nLTFePLc"}

As it can be seen above, request has been sent using json format for username and password and response has been received in Json format containing token and username

## Authentication Mechanism
I have chosen JWT(Json web token) as authenticaton mechanism for the api calls as JWT is a self contained token which has authetication information, expire time information, and other user defined claims digitally signed.A single token can be used with multiple backends and no cookies are required to be managed and session can be created as stateless. One more advantage of this solution is that we can withdraw access to the api at any time if we verify the user’s password has been compromised by generating a new token.

## Test Cases

A total of 31 test cases have been written. Test cases include unit test cases as well as integration test cases. A few test cases for Jpa respository have also been written using DataJpaTest.

## Api Details

### Items Api

This api will return all the items available in catalogue whose quantity is greater than zero.

Api call details:
curl http://localhost:8080/items

Sample Response: [{"id":1,"name":"Fitness-Tracker-Band","description":"Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor","stock":20,"price":100},{"id":2,"name":"Cycling-Gloves","description":"Inner dazzle fabric ensures breathability and pleasantness to the hands","stock":30,"price":50}]

### Orders Api

This api will place an order for an item whose stock is available. This api call is authenticated and needs to pass an auth token in Authorization header.

Api call details:

curl -X POST -d "{\"id\": \"1\", \"quantity\": \"1\"}" -H "Content-Type:application/json" -H "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJSeWFuIFNub3ciLCJyb2xlcyI6WyJST0xFU19VU0VSIl0sImlhdCI6MTU5NTIyMzI0MiwiZXhwIjoxNTk1MzA5NjQyfQ.XBc7KZKAMzM1YlJFo1EkMYjVyiHt1-UuVswZw-HIg-k" http://localhost:8080/orders

sample respone: {"orderId":9,"itemId":1,"itemPrice":100,"quantity":1,"userName":"Ryan Snow"}

This api needs an auth token which can be generated using Authorization Api.

### Authorization Api

This api an an authorizatio api. This will take username and password as request body and will generate an auth token(Jwt token).

Api call details:

curl -X POST -d "{\"userName\": \"user\", \"password\": \"password\"}" -H "Content-Type:application/json" http://localhost:8080/auth/signIn

sample response:
{"userName":"user","token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTU5NTIyMzMxOSwiZXhwIjoxNTk1MzA5NzE5fQ.iTdVU8RGAPAdSUqJlhAWPKOY-0SeV9l2Sq7nLTFePLc"}









