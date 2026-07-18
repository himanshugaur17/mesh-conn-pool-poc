# mesh-conn-pool-poc

Local POC for stressing Envoy's upstream connection pool between a Spring Boot caller and WireMock.

## Topology

```text
client -> Spring Boot :8080 -> Envoy :10000 -> WireMock :8080
                         Envoy admin :19000
```

Spring's Apache HttpClient pool is intentionally set high so Envoy is the bottleneck:

```text
Java maxTotal/defaultMaxPerRoute: 200
Envoy max_connections: 2
Envoy max_pending_requests: 1
WireMock fixed delay: 2000 ms
```

## Run

```bash
docker compose up -d --build
curl http://localhost:8080/call
```

## Stress

```bash
python3 scripts/stress.py --requests 20 --concurrency 20
```

Expected result: a mix of `200` and `503`. The `503` responses are Envoy overflow behavior passed through by the Spring controller.

Useful Envoy stats:

```bash
curl 'http://localhost:19000/stats?filter=cluster.wiremock_backend_pool.upstream_rq_pending_overflow'
curl 'http://localhost:19000/stats?filter=cluster.wiremock_backend_pool.upstream_cx'
```

## Stop

```bash
docker compose down
```
