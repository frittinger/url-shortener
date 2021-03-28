# url-shortener
A rudimentary URL shortener service.

## Building and Running the service

### Prerequisites
- Java 11 or higher
- npm and node.js (>=14)

### Building
<pre><code>$ cd react
$ npm install
$ npm run build
$ cd ..
$ gradle copyReactApp
</code></pre>

### Running
<pre><code>$ gradle bootRun</code></pre>

Open in a browser: [localhost:8080/](localhost:8080/)

### Notes
- for this use case, a H2 in-memory database is used (instead a real one) for persistence to reduce overhead
- only URLs with less than 255 characters can be shortened, due to defaults in H2
- proper input validation is missing
- domains and ports are hard coded, i.e. defaults are used
- frontend is a basic react template with a simple form
- packaging into docker container would be useful for deployment
- superficial request mapping "/s" is needed to use default serving of static resources of spring boot
- used ShortUrl for persistence and web layer

## Design considerations

I decided to create a spring boot app, because as such it was easy to create one git repository and have everything in one 
place for submission. Besides, I felt most familiar with Java coding.

I was considering a second serverless option which was the default for us in the last two years. This would have been 
more on the configuration side and although it is my design choice, I would not know details to discuss.

- Setup AWS Amplify for the storage backend in DynamoDB
- Create a Lambda@Edge for the redirect mechanism (looking up the data in dynamoDB)
- Create a react app for the UI and parts of the application logic (creating the IDs, etc.)
