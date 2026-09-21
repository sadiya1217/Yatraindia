\# M10 — Unified Booking Core



\## 1. Overview



M10 establishes the unified booking core for YatraIndia.



The booking system provides a common backend workflow for multiple

travel service types while keeping service-specific information such

as cab pickup, drop location, and passenger count.



Supported booking types:



\- HOTEL

\- CAB



\## 2. Booking Lifecycle



The supported booking statuses are:



\- PENDING

\- CONFIRMED

\- CANCELLED

\- COMPLETED



Valid status transitions:



```text

PENDING

&#x20;  ├──> CONFIRMED

&#x20;  └──> CANCELLED



CONFIRMED

&#x20;  ├──> COMPLETED

&#x20;  └──> CANCELLED



COMPLETED

&#x20;  └──> Final state



CANCELLED

&#x20;  └──> Final state



