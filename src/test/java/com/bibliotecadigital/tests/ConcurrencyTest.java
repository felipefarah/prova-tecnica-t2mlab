package com.bibliotecadigital.tests;

import com.bibliotecadigital.clients.BooksClient;
import com.bibliotecadigital.utils.Environment;
import com.bibliotecadigital.utils.TestLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Books API")
@Feature("Concorrência")
public class ConcurrencyTest {

    @Test
    @DisplayName("Concorrência - Executar 50 requisições simultâneas ao GET /Books")
    @Description("Validar que a API suporta no mínimo 50 requisições simultâneas sem erro 500, sem exceções e com tempo aceitável")
    void shouldHandleConcurrentRequests() throws InterruptedException, ExecutionException {
        int totalRequests = Environment.getConcurrentRequests();
        TestLogger.logTestStart("Concorrência - Executar " + totalRequests + " requisições simultâneas ao GET /Books");
        TestLogger.logRequest("GET", "/Books (x" + totalRequests + " simultâneas)", null);

        ExecutorService executor = Executors.newFixedThreadPool(totalRequests);
        List<Future<Response>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            futures.add(executor.submit(() -> {
                BooksClient client = new BooksClient();
                return client.getAllBooks();
            }));
        }

        List<Response> responses = new ArrayList<>();
        List<Exception> exceptions = new ArrayList<>();

        for (Future<Response> future : futures) {
            try {
                responses.add(future.get(30, TimeUnit.SECONDS));
            } catch (Exception e) {
                exceptions.add(e);
            }
        }

        long totalTime = System.currentTimeMillis() - startTime;
        executor.shutdown();

        System.out.println("  Respostas: " + responses.size() + "/" + totalRequests);
        System.out.println("  Exceções:  " + exceptions.size());
        System.out.println("  Tempo:     " + totalTime + "ms");

        long serverErrors = responses.stream().filter(r -> r.getStatusCode() >= 500).count();
        System.out.println("  Erros 500: " + serverErrors);

        try {
            assertTrue(exceptions.isEmpty(),
                    "Não devem ocorrer exceções. Encontradas: " + exceptions.size());
            assertEquals(0, serverErrors,
                    "Não deve haver erros 500. Encontrados: " + serverErrors);
            assertTrue(totalTime < 30000,
                    "Tempo total deve ser menor que 30s. Obtido: " + totalTime + "ms");
            assertEquals(totalRequests, responses.size(),
                    "Todas as " + totalRequests + " requisições devem retornar resposta");
            TestLogger.logPass();
        } catch (AssertionError e) {
            TestLogger.logFail(totalRequests + " respostas sem erros em < 30s",
                    "respostas=" + responses.size() + " erros500=" + serverErrors + " tempo=" + totalTime + "ms");
            throw e;
        }
    }
}
