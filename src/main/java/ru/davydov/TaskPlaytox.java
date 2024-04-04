package ru.davydov;

import ru.davydov.service.TransferService;
import ru.davydov.util.Util;

import java.util.Properties;

public class TaskPlaytox {
    public static void main(String[] args){
        Properties appProperties = Util.getAppProperties();
        TransferService transferService = new TransferService(appProperties.getProperty("threads"), appProperties.getProperty("accounts"));
        transferService.start(appProperties.getProperty("transactions"));
    }
}
