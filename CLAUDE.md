# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```shell
# Full build (skip tests)
mvn clean package -DskipTests

# Run code formatter
mvn spotless:apply

# Check formatting (runs automatically during verify phase)
mvn spotless:check

# Run tests
mvn test

# Run a single test
mvn test -Dtest=ClassName#methodName -pl naive-rpc-netty
```

## Project Structure

```
naive-rpc (parent pom)
├── naive-rpc-core       # Core interfaces, SPI, and abstractions
├── naive-rpc-netty     # Netty transport, serialization, proxy implementations
├── naive-rpc-spring    # Spring integration (@EnableNaiveRpc, @NaiveRpcService, @NaiveRpcReference)
├── naive-rpc-spring-boot # Spring Boot auto-configuration
└── naive-rpc-demo      # Demo applications
```

**Dependency chain:** `naive-rpc-spring` → `naive-rpc-netty` → `naive-rpc-core`

## Architecture

### Core SPI Interfaces (naive-rpc-core)

| Interface | Purpose |
|-----------|---------|
| `RpcAccessPoint` | Main entry point - manages server lifecycle, NameService, service registration, proxy creation |
| `Serializer` | Serialization abstraction with `serialize()`/`deserialize()` |
| `LoadBalance` | Load balancing with `select(List<String>)` |
| `NameService` | Service registry (register/lookup addresses) |
| `Transport` | Sends `Command` requests asynchronously, returns `CompletableFuture<Command>` |
| `RequestHandler` | Processes incoming `Command` requests |
| `StubFactory` | Creates proxy stubs for service interfaces |

### SPI Mechanism

- `@SPI` annotation marks extensible interfaces (RpcAccessPoint, Serializer, LoadBalance, NameService, etc.)
- `ServiceSupport<T>` loads implementations via `java.util.ServiceLoader`
- `META-INF/services/` files in `naive-rpc-netty` register implementations

### Serialization Types (naive-rpc-netty)

| ID | Type |
|----|------|
| 1 | nativejava |
| 2 | java |
| 3 | metadata |
| 4 | gson |
| 5 | kryo |

### Request Flow

1. Consumer obtains proxy via `RpcAccessPoint.createProxy()`
2. Proxy queries `NameService` for provider address
3. Proxy creates `Transport` via `TransportClient`
4. `Transport.send()` sends `Command` (Header + RpcRequest) asynchronously
5. Server's `RequestHandler` receives request, looks up service via `ServiceProviderRegistry`, invokes via reflection
6. Response `Command` returned via `CompletableFuture`

### Spring Integration

- `@EnableNaiveRpc` - enables RPC component scanning
- `@NaiveRpcService` - exports a service implementation
- `@NaiveRpcReference` - injects a remote service reference

## Tech Stack

- Java 21
- Netty (network transport)
- Spring Framework 6 / Spring Boot 3
- SPI for extensibility
- Palantir Java Format (spotless)
