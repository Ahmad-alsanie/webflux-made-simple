# WebFlux Made Simple
Application that demonstrates the capabilities of spring webFlux covering basic concepts such as endpoints, controllers vs. handler to advanced topics such as SSE, backpressure and webclient.

## What you will find here
Aside from demonstrating the core and advanced concepts, we will also cover testing, basic authentication, deployment and containerization.


## Prerequisites
- Java >= 17
- Maven
- Docker
- MongoDB

### Basic Concepts
Under `MadeSimpleController` you will see an example of how we used mono and flux.

- Mono: Represents a single or empty asynchronous value. It's useful for async operations that return at most one value.
- Flux: Represents a sequence of 0..N elements, so it's suitable for async operations that can return multiple values (e.g., streaming data).

### Introduction to Functional Endpoints
In ```GreetingsHandler``` and ```GreetingRouter``` we will use a more functional approach. It is particularly preferred when you want a more explicit routing configuration. 
It separates the route definitions from the business logic.

The setup of our router and handler demonstrates a functional way of handling requests, where you define a router that directs HTTP requests to specific handlers based on the route.

#### Annotation vs. Functional Endpoints
The annotation-based approach is quick and declarative, suitable for straightforward mappings. 
The functional approach provides more control and explicitness, used for complex routing.

### Implementing CRUD Operations with WebFlux
We'll demonstrate this by creating a simple reactive application that manages a collection of items.
Where we have ```Product``` model class, ```ProductRepository``` to interact with our MongoDb and a ```ProductController``` to expose the following endpoints:

- POST /products to create new products.
- GET /products to list all products.
- GET /products/{id} to view a specific product.
- PUT /products/{id} to update a product.
- DELETE /products/{id} to delete a product.

### Implementing Server-Sent Events in Spring WebFlux
Server-Sent Events allow a server to push real-time updates to the client over an HTTP connection. This feature is useful for applications that require real-time data updates, such as live notifications, stock prices updates, or any streaming data.

To demonstrate SSE, we'll create a simple endpoint through ```StreamController``` that streams date and time updates to the client every second.

- The produces = MediaType.TEXT_EVENT_STREAM_VALUE attribute in @GetMapping specifies that this endpoint produces an event stream.
- Flux.interval(Duration.ofSeconds(1)) generates a sequence that emits long values starting with 0 and incrementing at specified time intervals (every second in this case).

#### Common use-cases of SSE
- Real-time notifications: Informing users about new messages, updates, or events as soon as they occur.
- Live feeds: Streaming live data such as news updates, sports scores, or social media feeds.
- Monitoring dashboards: Displaying real-time metrics or logs for system monitoring tools.

#### Best Practices and Considerations
- Client Reconnection: Browsers automatically try to reconnect to the SSE stream if the connection is lost. However, you should handle reconnections gracefully in non-browser clients.
- Error Handling: Implement error handling in your stream to manage any exceptions that may occur during data generation or processing.
- Resource Management: Be mindful of resource usage, as keeping many connections open simultaneously can be resource-intensive.


### Backpressure
In reactive streams, backpressure is a mechanism that allows subscribers to signal to producers how much data they are prepared to process, preventing them from being overwhelmed.

#### Implementing Backpressure
Spring WebFlux, built on Project Reactor that inherently supports backpressure through its reactive types Flux and Mono.
Backpressure allows consumers to control the flow of data, requesting more data only when they are ready to process it.

Take a look at StreamController `/data-stream` endpoint and `DataStreamClient`

### WebClient
Provides a way to perform HTTP operations asynchronously and supports both synchronous and asynchronous request processing.

Take a look at ```DataStreamClient```