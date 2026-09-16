\# YatraIndia – Cab Module Testing



\## 1. Purpose



This document defines the testing approach for the YatraIndia Cab Discovery Module.



The objective is to verify database integration, backend APIs, search functionality, error handling, and regression stability.



\## 2. Test Environment



\- Java: 21

\- Spring Boot: YatraIndia backend

\- Database: MySQL

\- Database name: `yatraindia`

\- Database migration tool: Flyway

\- Build tool: Maven



\## 3. Cab Module Test Cases



\### CAB-001 – Retrieve All Active Cabs



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs`



\*\*Expected Result:\*\*

HTTP 200 response with the available active cab records.



\### CAB-002 – Retrieve Cab by ID



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs/{id}`



\*\*Expected Result:\*\*

HTTP 200 response when the cab ID exists.



\### CAB-003 – Invalid Cab ID



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs/{id}`



\*\*Expected Result:\*\*

A suitable not-found response when the cab ID does not exist.



\### CAB-004 – Retrieve Cab by Slug



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs/slug/{slug}`



\*\*Expected Result:\*\*

HTTP 200 response for a valid cab slug.



\### CAB-005 – Search Cabs by City



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs/city/{city}`



\*\*Expected Result:\*\*

Cabs operating in the requested city are returned.



\### CAB-006 – Search Cabs by State



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs/state/{state}`



\*\*Expected Result:\*\*

Cabs operating in the requested state are returned.



\### CAB-007 – Search Cabs by Type



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs/type/{cabType}`



\*\*Expected Result:\*\*

Cabs matching the requested cab type are returned.



\### CAB-008 – Search Cabs by Name



\*\*Method:\*\* GET



\*\*Endpoint:\*\*

`/api/cabs/search?name={name}`



\*\*Expected Result:\*\*

Matching cab records are returned.



\## 4. Database Verification



Verify the seeded cab records:



```sql

SELECT id, name, slug, cab\_type, city, seating\_capacity, status

FROM cabs;

