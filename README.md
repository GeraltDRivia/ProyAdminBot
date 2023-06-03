## Como configurar el proyecto para correrlo en local
> 1. Software necesario para la instalación

- Java JDK 17
- Base de datos posgres
- Gestor de base de datos

> 2. En el proyecto se deben configurar las siguiente variables de entorno

- DATABASE_URL (Url de la base de datos)
- DATABASE_USERNAME (Usuario de base de datos)
- DATABASE_PASSWORD (Constraseña de base de datos)

Estas estan localizadas en los archivos `hibernate.cfg.xml` y `application.properties` 

> 3. Debemos crear un bot en telegram

- Creamos el bot siguiendo los pasos dados por [BotFather](https://t.me/BotFather).
- Seguido añadir el Username y el Token al archivo `application.properties`

> 4. El Nombre de nuestro bot es [RapiPedidosGUA](https://t.me/ProyOneBot).

![](resources/images/1.jpg)


