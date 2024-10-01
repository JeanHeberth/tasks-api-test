package br.com.tasks.apitest;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;

public class APITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8015/tasks-backend/";
    }


    @Test
    public void deveIncluirTarefaComDataFutura() {

        LocalDate dataFutura = LocalDate.now().plusDays(1);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(Map.of("task", "Tarefa via API", "dueDate", dataFutura.toString()))
                .when()
                .post("todo")
                .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveAdicionarTarefaComDataAtual() {
        LocalDate dataAtual = LocalDate.now();

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(Map.of("task", "Tarefa via API", "dueDate", dataAtual.toString()))
                .post("todo")
                .then()
                .statusCode(201)
        ;
    }

    @Test
    public void naoDeveAdicionarTarefaComDataPassada() {
        LocalDate dataPassada = LocalDate.now().plusDays(-1);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(Map.of("task", "Tarefa via API", "dueDate", dataPassada.toString()))
                .when()
                .post("todo")
                .then()
                .statusCode(400)
                .body("message", containsString("Due date must not be in past"))
        ;
    }
    @Test
    public void naoDeveAdicionarTarefaComTaskVazia() {
        LocalDate dataPassada = LocalDate.now().plusDays(-1);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(Map.of("task", "", "dueDate", dataPassada.toString()))
                .when()
                .post("todo")
                .then()
                .statusCode(400)
                .body("message", containsString("Fill the task description"))
        ;
    }
    @Test
    public void naoDeveAdicionarTarefaComDataNull(){
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(Map.of("task", "Tarefa via API", "dueDate", ""))
                .when()
                .post("todo")
                .then()
                .statusCode(400)
                .body("message", containsString("Fill the due date"))
        ;
    }

    @Test
    public void deveRetornarTarefas() {
        RestAssured.given()
                .when()
                .get("todo")
                .then()
                .statusCode(200)
        ;
    }
}