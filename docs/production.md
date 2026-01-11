---
layout: default
title:  "Production deployment"
---

## Docker

To build a docker image, run `docker/Docker/publishLocal`. This will create the `docker:latest` image.

You can test the image by using the provided `docker-compose.yml` file.

## Kubernetes

Use [Helm](https://helm.sh/) to easily deploy Airline into [Kubernetes](https://kubernetes.io/) cluster.

### Add SoftwareMill Helm repository

```
helm repo add softwaremill https://charts.softwaremill.com/
helm repo update
```

### Fetch and Customize Airline chart

```
helm fetch softwaremill/airline --untar
```

### Install Airline chart

```
helm install --generate-name airline
```

Please see [Airline Helm Chart
documentation](https://github.com/softwaremill/airline/blob/master/helm/airline/README.md) for more information,
including configuration options.
