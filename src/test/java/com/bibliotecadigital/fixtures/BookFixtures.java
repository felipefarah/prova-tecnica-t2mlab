package com.bibliotecadigital.fixtures;

import com.bibliotecadigital.builders.BookBuilder;

import java.util.Map;

public final class BookFixtures {

    private BookFixtures() {
    }

    public static Map<String, Object> validBook() {
        return new BookBuilder()
                .withId(1001)
                .withTitle("Livro QA")
                .withDescription("Livro criado para testes")
                .withPageCount(200)
                .withExcerpt("Trecho de teste")
                .withPublishDate("2026-01-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> validBookForUpdate(int id) {
        return new BookBuilder()
                .withId(id)
                .withTitle("Livro Atualizado")
                .withDescription("Descrição Atualizada")
                .withPageCount(300)
                .withExcerpt("Atualizado")
                .withPublishDate("2026-01-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> bookWithoutTitle() {
        return new BookBuilder()
                .withId(1002)
                .withDescription("Sem título")
                .withPageCount(100)
                .withExcerpt("Teste")
                .withPublishDate("2026-01-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> bookWithEmptyStrings() {
        return new BookBuilder()
                .withId(1003)
                .withTitle("")
                .withDescription("")
                .withPageCount(0)
                .withExcerpt("")
                .withPublishDate("1900-01-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> bookWithMaxValues() {
        String longString = "A".repeat(10000);
        return new BookBuilder()
                .withId(Integer.MAX_VALUE)
                .withTitle(longString)
                .withDescription(longString)
                .withPageCount(Integer.MAX_VALUE)
                .withExcerpt(longString)
                .withPublishDate("9999-12-31T23:59:59.999Z")
                .build();
    }

    public static Map<String, Object> bookWithInvalidDate() {
        return new BookBuilder()
                .withId(1004)
                .withTitle("Data Inválida")
                .withDescription("Teste de data")
                .withPageCount(100)
                .withExcerpt("Teste")
                .withPublishDate("data-invalida")
                .build();
    }

    public static Map<String, Object> bookWithSqlInjection() {
        return new BookBuilder()
                .withId(3000)
                .withTitle("' OR '1'='1")
                .withDescription("SQL Injection")
                .withPageCount(100)
                .withExcerpt("Teste")
                .withPublishDate("2026-01-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> bookWithXss() {
        return new BookBuilder()
                .withId(3001)
                .withTitle("<script>alert('xss')</script>")
                .withDescription("XSS Test")
                .withPageCount(100)
                .withExcerpt("Teste")
                .withPublishDate("2026-01-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> bookForE2E() {
        return new BookBuilder()
                .withId(5000)
                .withTitle("Livro E2E")
                .withDescription("Teste end to end")
                .withPageCount(150)
                .withExcerpt("E2E")
                .withPublishDate("2026-06-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> bookForE2EUpdate(int id) {
        return new BookBuilder()
                .withId(id)
                .withTitle("Livro E2E Atualizado")
                .withDescription("Teste end to end atualizado")
                .withPageCount(250)
                .withExcerpt("E2E Atualizado")
                .withPublishDate("2026-06-01T00:00:00.000Z")
                .build();
    }

    public static Map<String, Object> bookForIntegration() {
        return new BookBuilder()
                .withId(2000)
                .withTitle("Livro Integração")
                .withDescription("Teste integração")
                .withPageCount(100)
                .withExcerpt("Teste")
                .withPublishDate("2026-01-01T00:00:00.000Z")
                .build();
    }
}
