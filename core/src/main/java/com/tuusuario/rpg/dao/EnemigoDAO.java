package com.tuusuario.rpg.dao;

import com.tuusuario.rpg.model.Enemigo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnemigoDAO {

    // Configuración para XAMPP (usuario root y contraseña vacía por defecto)
    private static final String URL = "jdbc:mysql://localhost:3306/rpg_medac?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Carga los enemigos desde la tabla de phpMyAdmin
    public List<Enemigo> obtenerTodosLosEnemigos() {
        List<Enemigo> lista = new ArrayList<>();
        String sql = "SELECT nombre, PS, ATQ, DEF,PM FROM enemigos";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int PS = rs.getInt("ps");
                int ATQ = rs.getInt("atq");
                int DEF = rs.getInt("def");
                int PM = rs.getInt("pm");

                Enemigo enemigo = new Enemigo(nombre, PS, ATQ, DEF,PM);

                lista.add(enemigo);
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BD MySQL: " + e.getMessage());
        }
        return lista;
    }


    }
