# YatraIndia Authentication and Security Testing

## 1. Purpose

This document records authentication and security configuration checks for the YatraIndia backend.

## 2. Scope

- Customer registration and login
- Authentication and authorization rules
- Application health endpoints
- Backend automated tests
- Frontend production build

## 3. Public Endpoints

The current security configuration permits unauthenticated access to:

- `/api/health`
- `/actuator/health`
- `/actuator/health/**`
- `/api/auth/register`
- `/api/auth/login`
- `/error`

Other endpoints remain subject to the remaining Spring Security rules. Public health access must not expose unrelated Actuator endpoints.

## 4. Secret Management

- JWT configuration uses `YATRAINDIA_JWT_SECRET`.
- The database password uses `YATRAINDIA_DB_PASSWORD`.
- Secret values must never be committed to source control.
- MySQL remains the application's database.
- Review authorization whenever API endpoints are added.

## 5. Verification Results

| Check | Result |
|---|---|
| Authentication integration tests | 7 passed |
| Full backend test suite | Build succeeded; 38 tests across five test classes |
| Frontend production build | Passed; 84 modules transformed |
| `git diff --check` | No whitespace errors reported; line-ending warning observed |

These results describe the executed test run and do not replace additional security testing before production deployment.

## 6. Recommended Security Checks

Before production deployment, verify that:

- Invalid registration and login input is handled appropriately.
- Protected endpoints reject unauthenticated requests.
- Invalid and expired JWTs are rejected.
- Users cannot access another user's private travel history or bookings.
- Administrative endpoints require the intended role.
- Public health endpoints expose only intended information.
- Deployment secrets are configured securely.
- Error responses do not reveal passwords, tokens, or internal details.

## 7. Running Backend Tests

From the `backend` directory, run:

```powershell
.\mvnw.cmd test
```

Configure the required database password and JWT secret environment variables before running tests that need them. Never put real secret values in this document or commit them to source control.
