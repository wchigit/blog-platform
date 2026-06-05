# Changelog

## 2026-06-04
- Fixed local Docker + Spring Boot startup failure caused by schema mismatch between Flyway migration types and JPA entity IDs.
- Updated `V1__initial_schema.sql` to use `BIGSERIAL`/`BIGINT` for primary and foreign keys so it matches entity `Long` IDs.
- Verified Docker Compose database startup and identified the correct local run command for PowerShell environments.
