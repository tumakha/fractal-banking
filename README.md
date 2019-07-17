## Fractal Banking API

Fractal Banking REST API - test task

#### Prerequisites

Java 8, Gradle 5 or gradle-wrapper

#### Run/Debug main class

    fractal.banking.WebApp
    
#### Build

    gradle build

#### Run web app

    cd ./rest-server/build/libs
    java -jar fractal-banking.jar

## Architecture overview

Project divided into 3 modules

- **fractal-sdk** - Fractal API rest client implementation using Retrofit2/OkHttp3
- **categorization-service** - Transaction categorization by description and merchant
- **rest-server** - Spring Boot 2 REST server implementation with spring-data-jpa repositories.

#### REST API documentation

Swagger REST UI is available by link http://localhost:8888

Synchronize transactions from Fractal API

    PUT /v1/bank/{bankId}/account/{accountId}/transaction/sync

then following endpoint will return categorized transactions    
    
    GET /v1/bank/{bankId}/account/{accountId}/transaction
    
Fractal Sandbox test values    

- bankId: 6
- accountId: fakeAcc, fakeAcc3, fakeAcc111

#### Data Source

H2 in-memory database is used as spring-data source to simplify run and build

#### Future improvement

Replace CategorizationService implementation with Word2Vec deep learning neural network model 
using **deeplearning4j**

https://github.com/deeplearning4j/dl4j-examples/tree/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/nlp/word2vec

https://www.kaggle.com/c/word2vec-nlp-tutorial/overview/part-2-word-vectors