# YatraIndia Hotel Module - Testing Documentation

## 1. Purpose

This document defines the testing approach for the Hotel Module of the YatraIndia tourism application.

The objective is to verify that hotel data, hotel search, hotel details, and hotel-related backend functionality work correctly and integrate properly with the YatraIndia backend and database.

---

## 2. Module Under Test

**Module:** Hotel Management and Booking

**Application:** YatraIndia

**Backend:** Spring Boot

**Database:** MySQL

**Build Tool:** Maven

**Testing Approach:** Unit Testing, Integration Testing and API Testing

---

## 3. Testing Objectives

The following objectives are defined for the Hotel Module:

1. Verify that hotel information can be stored correctly.
2. Verify that hotel information can be retrieved correctly.
3. Verify hotel search functionality.
4. Verify validation of hotel information.
5. Verify database connectivity.
6. Verify API responses.
7. Verify error handling.
8. Verify integration between the backend and MySQL database.
9. Ensure existing modules are not affected by hotel functionality.

---

## 4. Functional Test Cases

| Test Case ID | Test Scenario | Expected Result |
|---|---|---|
| HOTEL-001 | Add valid hotel information | Hotel should be stored successfully |
| HOTEL-002 | Retrieve hotel information | Correct hotel details should be returned |
| HOTEL-003 | Search hotel by city | Hotels available in the selected city should be returned |
| HOTEL-004 | Search hotel by name | Matching hotel should be returned |
| HOTEL-005 | Search with invalid city | Appropriate empty result should be returned |
| HOTEL-006 | Submit hotel with missing required data | Validation error should be returned |
| HOTEL-007 | Retrieve non-existing hotel | Appropriate not-found response should be returned |
| HOTEL-008 | Verify hotel database record | Correct record should exist in MySQL |
| HOTEL-009 | Verify API response format | Response should contain expected hotel data |
| HOTEL-010 | Verify invalid request | Appropriate error response should be returned |

---

## 5. Validation Test Cases

| Test Case ID | Input | Expected Result |
|---|---|---|
| HOTEL-VAL-001 | Valid hotel name | Accepted |
| HOTEL-VAL-002 | Empty hotel name | Validation error |
| HOTEL-VAL-003 | Valid city | Accepted |
| HOTEL-VAL-004 | Empty city | Validation error |
| HOTEL-VAL-005 | Valid address | Accepted |
| HOTEL-VAL-006 | Empty required address | Validation error |
| HOTEL-VAL-007 | Valid hotel description | Accepted |
| HOTEL-VAL-008 | Invalid or missing required data | Validation error |

---

## 6. Database Testing

The Hotel Module uses MySQL for persistent storage.

Database testing should verify:

- Database connection is successful.
- Hotel table exists.
- Required columns are present.
- Hotel records are inserted correctly.
- Hotel records can be retrieved correctly.
- Invalid records are rejected when validation is applied.
- Primary key values are generated correctly.
- Database constraints work as expected.

### Database Verification

```sql
USE yatraindia;

SHOW TABLES;
