/** Agrupa las Tasks Screenplay que representan acciones sobre usuarios. */
package com.qvision.automation.tasks; // Declara el paquete de acciones que pueden ejecutar los actores.

import java.util.Map; // Permite serializar el cuerpo de la solicitud como un objeto JSON de propiedades.

import net.serenitybdd.annotations.Step; // Publica la interacción en el reporte detallado de Serenity.
import net.serenitybdd.screenplay.Actor; // Define el actor genérico que ejecutará esta Task.
import net.serenitybdd.screenplay.Task; // Contrato Screenplay para acciones realizadas por un actor.
import net.serenitybdd.screenplay.rest.interactions.Post; // Construye una solicitud HTTP POST mediante Serenity REST.

/**
 * Task Screenplay que crea un usuario mediante Serenity REST.
 * Recibe el endpoint y los campos del usuario; el actor debe tener previamente la habilidad CallAnApi.
 */
public class CreateUser implements Task { // Modela una acción de negocio reutilizable en escenarios de creación.
    private final String endpoint; // Guarda la ruta relativa a la URL base configurada en CallAnApi.
    private final String name; // Guarda el valor que se enviará en la propiedad JSON name.
    private final String job; // Guarda el valor que se enviará en la propiedad JSON job.

    /**
     * Construye la Task con la ruta y los datos necesarios para la creación.
     * La fábrica pública withDetails es la interfaz recomendada para los escenarios.
     *
     * @param endpoint ruta de API a la que se enviará el POST
     * @param name nombre incluido en el cuerpo JSON
     * @param job cargo incluido en el cuerpo JSON
     */
    private CreateUser(String endpoint, String name, String job) { // Restringe la creación directa y mantiene la Task inmutable.
        this.endpoint = endpoint; // Conserva la ruta destino recibida desde el feature.
        this.name = name; // Conserva el nombre para serializarlo en la petición.
        this.job = job; // Conserva el cargo para serializarlo en la petición.
    }

    /**
     * Fábrica legible que prepara la Task para que la ejecute un actor.
     *
     * @param endpoint ruta relativa del recurso REST
     * @param name nombre que se desea crear
     * @param job cargo que se desea crear
     * @return Task inmutable configurada con los datos de la solicitud
     */
    public static CreateUser withDetails(String endpoint, String name, String job) { // Expone una sintaxis fluida coherente con Screenplay.
        return new CreateUser(endpoint, name, job); // Crea la Task con los tres valores proporcionados.
    }

    /**
     * Ejecuta la interacción HTTP como parte de las acciones del actor.
     * Serenity REST combina la URL base de CallAnApi con el endpoint y serializa el Map como JSON.
     *
     * @param actor actor Screenplay que realiza la petición
     * @param <T> tipo concreto de Actor usado por Screenplay
     */
    @Override // Indica que la implementación cumple el contrato de Task.
    @Step("{0} creates a user named #name with the role #job") // Registra una descripción de la acción y sus datos en el reporte Serenity.
    public <T extends Actor> void performAs(T actor) { // Define la interacción que se ejecuta cuando el actor intenta esta Task.
        actor.attemptsTo( // Delega la solicitud a una Interaction que Serenity REST administra y reporta.
                Post.to(endpoint).with(request -> request // Prepara un POST hacia la ruta recibida usando el host configurado en el actor.
                        .contentType("application/json") // Declara que el cuerpo de la solicitud está serializado como JSON.
                        .body(Map.of("name", name, "job", job))) // Envía ambos campos del contrato ReqRes en el cuerpo de la petición.
        ); // Completa la secuencia de interacciones del actor.
    }
}