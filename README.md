# Getting Started

### Context
This project provides a solution to the _Gilded Rose_ coding exercise using the Java language and its Spring framework. For more info on the requirements, please consult the [Coding Exercise SDE 2019 (4).docx](https://github.com/panovic/gildedrose/blob/master/Coding%20Exercise%20SDE%202019%20(4).docx) document.

### Build Instructions
To build the app and run its unit-tests, execute the following command from the root folder:
```bash
mvn clean test
```

### Run Instructions
To run this Java Spring Boot app, execute the following command from the root folder and leave the app running:
```bash
mvn spring-boot:run
```

### Test Instructions
#### Getting a List of Items (GET /items)
To get a list of items from the app, execute the following `curl` command or use an equivalent tool (i.e. postman) to send a similar `get` request to the server that was started in the previous step (_Run Instructions_):
```bash
curl http://localhost:8080/items
```
Note that executing the above command more than 10 times in less than an hour (configurable), the prices for the items will be increased by 10%. 

#### Buying an Item (POST /purchases)
To purchase an item from the list returned by the previous command (note: only a single item can be purchased at a time), execute the following `curl` command or use an equivalent tool (i.e. postman) to send a `POST /purchases` request to the server:
```bash
curl -X POST "http://localhost:8080/purchases"  --data '{"itemId": 3, "itemQuantity": 10}' -H "Content-Type: application/json" -H "x-apikey: 5af08d534f904e84803c1942473b453d"
```
Note that after purchasing the item, its quantity will be reduced accordingly (i.e. if original stock quantity was 100 and if the item was purchased with quantity of 10, the new stock quantity will be reduced to 90) which can be checked by running the `GET /items` command again.

Also, note that for the `POST /purchases` command you need to "authenticate" yourself using the `api key` provided as a  header parameter. Otherwise, you will be forbidden to make the purchase (403 error code will be returned) like in the example below:
```bash
curl -X POST "http://localhost:8080/purchases"  --data '{"itemId": 3, "itemQuantity": 10}' -H "Content-Type: application/json"
```
```json
{"timestamp":"2020-05-15T13:16:37.626+0000","status":403,"error":"Forbidden","message":"Access Denied","path":"/purchases"}
```

Finally, there was no requirement to display purchases, so no corresponding API was provided (note: you would need to look into its database to see purchases made).


### Design Notes
The exercise was implemented as a Java app using the Spring framework (REST, DATA JPA, etc.). The design follows a typical `controller` -> `service` -> `repository` layered architecture with `model` and `dto` classes used where needed. To speed up the development, Spring Boot was used extensively in some areas (like repository where hardly any code was written).


#### Architecture
There are 2 REST API controllers exposed at the following endpoints (as already "previewed" in the previous section):
- `GET /items` - retries the list of items with basic info (id, name, desc, price and available quantity) in the following JSON format:
```json
[{"id":4, "name":"Big Rock Honey Brown", "description":"Honey Brown Amber Lager", "price":8, "quantity":100}]
```

- `POST /purchases` - submits the purchase request for a specific item (by `id`) for a given `quantity` and makes sure the user is authenticated with the API Key mechanism (provided in the `x-apikey` header):
```bash
curl -X POST "http://localhost:8080/purchases"  --data '{"itemId": 3, "itemQuantity": 10}' -H "Content-Type: application/json" -H "x-apikey: 5af08d534f904e84803c1942473b453d"
```

`JSON` is used as the data format for POST requests and GET responses as a widely used data format that is easily supported by many toolsets and user friendly format for humans to view and understand the data when playing with the API.  

#### Data Model
The basic data model consists of the following entities:
- `View (id, dateTime)`: used for keeping track (datetime) of the number of times items were viewed. Note that this entity is not specific to an item but is updated whenever all items are retrieved (since there's no separate API to retrieve individual items),
- `Item (id, name, description, price, quantity)`: contains basic information about items including the price and stock quantity where the stock quantity is being reduced with every purchase until it's depleated when the app will start rejecting purchase orders complaining about being out of stock,
- `Purchase (id, userId, itemId, price, createdDateTime)`: contains information about an item purchase including which user made the purchase, at what price (since it could differ from the price in the `item` table due to utilizing the surge model) and purchased quantity,
- `User (id, firstName, lastName, apiKey)`: contains basic info about users including their `apikey` which is used for authentication purposes.

`In-memory` database (H2) was used for repository implementation since external databases were not required. This allowed for easy troubleshooting and provided enough confidence that switching to an external DB in future would be seamless.

Data is pre-populated with 3 items (my favourite beer) and a single user whose API Key would need to be used for making purchases as shown in the example above. For how data is prepopulated see [LoadDatabase.java](https://github.com/panovic/gildedrose/blob/master/src/main/java/com/example/gildedrose/configuration/LoadDatabase.java).
  


#### Pricing Logic
Each time items are "viewed" (that is, retrieved by the `GET /items` operation), the system keeps track of these "views" and adjusts the prices retrieved if needed (i.e. if more than 10 "views" in last hour, the item price retrieved will be increased by 10%). The same logic is used when processing a purchase order at which time the same calculation is applied to item price which will be adjusted if needed. Note that there's a chance that the prices "shown" when the items where being "viewed" could differ from the final purchase price if there's a bigger time gap between "viewing" and "purchasing" an item. This is similar to how some online payment systems behave when making final calculations of currency conversion rates and similar scenarious.

The parameters involved in this calculation have been externalized to the [application.properties](https://github.com/panovic/gildedrose/blob/master/src/main/resources/application.properties) configuration file.


#### Security
To simplify the implementation, the __api key__ authentication mechanism is being utilized by this app. Although still in wide use in lots of REST APIs out there, this mechanism is not considered most secure and upgrading to more secure options like oAuth2 is recommended. However, those approaches were not implemented due to complexity (require external services, etc.). 

Hence, users who want to purchase items with this app would need to specify an api key provided to them by the merchant in a form of `x-apikey` request header as shown in the previous sections. If no api key is provided when making a purchase, the system will reject the purchase request with `403 (forbidden)` error code as shown below:
```bash
curl -X POST "http://localhost:8080/purchases"  --data '{"itemId": 3, "itemQuantity": 10}' -H "Content-Type: application/json"
```
```json
{"timestamp":"2020-05-15T13:16:37.626+0000","status":403,"error":"Forbidden","message":"Access Denied","path":"/purchases"}
```

The __Spring Security__ module was used for ensuring that only the authenticated users may purchase items. 

#### Testing Notes
Unit tests were created for all major components having behaviour/logic under the [test/java/com/example/gildedrose](https://github.com/panovic/gildedrose/tree/master/src/test/java/com/example/gildedrose) package. Dummy classes without any logic like Exception classes, model/dto, etc were not being unit-tested. The code coverage for units with behaviour/logic that made sense to test is high while it is significantly less in case the components didn't have anything meaningful for unit-testing. Special attention was paid to edge cases as well as important business scenarious (i.e. surge pricing, etc.). Note that all three layers were being unit-tested (controller, service, and repository) You may run unit-test with the following command:
```bash
mvn clean test
```
