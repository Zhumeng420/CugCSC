package com.example.cugcsc.database;

import static com.example.cugcsc.database.DbConnector.getConnection;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.Connection;

public class DbConnectorTest {

    @Test
    public void testGetConnect() throws Exception {
        Connection conn=getConnection();
        assertNotNull(conn);
    }
}