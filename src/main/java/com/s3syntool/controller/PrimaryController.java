package com.s3syntool.controller;

import java.io.IOException;

import com.s3syntool.app.App;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
