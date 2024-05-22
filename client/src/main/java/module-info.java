module com.stream {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires jlayer;
    requires javafx.base;
    requires org.apache.commons.io;

    opens com.stream to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.stream.controller to javafx.fxml;

    exports com.stream;
    exports com.stream.controller;
    exports com.stream.model;
    exports com.stream.util;
    exports com.stream.listener;
}
