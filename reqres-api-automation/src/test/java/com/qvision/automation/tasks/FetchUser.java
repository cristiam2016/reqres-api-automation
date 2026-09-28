/** Agrupa las Tasks Screenplay que representan acciones sobre usuarios. */
package com.qvision.automation.tasks; // Declara el paquete común a las acciones de ReqRes.

import net.serenitybdd.screenplay.Actor; // Representa al actor que realiza la consulta REST.
import net.serenitybdd.screenplay.Task; // Define la acción que el actor puede intentar ejecutar.
import net.serenitybdd.annotations.Step; // Permite mostrar la consulta como paso en el reporte Serenity.
import net.serenitybdd.screenplay.rest.interactions.Get; // Construye solicitudes HTTP GET con Serenity REST.
import net.serenitybdd.screenplay.targets.Target; // Import heredado que no se utiliza en esta Task y no afecta a su ejecución.

/**
 * Task Screenplay que recupera un usuario por su identificador.
 * Se conserva como interacción reutilizable para escenarios GET además del flujo de creación POST.
 */
public class FetchUser implements Task { // Modela la acción de consulta sin mezclarla con las aserciones del escenario.
    private final String userId; // Guarda el identificador que se insertará en el parámetro de ruta.

    /**
     * Inicializa la Task con el identificador del usuario que se desea consultar.
     *
     * @param userId identificador de ReqRes que se enviará en la ruta
     */
    private FetchUser(String userId) { // Mantiene la construcción directa privada para favorecer el método fábrica.
        this.userId = userId; // Conserva el identificador para usarlo cuando el actor ejecute la Task.
    }

    /**
     * Crea una Task de consulta de usuario con una sintaxis descriptiva.
     *
     * @param userId identificador del usuario buscado
     * @return Task lista para que la ejecute un actor Screenplay
     */
    public static FetchUser withId(String userId) { // Expone una fábrica legible para los StepDefinitions.
        return new FetchUser(userId); // Devuelve la acción configurada con el identificador solicitado.
    }

    /**
     * Envía la consulta GET al recurso users/{id}.
     * El actor debe haber recibido antes la habilidad CallAnApi para resolver la URL base.
     *
     * @param actor actor Screenplay que ejecuta la solicitud
     * @param <T> tipo concreto de Actor usado por el framework
     */
    @Override // Implementa el método requerido por la interfaz Task.
    @Step("{0} requests user with id #userId") // Registra la acción y el identificador consultado en el reporte Serenity.
    public <T extends Actor> void performAs(T actor) { // Define cómo se realiza la consulta desde el punto de vista del actor.
        actor.attemptsTo( // Permite que Serenity registre la interacción REST ejecutada.
                Get.resource("/users/{id}").with(request -> request.pathParam("id", userId)) // Sustituye {id} por el userId y envía el GET.
        ); // Finaliza la secuencia de acciones del actor.
    }
}
