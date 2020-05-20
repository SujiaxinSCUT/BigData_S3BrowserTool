package com.s3syntool.controller;

import java.io.IOException;

import com.s3syntool.app.App;

import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}