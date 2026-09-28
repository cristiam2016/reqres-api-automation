# reqres-api-automation

![Estado de las pruebas](https://img.shields.io/badge/estado-pruebas%20automatizadas-brightgreen)

Proyecto de **automatización de APIs REST con Serenity BDD, Rest Assured y Screenplay**. Utiliza Cucumber para expresar escenarios en Gherkin y Serenity para coordinar actores, interacciones, consultas y evidencias de ejecución.

El escenario principal crea un usuario en ReqRes mediante `POST /api/users` y verifica que la respuesta tenga estado `201` y contenga los campos `name` y `job` con los valores solicitados.

## Prerrequisitos

- Java 17 o superior.
- Apache Maven 3.8 o superior.

**Nota sobre la versión actual:** el proyecto configura `maven.compiler.release` en Java 25. Para compilar y ejecutar esta configuración sin modificarla, se requiere un JDK 25; el mínimo general indicado arriba no sustituye el nivel de compilación declarado en `pom.xml`.

## Estructura del proyecto

```text
src/test/
├── java/com/qvision/automation/
│   ├── runners/             # Suite JUnit Platform y configuración del motor Cucumber
│   ├── stepdefinitions/     # Enlaces entre frases Gherkin y acciones Screenplay
│   ├── tasks/               # Acciones del actor, como enviar solicitudes POST y GET
│   └── questions/           # Consultas sobre la respuesta, como status code y JSON
└── resources/
	├── features/            # Escenarios de aceptación escritos en Gherkin
	└── serenity.properties  # Nombre del proyecto y ubicación del reporte
```

### Flujo Screenplay con Serenity REST

1. El Runner inicia JUnit Platform, descubre los `.feature` y registra las StepDefinitions junto al plugin de Serenity.
2. Las StepDefinitions preparan el escenario y crean un actor. La habilidad `CallAnApi` le permite comunicarse con la URL base de ReqRes.
3. El actor ejecuta la Task `CreateUser`, que usa Serenity REST para enviar un `POST` con `Content-Type: application/json` y los campos `name` y `job`.
4. Las Questions `ResponseStatus` y `CreatedUser` consultan la última respuesta HTTP. Las aserciones comprueban el código `201` y ambos campos del JSON.

La respuesta se representa como un `Map<String, Object>` en la Question `CreatedUser`; el proyecto no define una clase de modelo independiente para este cuerpo JSON.

## Ejecutar las pruebas

Desde la raíz del proyecto, este comando ejecuta el Runner Cucumber y genera el reporte Serenity con sus recursos estáticos:

```powershell
mvn clean test
```

## Reporte de evidencias

La agregación del reporte está enlazada a la fase Maven `test`, después de Surefire. Serenity genera el reporte HTML en:

```text
target/site/serenity/index.html
```
