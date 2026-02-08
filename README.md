# gRPC Currency Services (Java + Spring Boot)

Два сервиса на Java (Spring Boot) с RPC API на gRPC.

## Сервис 1: currency-rate-provider
gRPC сервер, метод `GetUsdRubRate`, возвращает курс USDRUB.
Курс = BASE + случайное отклонение (чтобы не был константой).

## Сервис 2: rate-printer
gRPC клиент. Каждые 5 секунд вызывает сервис 1 и печатает курс в консоль.

##  gRPC
- Контракт описан в `.proto`, из него генерируется код клиента и сервера
- Синхронный вызов реализован через BlockingStub.

## Как запустить

### 1) Запуск сервера
```bash
cd currency-rate-provider
./mvnw clean package
./mvnw spring-boot:run
# grpc-currency-services
PPDPO_part2
