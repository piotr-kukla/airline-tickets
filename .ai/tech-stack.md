# Stack technologiczny (na podstawie kodu i konfiguracji)
## Backend
1. Język/runtime: Scala 3.7.4 na JVM 21 (Docker base: Temurin 21).
2. HTTP API: Tapir + Netty (endpointy jako opis + generacja OpenAPI/Swagger UI).
3. HTTP client / integracje: sttp client4 (w tym integracje OTEL i JSON).
4. JSON: jsoniter-scala (makra do codeców).
5. Baza danych: PostgreSQL + JDBC + HikariCP (pool).
6. Migracje: Flyway.
7. Dostęp do DB: Magnum (type-safe DB client).
8. Konkurencja i procesy: Ox (structured concurrency, scopes, sleep, itp.).
9. Konfiguracja: pureconfig.
10. Logowanie: SLF4J + Logback (+ mostki z JUL).
11. Obserwowalność: OpenTelemetry (metryki/tracing/logs eksportowane przez OTLP).
12. Bezpieczeństwo: hashowanie haseł password4j; autoryzacja oparta o API keys w Authorization: Bearer ....
13. E-mail: wysyłka przez SMTP lub Mailgun (plus „dummy sender” do dev/test).
14. DI / kompozycja: MacWire (compile-time DI).
15. Testy: ScalaTest, embedded Postgres.

## Frontend
1. Język: TypeScript.
2. Framework: React 19.
3. Routing: react-router v7.
4. Dane/API: @tanstack/react-query + typy i klient generowane z OpenAPI (openapi-codegen).
5. Formy i walidacja: react-hook-form + zod.
6. (Dokument docs/stack.md wspomina o Formik/Yup, ale aktualny ui/package.json jest na RHF/Zod.)
7. UIzD: Radix UI, shadcn-style komponenty w ui/src/components/ui/*, Tailwind.
8. Build: Vite (+ SWC plugin).
9. Testy: Vitest + Testing Library, jsdom.
10. Lint/format: ESLint + Prettier.
11. Node: wymagany >= 22.
12. Infra / uruchamianie
13. Docker Compose: backend + Postgres 17.5 + grafana/otel-lgtm (collector + Prometheus/Loki/Tempo/Grafana).
14. Helm chart: deployment na Kubernetes + zależność na Bitnami PostgreSQL.
