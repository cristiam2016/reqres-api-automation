/** Paquete que contiene los puntos de entrada para ejecutar las pruebas automatizadas. */
package com.qvision.automation.runners; // Organiza los runners separados del código de soporte y de negocio.

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME; // Permite configurar el paquete donde Cucumber encuentra las definiciones de pasos.
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME; // Permite indicar el plugin que publica los resultados de Cucumber.

import org.junit.platform.suite.api.ConfigurationParameter; // Configura propiedades del motor Cucumber desde la suite JUnit.
import org.junit.platform.suite.api.IncludeEngines; // Restringe la ejecución de esta suite al motor Cucumber.
import org.junit.platform.suite.api.SelectClasspathResource; // Selecciona recursos de pruebas disponibles en el classpath.
import org.junit.platform.suite.api.Suite; // Marca la clase como suite ejecutable por JUnit Platform.

/**
 * Runner de JUnit Platform que descubre los escenarios Gherkin y los ejecuta con Cucumber.
 * Serenity se conecta como plugin para recopilar los pasos, resultados y evidencias de cada escenario.
 */
@Suite // Declara esta clase como una suite y punto de entrada de pruebas.
@IncludeEngines("cucumber") // Ejecuta los escenarios con el motor de Cucumber.
@SelectClasspathResource("features") // Descubre los archivos .feature bajo src/test/resources/features.
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.qvision.automation.stepdefinitions") // Registra las StepDefinitions como glue de Cucumber.
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "net.serenitybdd.cucumber.core.plugin.SerenityReporter") // Activa el plugin compatible con Serenity 5 para generar evidencias.
public class ReqresApiTest { // El runner no contiene lógica de negocio; configura el arranque de las pruebas.
	// JUnit Platform y Cucumber interpretan las anotaciones de la clase para ejecutar la suite.
}
