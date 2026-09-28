/** Agrupa las Questions Screenplay que consultan información de usuarios. */
package com.qvision.automation.questions; // Mantiene las preguntas separadas de las acciones y de las StepDefinitions.

import net.serenitybdd.screenplay.Actor; // Representa al actor cuya respuesta HTTP se va a consultar.
import net.serenitybdd.screenplay.Question; // Define una consulta que devuelve un valor para una aserción.
import net.serenitybdd.screenplay.rest.questions.LastResponse; // Accede a la última respuesta obtenida con Serenity REST.

/**
 * Question que obtiene el código de estado HTTP de la última respuesta.
 * Para el escenario exitoso de creación, el feature compara el valor devuelto con 201 Created.
 */
public class ResponseStatus implements Question<Integer> { // Tipifica el resultado de esta consulta como un entero HTTP.
    /**
     * Crea la pregunta del código de estado.
     *
     * @return Question que puede consultar el actor
     */
    public static ResponseStatus code() { // Expone una fábrica legible para las StepDefinitions.
        return new ResponseStatus(); // Construye una pregunta sin estado porque consulta la respuesta del actor al responderse.
    }

    /**
     * Consulta el status code de la última respuesta recibida por el actor.
     * La aserción del paso Then decide si el código coincide con el esperado, como 201 para una creación exitosa.
     *
     * @param actor actor que realizó la solicitud REST
     * @return código de estado HTTP de la última respuesta
     */
    @Override // Implementa el método de respuesta requerido por Question.
    public Integer answeredBy(Actor actor) { // Se ejecuta cuando el flujo pide al actor el código de respuesta.
        return actor.asksFor(LastResponse.received()).statusCode(); // Recupera la última respuesta de Serenity REST y devuelve su status code.
    }
}
