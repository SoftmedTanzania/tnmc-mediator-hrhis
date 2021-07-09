# tnmc-mediator-hrhis

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d8cec17644bc4a2c8c7c205284160c92)](https://app.codacy.com/gh/SoftmedTanzania/tnmc-mediator-hrhis?utm_source=github.com&utm_medium=referral&utm_content=SoftmedTanzania/tnmc-mediator-hrhis&utm_campaign=Badge_Grade_Settings)
[![Java CI Badge](https://github.com/SoftmedTanzania/tnmc-mediator-hrhis/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/SoftmedTanzania/tnmc-mediator-hrhis/actions?query=workflow%3A%22Java+CI+with+Maven%22)
[![Coverage Status](https://coveralls.io/repos/github/SoftmedTanzania/tnmc-mediator-hrhis/badge.svg?branch=development)](https://coveralls.io/github/SoftmedTanzania/tnmc-mediator-hrhis?branch=development)


An [OpenHIM](http://openhim.org/) mediator for handling system integration between Human Resource Health Information System (HRHIS) and Tanzania Training for International Health System(TNMC).


## 1. Dev Requirements

1. Java 1.8
2. IntelliJ or Visual Studio Code
3. Maven 3.6.3

## 2. Mediator Configuration

This mediator is designed to work with HRHIS that send GET Requests while requesting data from TNMC via the HIM.

### 3 Configuration Parameters

The configuration parameters specific to the mediator and destination system can be found at

`src/main/resources/mediator.properties`


## 4. Deployment

To build and run the mediator after performing the above configurations, run the following

```
mvn clean package -DskipTests=true -e source:jar javadoc:jar
java -jar target/tnmc-mediator-hrhis-<version>-jar-with-dependencies.jar
```
