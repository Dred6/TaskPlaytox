package ru.davydov.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public class Util {
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final String appPropertiesPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "application.properties";
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static String getRandomAccountId(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(alphabet.length());
            stringBuilder.append(alphabet.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static Properties getAppProperties(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(appPropertiesPath));
        } catch (IOException e) {
            logger.error("Не найдет файл со значениями переменных");
        }
        return properties;
    }

    public static int getAccountMoney(){
        Properties appProperties = getAppProperties();
        return Integer.parseInt(appProperties.getProperty("start_money"));
    }

    public static int[] getRandomPair(int count){
        int[] pair = new int[2];
        int first = (int) (Math.random() * count);
        int second = (int) (Math.random() * count);
        if (first == second){
            pair = getRandomPair(count);
        } else{
            pair[0] = first;
            pair[1] = second;
        }
        return pair;
    }

    public static int getRandomAmount() {
        Properties appProperties = getAppProperties();
        return (int) ((Math.random() * (Integer.parseInt(appProperties.getProperty("max_money")) -
                Integer.parseInt(appProperties.getProperty("min_money")))) + Integer.parseInt(appProperties.getProperty("min_money")));
    }

    public static int getRandomSleepTime() {
        Properties appProperties = getAppProperties();
        return (int) ((Math.random() * (Integer.parseInt(appProperties.getProperty("max_sleep")) -
                Integer.parseInt(appProperties.getProperty("min_sleep")))) + Integer.parseInt(appProperties.getProperty("min_sleep")));
    }
}
