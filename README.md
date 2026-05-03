# distributed-workflow-with-temporal

## Prerequisites 
- JDK 17+
- Maven 3.9+
- Docker / Docker Compose 


## 1) Start Temporal Server + UI 

```bash 
docker compose up -d 
```

- **Temporal Web UI**: http://localhost:8080
- **Frontend gRPC**: `localhost:7233`

Wait until containers are healthy (`docker compose ps`)

## 2) Run the Worker(terminal A)

```bash 
cd temporal-task-distribution 
mvn -q compile exec:java -Dexec.mainClass=com.tus.coupon.temporal.prototype.worker.CouponTaskWorker 
```

## 3) Start a workflow (terminal B)
Immediate run (`sendTime` = now):

```bash 
mvn -q compile exec:java \
    -Dexec.mainClass= \
    -Dexec.args="--delay-seconds 15"
```

Custom task id:

```bash 
mvn -q compile exec:java \
  -Dexec.mainClass= \
  -exec.args="--task-id 9001 --delay-seconds 5"
```


## 4) Observe in Temporal UI 
- 1. Open http://localhost:8080
- 2. Namespace: **default**
- 3. Search workflow id: `coupon-task-<taskId>` (deafult task id is `10001` if not overridden)
- 4. Inspect **History** for timers and Activity attempts. 

## 5) Stop 
- Worker: Ctrl + C
- Stack: `docker compose down -v` (with `-v` to drop Postgres volumes if needed)

## Environment variables 

| Variable | Default | Description |
|----------|---------|-------------|
| `TEMPORAL_TARGET` | `127.0.0.1:7233` | Temporal frontend address |
| `TEMPORAL_NAMESPACE` | `default` | Temporal namespace |
| `TEMPORAL_TASK_QUEUE` | `coupon-task-queue` | Worker task queue |



