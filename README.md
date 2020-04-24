# Shopping Cart Exercise
Springboot application that calculates the price of a shopping basket.

## Local URL
I recommend using Postman to test the rest endpoints.
1) http://localhost:8080/items
```
Displays the price of each item
```
``` Rest method: GET ```

Response:
``` 
Status code: 200 OK
```
```
---In4mo Shopping Center---
     Date: 2020-04-24
     Price of each item
----------------------------
 Apple  x 1 : 25¢
 Orange x 1 : 30¢
 Banana x 1 : 15¢
 Papaya x 1 : 50¢
 Promotion on papaya: Buy 3 in price of 2 : 1€
 ```

2) http://localhost:8080/receipt
``` Takes input from user via payload and calculates the price of items purchased. ```
```Rest method: Put
Payload: {
         "numberOfApples": 2,
         "numberOfOranges": 3,
         "numberOfBananas": 1,
         "numberOfPapayas": 5
         }
```
Keep the payload in Body>Raw>JSON

Response:
``` 
Status code: 200 OK
 ```
```
---In4mo Shopping Center---
     Leborska 3B      
     80-386 Gdansk    
     NIP 123-456-56-88     
     Date: 2020-04-24
-------------------------
         Receipt         
-------------------------
Apple  2*0.25€ : 0.50€ 
Orange 3*0.3€ : 0.9€ 
Banana 1*0.15€ : 0.15€ 
Papaya 5*0.5€ : 2.0€ 
==========================
Total Price: 3.55€
```

### Tools and Technologies
* Java 11
* Maven
* Junit 4
* Spring Boot
* Postman
