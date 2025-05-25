module com.example.medisyncbackend {
    /* ----- dependencies ----- */
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires jakarta.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires javafx.base;

    /* ----- packages Spring must reflect on ----- */
    opens com.example.medisyncbackend;               // main class
    opens com.example.medisyncbackend.Models;        // JPA entities
    opens com.example.medisyncbackend.Controllers;   // REST/MVC controllers
    opens com.example.medisyncbackend.Services;      // @Service/@Component beans  ← NEW
    opens com.example.medisyncbackend.Repository;  // (optional but safe)

    /* ----- any packages you want other modules to import ----- */
    exports com.example.medisyncbackend.Models;
}
