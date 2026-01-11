# airline

![Version: 0.3.0](https://img.shields.io/badge/Version-0.3.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 1.0](https://img.shields.io/badge/AppVersion-1.0-informational?style=flat-square)

A Helm chart for Airline

**Homepage:** <https://softwaremill.github.io/airline/>

## Installation

### Add Helm repository

```
helm repo add softwaremill https://charts.softwaremill.com/
helm repo update
```

## Fetch and Customize Airline chart
```
helm fetch softwaremill/airline --untar
```

## Install Airline chart

```
helm install --generate-name airline
```

## Configuration

The following table lists the configurable parameters of the chart and the default values.

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| airline.affinity | object | `{}` |  |
| airline.fullnameOverride | string | `""` |  |
| airline.image.pullPolicy | string | `"Always"` |  |
| airline.image.repository | string | `"softwaremill/airline"` |  |
| airline.image.tag | string | `"latest"` |  |
| airline.ingress.annotations."kubernetes.io/ingress.class" | string | `"nginx"` |  |
| airline.ingress.annotations."kubernetes.io/tls-acme" | string | `"true"` |  |
| airline.ingress.enabled | bool | `true` |  |
| airline.ingress.hosts[0].host.domain | string | `"airline.example.com"` |  |
| airline.ingress.hosts[0].host.path | string | `"/"` |  |
| airline.ingress.hosts[0].host.pathType | string | `"ImplementationSpecific"` |  |
| airline.ingress.hosts[0].host.port | string | `"http"` |  |
| airline.ingress.tls[0].hosts[0] | string | `"airline.example.com"` |  |
| airline.ingress.tls[0].secretName | string | `"airline-tls"` |  |
| airline.ingress.tls_enabled | bool | `false` |  |
| airline.java_opts | string | `"-XX:MaxRAMPercentage=60"` |  |
| airline.liveness_initial_delay | int | `60` |  |
| airline.logback_json_encode | bool | `false` |  |
| airline.nameOverride | string | `""` |  |
| airline.nodeSelector | object | `{}` |  |
| airline.otel.enabled | bool | `false` |  |
| airline.otel.endpoint | string | `""` |  |
| airline.otel.metric_export_interval | string | `"60s"` |  |
| airline.otel.protocol | string | `""` |  |
| airline.otel.service_name | string | `"airline"` |  |
| airline.readiness_initial_delay | int | `60` |  |
| airline.replicaCount | int | `1` |  |
| airline.reset_password_url | string | `"https://airline.example.com/password-reset?code=%s"` |  |
| airline.resources | object | `{}` |  |
| airline.service.port | int | `8080` |  |
| airline.service.type | string | `"ClusterIP"` |  |
| airline.smtp.enabled | bool | `true` |  |
| airline.smtp.from | string | `"hello@airline.example.com"` |  |
| airline.smtp.host | string | `"server.example.com"` |  |
| airline.smtp.password | string | `"airline"` |  |
| airline.smtp.port | int | `465` |  |
| airline.smtp.ssl | string | `"true"` |  |
| airline.smtp.ssl_ver | string | `"false"` |  |
| airline.smtp.username | string | `"server.example.com"` |  |
| airline.sql.host | string | `"{{ .Values.postgresql.fullnameOverride }}"` | Value will be taken from 'postgresql.fullnameOverride' setting |
| airline.sql.name | string | `"{{ .Values.postgresql.auth.database }}"` | Value will be taken from 'postgresql.postgresqlDatabase' setting |
| airline.sql.password | string | `"{{ .Values.postgresql.auth.password }}"` | Value will be taken from 'postgresql.postgresqlPassword' setting |
| airline.sql.port | string | `"{{ .Values.postgresql.service.port }}"` | Value will be taken from 'postgresql.service.port' setting |
| airline.sql.username | string | `"{{ .Values.postgresql.auth.username }}"` | Value will be taken from 'postgresql.postgresqlUsername' setting |
| airline.tolerations | list | `[]` |  |
| postgresql.auth.database | string | `"airline"` | Database name for Airline |
| postgresql.auth.password | string | `"airline"` | Password for PostgreSQL user |
| postgresql.auth.username | string | `"postgres"` | Username for PostgreSQL user |
| postgresql.connectionTest.image.pullPolicy | string | `"IfNotPresent"` |  |
| postgresql.connectionTest.image.repository | string | `"bitnami/postgresql"` |  |
| postgresql.connectionTest.image.tag | int | `11` |  |
| postgresql.enabled | bool | `true` | Disable if you already have PostgreSQL running in cluster where Airline chart is being deployed |
| postgresql.fullnameOverride | string | `"airline-pgsql-postgresql"` |  |
| postgresql.service.port | int | `5432` |  |
