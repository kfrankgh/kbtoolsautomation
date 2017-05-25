package com.sersol.kbtools.bvt.tests.smoketests.common;

import com.sersol.kbtools.bvt.tests.ITestConstants;

/**
 * Class for setting and getting Holding values for serials, monographs or unknowns
 */
public class HoldingData {
    private String databaseCode;
    private ITestConstants.TitleType titleType;
    private String title;
    private String issn;
    private String publisher;
    private String dateStart;
    private String dateEnd;
    private String url;
    private String isbn10;
    private String isbn;
    private String edition;
    private String datePublished;
    private String author;
    private String editor;

    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public ITestConstants.TitleType getTitleType() {
        return titleType;
    }

    public void setTitleType(ITestConstants.TitleType titleType) {
        this.titleType = titleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsbn10() { return isbn10; }

    public void setIsbn10(String isbn10) { this.isbn10 = isbn10; }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
