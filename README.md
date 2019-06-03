# functional-shopping-cart

A demo of functional shopping cart using scala ,akka-http,circe and cats


This is a simple project which is actually a shopping cart.The shopping
cart has following features:

1.User can be added on the basis of some basic details such as email,
  residence address etc. 
2.Products can be added with the product code.
3.Products can be added in the shopping cart which will show the list of products
  it has,****their individual price and the total price.I have assumed currency as EUR,however
   it can be configured.
4.Check Out the products in shopping and generate an orderId,the email 
of user i assumed here to fetch items in cart, it is linked with. 


**Architecture**  

I am using the onion structure from DDD by eric evans which goes layer by layer and
paradigms of functional domain .

The communication goes like this 

RestEndPoint => Service => Repository
RestEndPoint <= Service <= Repository

Here we have different modules based on it's bounded context such as :
User,Product,ShoppingCart and CheckOut.

As a functional domain,i have used a package called domain which includes algebra
for the particular domain (It only defines the state). 
Now for the interaction of domain with real world,i have used the package called
interpreter which interprets the algebra defined in domain (It defines behavior).

The interpreter includes interpreter for Repository which includes logic for persistence
of data and it's retrieval and interpreter for Service which covers business logic
for the module.

**Tech Stack**

Scala 2.12.8 -- Functional Programming
Java 11 -- For JRE
ScalaTest -- Testing
ScalaMock -- Mocking
AkkaHttp -- RestFullApi
Cats -- FP awesomeness 
Circe -- Json serializer ,deserializer with out of the box support for implicits 
by cats and shapeless.
Macwire -- For compile time DI.

**Prerequisites:**

Sbt 1.2.4
Java 8+

**How To Run**

Good Question :) . Well following are the rest end points which consumes 
and produces JSON .
 
 The end points are as follows :
 
 http://localhost:8080/api/v1/users
 http://localhost:8080/api/v1/products
 http://localhost:8080/api/v1/shoppingcart
 http://localhost:8080/api/v1/checkout
 
 Note : Except checkout,all the end points are only POST based but it is in my
 ToDo list to create GET based end points also.
 
 Now to run, first open the terminal pointing to the root folder of this project.
 
#  Then write sbt clean compile 
#  After its successful completion,write sbt test to run the tests.
#  Then write sbt run.This should start the akka http server on 8080 port.
 
 Now just hit the end points with the request it expects and enjoy the response.
 
 **Limitations**
 
 It is a monolith application showcasing power of scala and Functional Domain Driven 
 Design to design rest based modular architecture.
 
 The modules are independent but they are communicating through services and not via endpoints.
 
 For persistence ,I am using in memory tree map which is cleaned up when server restarts.
 
 
**ToDo**

1.Migrate to micro service architecture with rest based end points as single point of communication . 
2.Refactor the search module from Service class to Rest End Point and define clients to communicate 
with them from other modules .
3.Dockerise the app . 
4.Add integration tests
5.Use real database for persistence
6.Add caching to GET end points


Last but not the least ,happy coding.


