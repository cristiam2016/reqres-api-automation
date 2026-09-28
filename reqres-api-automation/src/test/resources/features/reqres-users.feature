#language: es
Característica: Gestión de usuarios en la API REST de ReqRes

  @API
  Escenario: Crear un nuevo usuario exitosamente
    Dado que el automatizador establece la URL base "https://reqres.in"
    Cuando envía una solicitud POST al endpoint "/api/users" con el nombre "Cristiam" y el cargo "QA Lead"
    Entonces el código de respuesta debe ser 201
    Y la respuesta debe contener el nombre "Cristiam" y el cargo "QA Lead"