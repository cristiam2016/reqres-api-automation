/** Implementa el glue de Cucumber que conecta las frases Gherkin con las interacciones Screenplay. */
package com.qvision.automation.stepdefinitions; // Mantiene las definiciones en el paquete configurado por el runner.

import static org.assertj.core.api.Assertions.assertThat; // Proporciona aserciones legibles para comprobar los resultados del escenario.

import com.qvision.automation.questions.CreatedUser; // Consulta los campos del usuario devueltos en el último JSON.
import com.qvision.automation.questions.ResponseStatus; // Consulta el código HTTP de la última respuesta REST.
import com.qvision.automation.tasks.CreateUser; // Ejecuta la interacción POST de creación de usuario.
import com.qvision.automation.tasks.FetchUser; // Conserva la interacción GET para escenarios de consulta existentes.
import io.cucumber.java.Before; // Ejecuta la inicialización del actor antes de cada escenario.
import io.cucumber.java.en.Given; // Vincula pasos Gherkin de contexto o precondición.
import io.cucumber.java.en.Then; // Vincula pasos Gherkin de verificación.
import io.cucumber.java.en.When; // Vincula pasos Gherkin que ejecutan acciones.
import net.serenitybdd.screenplay.Actor; // Representa al actor que realiza tareas y formula preguntas.
import net.serenitybdd.screenplay.actors.OnStage; // Administra el escenario Screenplay y sus actores.
import net.serenitybdd.screenplay.actors.OnlineCast; // Crea el reparto de actores usado por Serenity en pruebas automatizadas.
import net.serenitybdd.screenplay.rest.abilities.CallAnApi; // Otorga al actor la habilidad de comunicarse con una API REST.

import java.util.Map; // Representa el objeto JSON de respuesta como pares de clave y valor.

/**
 * Traduce los pasos del feature a acciones y consultas del actor Screenplay.
 * Las tareas realizan las solicitudes; las preguntas consultan la respuesta y las aserciones verifican el contrato esperado.
 */
public class ReqresUserStepDefinitions { // Clase glue detectada porque su paquete está configurado en el runner.
    private static final String BASE_URL = System.getProperty("reqres.baseUrl", "https://reqres.in/api"); // Permite cambiar la URL predeterminada de los pasos en inglés mediante una propiedad Maven.
    private Actor user; // Conserva al actor que ejecuta las tareas y consulta las respuestas durante cada escenario.

    /**
     * Prepara un escenario Screenplay aislado antes de cada escenario Cucumber.
     * El reparto en línea permite a Serenity registrar las acciones y evidencias del actor.
     */
    @Before // Indica a Cucumber que ejecute este hook antes de cada escenario.
    public void setTheStage() { // Inicializa el escenario de Screenplay y crea al actor de pruebas.
        OnStage.setTheStage(new OnlineCast()); // Instala el reparto de actores administrado por Serenity.
        user = OnStage.theActorCalled("API consumer"); // Crea al actor con un nombre visible en los reportes de Serenity.
    }

    /**
     * Otorga la habilidad REST usando la URL configurable por propiedad.
     * Esta definición conserva el flujo previo en inglés para consultar usuarios.
     */
    @Given("the Reqres API is available") // Vincula la precondición inglesa del feature de consulta.
    public void theReqresApiIsAvailable() { // Configura el host de API predeterminado o el provisto por Maven.
        user.can(CallAnApi.at(BASE_URL)); // Asigna al actor la habilidad CallAnApi necesaria para enviar solicitudes REST.
    }

    /**
     * Otorga al actor la habilidad de llamar a la API en la URL indicada por el escenario.
     * Para el escenario de creación, la URL base es https://reqres.in y el endpoint incluye /api/users.
     */
    @Given("que el automatizador establece la URL base {string}") // Vincula el paso Dado del escenario en español.
    public void theAutomatorSetsTheBaseUrl(String baseUrl) { // Recibe la URL base como parámetro del texto Gherkin.
        user.can(CallAnApi.at(baseUrl)); // Configura CallAnApi en el actor para que sus Tasks puedan usar Serenity REST.
    }

    /**
     * Ejecuta la tarea de consulta de un usuario por su identificador.
     * Este paso existente se mantiene disponible para escenarios GET.
     */
    @When("the user requests user {string}") // Vincula el paso When inglés de consulta.
    public void theUserRequestsUser(String userId) { // Recibe el identificador indicado en el escenario.
        user.attemptsTo(FetchUser.withId(userId)); // Pide al actor que ejecute la Task GET para ese identificador.
    }

    /**
     * Ejecuta el POST para crear un usuario con los datos incluidos en el escenario.
     * La StepDefinition delega la interacción HTTP a una Task, conservando la separación de responsabilidades Screenplay.
     */
    @When("envía una solicitud POST al endpoint {string} con el nombre {string} y el cargo {string}") // Vincula el paso Cuando del feature en español.
    public void sendsPostRequestToCreateUser(String endpoint, String name, String job) { // Recibe endpoint, nombre y cargo desde los parámetros Gherkin.
        user.attemptsTo(CreateUser.withDetails(endpoint, name, job)); // El actor ejecuta la Task responsable de construir y enviar el POST.
    }

    /**
     * Comprueba el código HTTP esperado para los escenarios ingleses existentes.
     * La Question delega la lectura de la respuesta al modelo Screenplay.
     */
    @Then("the response status should be {int}") // Vincula la verificación inglesa del estado HTTP.
    public void theResponseStatusShouldBe(int expectedStatus) { // Recibe el estado esperado como entero.
        assertThat(user.asksFor(ResponseStatus.code())).isEqualTo(expectedStatus); // Verifica que la respuesta REST tenga el código solicitado.
    }

    /**
     * Valida el estado del POST de creación definido por el feature.
     * En el escenario exitoso el valor esperado es 201 Created.
     */
    @Then("el código de respuesta debe ser {int}") // Vincula el paso Entonces que especifica el estado esperado.
    public void theResponseCodeShouldBe(int expectedStatus) { // Recibe, por ejemplo, el código HTTP 201 del feature.
        assertThat(user.asksFor(ResponseStatus.code())).isEqualTo(expectedStatus); // Solicita el StatusCode mediante la Question y lo compara con el valor esperado.
    }

    /**
     * Comprueba que la representación JSON devuelta conserve el nombre y el cargo enviados.
     * La Question recupera el documento completo y AssertJ valida ambos pares clave-valor.
     */
    @Then("la respuesta debe contener el nombre {string} y el cargo {string}") // Vincula el paso Entonces que valida los campos del JSON.
    public void theResponseShouldContainNameAndJob(String name, String job) { // Recibe los valores que el escenario espera encontrar.
        Map<String, Object> response = user.asksFor(CreatedUser.details()); // Pregunta al actor por el objeto JSON de la última respuesta.
        assertThat(response).containsEntry("name", name).containsEntry("job", job); // Verifica que JSON incluya exactamente las claves name y job con los valores esperados.
    }
}
