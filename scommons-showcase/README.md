## scommons-showcase
Example client/server application that demonstrates scommons components.

#### How to Build

To build and run tests use the following command:
```bash
sbt "project scommons-showcase-server" test it:test
```

#### How to Run Showcase/Demo Server

To start the application server locally, use the following command:
```bash
sbt "project scommons-showcase-server" run
```

#### Showcase Client UI

To see the Showcase Client UI with all the components in browser:
```
http://localhost:9000/scommons-showcase
```

#### REST API Documentation

To see the Swagger REST API documentation page and try endpoints in browser:
```
http://localhost:9000/scommons-showcase/swagger.html
```
