# Spring Boot Kafka example client API
Example of Spring Boot Kafka client consist of Rest API that invoke Kafka request reply consumer. 

## How to use
**1. Clone the application**

```bash
git clone https://github.com/harvanir/example.git
```

**2. Download & install kafka**

```bash
https://kafka.apache.org/downloads
https://kafka.apache.org/quickstart
```

**3. Build the application**

```bash
mvn clean install
```

**4. Go to kafka directory**

```bash
localDrive>cd D:\app\kafka_2.12-1.1.0
```

**5. Run the kafka server**

```bash
D:\app\kafka_2.12-1.1.0>.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
D:\app\kafka_2.12-1.1.0>.\bin\windows\kafka-server-start.bat .\config\server.properties
```

**6. Run the spring boot application**

```bash
java -jar -Dserver.port=8080 target/springboot-kafka-example-client-api-0.0.1-SNAPSHOT-exec.jar
```

**7. Access the application via web browser (will handle by kafka consumer)**

```
POST: http://localhost:8080/sum/{loop}

BODY:
{
	"firstNumber": 1,
	"secondNumber":2
}
```

**8. Access the application via web browser (will handle by Rest API)**

```
POST: http://localhost:8080/sum2/{loop}

BODY:
{
	"firstNumber": 1,
	"secondNumber":2
}
```
