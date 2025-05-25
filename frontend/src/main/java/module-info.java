module com.example.medisyncfrontend {

    /* ── JavaFX ─────────────────────────────────────────────── */
    requires javafx.controls;
    requires javafx.fxml;

    /* ── Networking / JSON ──────────────────────────────────── */
    requires java.net.http;                 // ApiClient uses HttpClient
    // ObjectMapper in controllers
    requires com.google.gson;                // if you still need it anywhere

    /* ── Your back-end’s model module ───────────────────────── */
    requires com.example.medisyncbackend;
    requires org.apache.tomcat.embed.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;    // import Patient, Doctor, …

    /* ── Packages opened for reflective access ──────────────── */
    // FXML loads controllers via reflection
    opens com.example.medisyncfrontend.Controllers to javafx.fxml;
    // The FXML files that use plain classes in the root package
    opens com.example.medisyncfrontend to javafx.fxml, javafx.graphics;

    // Jackson needs to read/write fields of ApiClient helper classes, etc.
    opens com.example.medisyncfrontend.Utils     to com.fasterxml.jackson.databind;



    /* ── Public API exports (if other modules need them) ────── */
    exports com.example.medisyncfrontend.Controllers;
    exports com.example.medisyncfrontend.Utils;
}
