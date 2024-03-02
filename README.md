# SOA-LAB

Lab works for discipline "Service-Oriented Architecture" I did in ITMO University.  

Labs are divided by branches:     

[Lab1](https://se.ifmo.ru/~s289142/soa-lab1):  
    Created the specification for our services on Swagger.  

[Lab2](https://github.com/bilguuk124/SOA-LAB/tree/back/Backend/soa-lab):   
    Simple microservices application built on JAX-RS (Jakarta EE) technoloy. Services communicate through JAX-RS http client.

[Lab3](https://github.com/bilguuk124/SOA-LAB/tree/back-spring):  
    Microservices from previous lab was rebuilt on Spring framework. Services communicate through RestTemplate.  
    For this lab there was added complexity to use other microservices technologies.  
    For first service:  
- Consul Service Discovery  
- Haproxy Load Balancer
    
  For second service:  
- Spring Cloud Gateway
- Spring Cloud Ribbon
- Spring Cloud Eureka
- Spring Cloud Config Server

[Lab4](https://github.com/bilguuk124/SOA-LAB/tree/back-lab4/Backend/soa-lab):  
    Removed all microservices technologies from previous lab in favor of Mule ESB (Enterprice Service Bus).  
    Removed previous implementation of second service, and rebuilt it on Spring Web Services, with entities generated from XML Schema.  
    Services still communicate through RestTemplate. TLS was configured. Created a Restful Proxy for second web service now working on SOAP.  
  
[Front](https://github.com/bilguuk124/SOA-LAB/tree/front):  
    To test the microservices architecture and service oriented architecture, we made a frontend for our services. It was written by @coder-logg using React.js  
