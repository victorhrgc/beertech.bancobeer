# beertech.bancobeer
Legacy POC for Becks group

# Access credentials
RabbitURL = http://localhost:15672/  
SwaggerUI = http://localhost:8080/swagger-ui.html

Rabbit user: guest  
Rabbit password: guest  

Postgres user: postgres  
Postgres password: password  

# How to test locally
- Java 8 / Maven / Your favourite IDE / Git
- Ports 8080 and 8081 must be available
- Clone the repo @ https://github.com/victorhrgc/beertech.bancobeer.git
- 'docker-compose up' in the root directory to start the postgres and the rabbit container
- Start both the API(8080) and the Consumer(8081) applications on your IDE ('BancoBeerApiApplication.java' and 'BancoBeerConsumerApplication.java')
- Access the API methods with either swagger (everything) or rabbitMQ (transactions)

# How to post a message on RabbitMQ
- Go to 'Exchanges' and click on the exchange named 'transactions.consumer.exchange'
- Go to the 'Publish Message' section and add a ROUTING KEY and PAYLOAD for the desired transaction as follows:
  - deposit.rk    <-> {"accountCode":"", "value":"", "jwtToken":""}
  - withdrawal.rk <-> {"accountCode":"", "value":"", "jwtToken":""}
  - statements.rk <-> {"accountCode":"", "jwtToken":""}
  - transfer.rk   <-> {"originAccountCode":"", "destinationAccountCode":"", "value":"", "jwtToken":""}
