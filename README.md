# Sistemas Distribuidos — Prácticas de RPC

**ESIME Culhuacán, IPN — Ingeniería en Computación, 8.º semestre**
Antonio de Jesús Torres Martínez

Tres prácticas de laboratorio sobre la arquitectura de llamada a procedimientos remotos (RPC), con un reporte técnico integrador único.

## Estructura

| Carpeta | Contenido |
|---|---|
| [`pcta1/`](pcta1/) | **Práctica 1 — RPC con CGI y Shell script sobre nginx.** `Dockerfile` (fedora:36 + nginx + fcgiwrap), scripts `saludo.cgi` y `calc.cgi` (reto: multi-operación con claves de idempotencia), configuración de nginx. |
| [`pcta2/`](pcta2/) | **Práctica 2 — Java RMI.** Interfaz remota, `CalculadoraImpl`, `BitacoraRemota` (reto: bitácora persistente), RMI sobre TLS con certificado autofirmado, `LatencyBench` y evidencia de tráfico (`.pcap`, hexdump del handshake TLS). |
| [`pcta3/`](pcta3/) | **Práctica 3 — RPC heterogéneo Java ↔ nginx ↔ CGI/Shell.** Gateway nginx (FastCGI + reverse proxy a JVM), cliente Java con reintentos/backoff/idempotencia, y `docker-compose.yml` con Prometheus + Grafana (reto de observabilidad). |
| [`informe/`](informe/) | **Reporte técnico integrador** en LaTeX (`informe/main/main.pdf`): marco teórico, desarrollo de las tres prácticas con capturas y análisis, cuestionarios de cierre, retos de extensión, comparación de latencia RMI vs HTTP/JVM vs HTTP/CGI, glosario y bitácora. |

## Ejecución rápida

```bash
# Práctica 1
cd pcta1 && docker build -t pcta1-rpc-cgi . && docker run -d -p 8080:80 pcta1-rpc-cgi
curl 'http://localhost:8080/rpc/saludo.cgi?nombre=Mundo'

# Práctica 2 (dos nodos)
cd pcta2 && docker build -t pcta2-rmi .
docker network create pcta2-net
docker run -d --name rmi-servidor --network pcta2-net pcta2-rmi sleep infinity
docker run -d --name rmi-cliente  --network pcta2-net pcta2-rmi sleep infinity
docker exec -d rmi-servidor bash -c 'cd /rmi-lab/src && java $RMI_TLS_OPTS -Djava.rmi.server.hostname=rmi-servidor mx.ipn.esimecu.rpc.ServidorRMI'
docker exec rmi-cliente bash -c 'cd /rmi-lab/src && java $RMI_TLS_OPTS mx.ipn.esimecu.rpc.ClienteRMI rmi-servidor'

# Práctica 3 (stack completo con observabilidad)
cd pcta3 && docker compose up -d --build
curl 'http://localhost:8082/api/v1/cotizar?divisa=USD&monto=100'
curl 'http://localhost:8082/api/v1/saldo'
# Grafana: http://localhost:3000  ·  Prometheus: http://localhost:9091
```

El reporte compila con `latexmk -pdf main.tex` desde `informe/main/` (requiere TeX Live con biber).
