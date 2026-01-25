Stack technologiczny (na podstawie kodu i konfiguracji)
Backend
Język/runtime: Scala 3.7.4 na JVM 21 (Docker base: Temurin 21).
HTTP API: Tapir + Netty (endpointy jako opis + generacja OpenAPI/Swagger UI).
HTTP client / integracje: sttp client4 (w tym integracje OTEL i JSON).
JSON: jsoniter-scala (makra do codeców).
Baza danych: PostgreSQL + JDBC + HikariCP (pool).
Migracje: Flyway.
Dostęp do DB: Magnum (type-safe DB client).
Konkurencja i procesy: Ox (structured concurrency, scopes, sleep, itp.).
Konfiguracja: pureconfig.
Logowanie: SLF4J + Logback (+ mostki z JUL).
Obserwowalność: OpenTelemetry (metryki/tracing/logs eksportowane przez OTLP).
Bezpieczeństwo: hashowanie haseł password4j; autoryzacja oparta o API keys w Authorization: Bearer ....
E-mail: wysyłka przez SMTP lub Mailgun (plus „dummy sender” do dev/test).
DI / kompozycja: MacWire (compile-time DI).
Testy: ScalaTest, embedded Postgres.

Frontend
Język: TypeScript.
Framework: React 19.
Routing: react-router v7.
Dane/API: @tanstack/react-query + typy i klient generowane z OpenAPI (openapi-codegen).
Formy i walidacja: react-hook-form + zod.
(Dokument docs/stack.md wspomina o Formik/Yup, ale aktualny ui/package.json jest na RHF/Zod.)
UIzD: Radix UI, shadcn-style komponenty w ui/src/components/ui/*, Tailwind.
Build: Vite (+ SWC plugin).
Testy: Vitest + Testing Library, jsdom.
Lint/format: ESLint + Prettier.
Node: wymagany >= 22.
Infra / uruchamianie
Docker Compose: backend + Postgres 17.5 + grafana/otel-lgtm (collector + Prometheus/Loki/Tempo/Grafana).
Helm chart: deployment na Kubernetes + zależność na Bitnami PostgreSQL.