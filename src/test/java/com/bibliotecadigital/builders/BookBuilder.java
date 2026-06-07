package com.bibliotecadigital.builders;

import java.util.LinkedHashMap;
import java.util.Map;

public class BookBuilder {

    private Integer id;
    private String title;
    private String description;
    private Integer pageCount;
    private String excerpt;
    private String publishDate;

    public BookBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public BookBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public BookBuilder withPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public BookBuilder withExcerpt(String excerpt) {
        this.excerpt = excerpt;
        return this;
    }

    public BookBuilder withPublishDate(String publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> book = new LinkedHashMap<>();
        if (id != null) book.put("id", id);
        if (title != null) book.put("title", title);
        if (description != null) book.put("description", description);
        if (pageCount != null) book.put("pageCount", pageCount);
        if (excerpt != null) book.put("excerpt", excerpt);
        if (publishDate != null) book.put("publishDate", publishDate);
        return book;
    }
}
