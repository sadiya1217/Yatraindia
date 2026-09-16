# YatraIndia Hotel Module - Database Design

## 1. Purpose

This document defines the database design for the Hotel Module of the YatraIndia tourism application.

The Hotel Module stores hotel information required for hotel discovery, hotel details and future booking integration.

## 2. Database

Database Name: `yatraindia`

Database Technology: `MySQL`

## 3. Existing Hotel Table

The YatraIndia database contains the following table:

`hotels`

The existing table is used by the Hotel Module.

## 4. Existing Hotel Records

| ID | Hotel Name | Slug | Description |
|---|---|---|---|
| 1 | Grand Hyderabad Hotel | grand-hyderabad-hotel | A comfortable hotel in Hyderabad suitable for business and leisure travelers. |
| 2 | Heritage Palace Hotel | heritage-palace-hotel | A heritage-style hotel offering a comfortable stay in Jaipur. |
| 3 | Goa Beach Resort | goa-beach-resort | A beach resort designed for travelers exploring Goa. |

## 5. Database Verification

```sql
USE yatraindia;

SHOW TABLES;

DESCRIBE hotels;

SELECT * FROM hotels;