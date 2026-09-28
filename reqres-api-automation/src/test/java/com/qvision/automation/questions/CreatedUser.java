/** Agrupa las Questions Screenplay que consultan información de usuarios. */
package com.qvision.automation.questions; // Declara el paquete de consultas usadas por las verificaciones.

import java.util.Map; // Modela el objeto JSON superior como un mapa de campos y valores.

import net.serenitybdd.screenplay.Actor; // Define el actor que responde la pregunta.
import net.serenitybdd.screenplay.Question; // Contrato Screenplay para obtener un valor verificable.
import net.serenitybdd.screenplay.rest.questions.LastResponse; // Expone la última respuesta HTTP recibida por el actor.

/**
 * Question que recupera el objeto JSON devuelto después de crear un usuario.
 * No introduce una clase de modelo adicional: representa de forma flexible el cuerpo con un Map.
 */
public class CreatedUser implements Question<Map<String, Object>> { // Declara el tipo de dato que la pregunta entrega a sus consumidores.
    /**
     * Crea la pregunta que leerá los detalles de la última respuesta.
     *
     * @return Question lista para ser consultada por un actor
     */
    public static CreatedUser details() { // Proporciona el punto de creación descriptivo de la Question.
        return new CreatedUser(); // Construye la pregunta sin estado adicional.
    }

    /**
     * Extrae el JSON superior de la última respuesta REST del actor.
     * Los StepDefinitions usan el mapa resultante para comprobar los campos name y job.
     *
     * @param actor actor que recibió la respuesta HTTP
     * @return propiedades del cuerpo JSON como mapa
     */
    @Override // Implementa la consulta requerida por Question.
    public Map<String, Object> answeredBy(Actor actor) { // Serenity invoca este método cuando el actor responde la pregunta.
        return actor.asksFor(LastResponse.received()).jsonPath().getMap(""); // Obtiene el cuerpo de la última respuesta y lo convierte a un mapa JSON.
    }
}