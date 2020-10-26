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
- Ports 8080, 8081 and 8082 must be available
- Clone the repo @ https://github.com/victorhrgc/beertech.bancobeer.git
- 'docker-compose up' in the root directory to start the postgres and the rabbit container
- Start the API(8080), the Consumer(8081) and the ExternalBank(8082) applications on your IDE ('BancoBeerApiApplication.java', 'BancoBeerConsumerApplication.java', 'ExternalbankApplication.java')
- Access the API methods with either swagger (everything) or rabbitMQ (transactions)

# How to post a transaction message on RabbitMQ
- Go to 'Exchanges' and click on the exchange named 'transactions.consumer.exchange'
- Go to the 'Publish Message' section and add a ROUTING KEY and PAYLOAD for the desired transaction as follows:
  - deposit.rk    <-> {"accountCode":"", "value":"", "jwtToken":""}
  - withdrawal.rk <-> {"accountCode":"", "value":"", "jwtToken":""}
  - statements.rk <-> {"accountCode":"", "jwtToken":""}
  - transfer.rk   <-> {"originAccountCode":"", "destinationAccountCode":"", "value":"", "jwtToken":""}
  
  
  # Payments

  - How to create a paymentslip on the database  
  
    POST -> http://localhost:8082/payment-slips
    {"date":"20201024",  "value":"010200", "origin":"001/conta", "destination":"002/11111", "category":"OT"}

    Date:                 yyyyMMdd
    Value:                R$102,00 = 010200 
    Origin e Destination: Bank(001 ou 002)/Account w/ 5 digits  
    Category:             OT / EN / FO / ED / SE
    
   - When you pay a payment slip, the external bank will return error on even minutes and success on odd minutes (real life time)
  
  
  
