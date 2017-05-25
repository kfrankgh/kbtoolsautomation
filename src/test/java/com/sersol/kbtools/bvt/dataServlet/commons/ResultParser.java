package com.sersol.kbtools.bvt.dataServlet.commons;

/**
 * Every DataServlet query should implement this interface in order to parse the result table.
 * @param <T> The class representing the result data row retrieved by executing the SQL
 *           query via DataServlet
 */
public interface ResultParser<T> {
    void parse(String headerName, String value, T result);
}
