# traffic-light-system repository

Traffic Light Controller API

Scenario:
Develop an API to control a traffic light system at an intersection.

Requirements:
• The system should manage state changes of lights (red, yellow, green) for multiple directions.
• Accept commands to change light sequences, pause, or resume operation.
• Validate that conflicting directions are never green simultaneously.
• Provide the current state and timing history via the API.
• Design for concurrency and possible future expansion to multiple intersections


SWAGGER ENDPOINT
================

http://127.0.0.1:8989/api/swagger-ui/index.html


traffic-light-service
=====================

Needed REST APIs as below:

1. Create new intersection

POST /api/v1/intersections/{id}

Ex: http://127.0.0.1:8989/api/v1/intersections/testintsec123

2. Change intersection light

POST /api/v1/intersections/{id}/changelight

Ex: http://127.0.0.1:8989/api/v1/intersections/testintsec123/changelight?direction=NORTH&color=GREEN

3. Update intersection state

POST /api/v1/intersections/{id}/{state}light

Ex: http://127.0.0.1:8989/api/v1/intersections/testintsec123/pauselight
http://127.0.0.1:8989/api/v1/intersections/testintsec123/resumelight

4. Get intersection light state

GET /api/v1/intersections/{id}/lightstate

Ex: http://127.0.0.1:8989/api/v1/intersections/testintsec123/lightstate

5. Get intersection light history

GET /api/v1/intersections/{id}/lighthistory

Ex: http://127.0.0.1:8989/api/v1/intersections/testintsec123/lighthistory?pageNumber=0&pageSize=5


Used Softwares or Technologies
==============================

Java 25
Maven 3.9.12
MySQL 8
Intellij IDE 2025.2.2
Spring Boot 4.0.0

Swagger
Xslf4j Logger
Jacoco (Added Surefire for unit test & Failsafe for integration test)

Thank You.,