# Patrones implementados

##Message Router

### Arquitecturales
- [ ] Message Broker

### Routers Simples
- [x] [Content-Based Router](https://www.enterpriseintegrationpatterns.com/patterns/messaging/ContentBasedRouter.html) (1 -> 1)
  - Envia un mensaje a un endpoint especifico, dependiendo del contenido del mensaje
- [x] [Message Filter](https://www.enterpriseintegrationpatterns.com/patterns/messaging/Filter.html) (1 -> 0 or 1)
  - Elimina mensajes indeseados de la ruta
- [ ] [Recipient List](https://www.enterpriseintegrationpatterns.com/patterns/messaging/RecipientList.html)
  - Se administra una lista de 1 o mas destinos, y se envia a cada uno dependiendo de reglas
- [x] [Splitter](https://www.enterpriseintegrationpatterns.com/patterns/messaging/Sequencer.html) (1 -> n)
  - Divide un mensaje en varios
- [x] [Aggregator](https://www.enterpriseintegrationpatterns.com/patterns/messaging/Aggregator.html) (n -> 1)
  - Agrega varios mensajes en uno
- [x] [Resequencer](https://www.enterpriseintegrationpatterns.com/patterns/messaging/Resequencer.html) (n -> n)
  - Obtiene y reordena mensajes, para enviarlos a un endpoint en un orden específico
- [ ] [Dynamic Router](https://www.enterpriseintegrationpatterns.com/patterns/messaging/DynamicRouter.html)
  - Las reglas de enrutamiento se configuran de acuerdo a una configuracion inicial, y se pueden cambiar en tiempo de ejecución  

### Routers Compuestos
- [ ] [Composed Message Processor](https://www.enterpriseintegrationpatterns.com/patterns/messaging/DistributionAggregate.html)
  - Agrupacion de Splitter, Aggregator, etc. para procesar un mensaje complejo
- [ ] [Scatter-Gather](https://www.enterpriseintegrationpatterns.com/patterns/messaging/BroadcastAggregate.html)
  - Distribuye un mensaje a varios receptores, y reagrega la respuesta en un solo mensaje
- [ ] [Process Manager](https://www.enterpriseintegrationpatterns.com/patterns/messaging/ProcessManager.html)
  - Mantiene el estado del mensaje, y enruta mediante reglas que no necesariamente se conocen en tiempo de diseño
- [ ] [Routing Slip](https://www.enterpriseintegrationpatterns.com/patterns/messaging/RoutingTable.html)
  - Cada mensaje contiene una lista de los filtros a los cuales tiene que acceder (Similar al proceso de reglas de recepción de correo de Outlook)

##Messaging
- [ ] Message Channel
- [ ] Message
- [ ] Pipes and Filters
- [ ] Message Router
- [ ] Message Translator
- [ ] Message Endpoint

##Message Channel

- [ ] Point-to-Point Channel
- [ ] Publish-Subscribe Channel
- [ ] Datatype Channel
- [ ] Invalid Message Channel
- [ ] Dead Letter Channel
- [ ] Guaranteed Delivery
- [ ] Channel Adapter
- [ ] Messaging Bridge
- [ ] Message Bus

##Message Construction
- [ ] Command Message
- [ ] Document Message
- [ ] Event Message
- [ ] Request-Reply
- [ ] Return Address
- [ ] Correlation Identifier
- [ ] Message Sequence
- [ ] Message Expiration
- [ ] Format Indicator

##Message Transformation
- [ ] Envelope Wrapper
- [ ] Content Enricher
- [ ] Content Filter
- [ ] Claim Check
- [ ] Normalizer
- [ ] Canonical Data Model

##Message Endpoint
- [ ] Messaging Gateway
- [ ] Messaging Mapper
- [ ] Transactional Client
- [ ] Polling Consumer
- [ ] Event-Driven Consumer
- [ ] Competing Consumers
- [ ] Message Dispatcher
- [ ] Selective Consumer
- [ ] Durable Subscriber
- [ ] Idempotent Receiver
- [ ] Service Activator

##System Management
- [ ] Control Bus
- [ ] Detour
- [ ] Wire Tap
- [ ] Message History
- [ ] Message Store
- [ ] Smart Proxy
- [ ] Test Message
- [ ] Channel Purger