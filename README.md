# Recipe Book Service

A Quarkus-based service to manage a recipe book with recipes and ingredients stored in MongoDB and secured with Keycloak. Includes full CRUD, OpenAPI docs, health checks, database migrations (Liquibase for MongoDB), unit tests, and container setup for Docker/Podman and suitable for K8s.

## Features
- MongoDB with Panache repositories
- Liquibase-MongoDB migrations (collections + seed data)
- Keycloak OIDC security (DevServices and docker-compose)
- CRUD endpoints
  - GET/POST/PUT/DELETE /api/ingredients
  - GET/POST/PUT/DELETE /api/recipes
- OpenAPI at /q/openapi and Swagger UI at /q/swagger-ui
- Health endpoints at /q/health

## Run in Dev
- Start Quarkus with live reload:
  ./gradlew quarkusDev
- DevServices will start MongoDB and a Keycloak container automatically. Dev UI: http://localhost:8080/q/dev

## Run Tests
./gradlew test

Tests mock an admin identity via Quarkus Test Security so security constraints are honored without external Keycloak.

## Containerized Setup (Docker/Podman)
- Build the app image and start infra + app:
  docker compose up --build
- Services:
  - App: http://localhost:8080
  - MongoDB: localhost:27017
  - Keycloak: http://localhost:8081
- Preloaded Keycloak realm: src/main/resources/keycloak/realm-export.json
  - Realm: recipe-realm
  - Client: recipe-book-service (secret: secret)
  - Users:
    - admin/admin (role: admin)
    - user/user (role: user)

The app is configured via env/JVM opts in docker-compose to connect to MongoDB and Keycloak.

## Configuration
See src/main/resources/application.properties. Important values:
- quarkus.mongodb.database=recipebook
- quarkus.liquibase-mongodb.migrate-at-start=true
- quarkus.oidc.auth-server-url (defaults to http://localhost:8081/realms/recipe-realm)
- quarkus.oidc.client-id=recipe-book-service
- quarkus.oidc.credentials.secret=secret

For production, set:
- MONGODB_CONNECTION_STRING=mongodb://<host>:27017
- OIDC_AUTH_SERVER_URL=https://<keycloak>/realms/recipe-realm
- OIDC_CLIENT_SECRET=<secret>

## API Examples
- Create Ingredient (admin):
  POST /api/ingredients
  {"name":"Butter","type":"DAIRY","caloriesPer100g":717}
- Create Recipe (admin):
  POST /api/recipes
  {"name":"Scrambled Eggs","category":"BREAKFAST","servings":2,
   "ingredients":[{"ingredientId":"<ingredientId>","amountGrams":100}],
   "steps":["Beat eggs","Cook"]}

## Example with authentication

### 1. Retrieve the token

POST http://localhost:8081/realms/recipe-realm/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials&client_id=recipe-book-service&client_secret=nCN8WWTwSsdSBg1g2aBstZlaEySW0HjY

> {% client.global.set("auth_token", response.body.access_token); %}

### 2. Call to the protect API

GET http://localhost:8080/api/ingredients
Authorization: Bearer {{auth_token}}

## Kubernetes
This app uses standard Quarkus images and properties and can be containerized with the provided Dockerfile. You can convert docker-compose to K8s manifests via Kompose or author manifests/Helm charts. Ensure secrets and env vars are configured in your cluster.
