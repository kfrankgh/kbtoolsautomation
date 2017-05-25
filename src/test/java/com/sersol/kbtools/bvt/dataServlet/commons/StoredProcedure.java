package com.sersol.kbtools.bvt.dataServlet.commons;

/**
 * Stored Procedure data class should implement this interface.
 * The implemented class should define the name of the stored procedure and the database where the
 * stored procedure exists.
 */
public interface StoredProcedure {
    String getDatabase();
    String getStoredProcedure();
}
