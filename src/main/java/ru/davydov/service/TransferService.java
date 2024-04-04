package ru.davydov.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.davydov.entity.Account;
import ru.davydov.util.Util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransferService {
    private final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private final AccountService accountService;
    private final ExecutorService threadPool;
    private final List<Account> accountList;


    public TransferService(String threadPool, String accountList) {
        this.accountService = new AccountService();
        this.threadPool = Executors.newFixedThreadPool(Integer.parseInt(threadPool));
        this.accountList = accountService.generateListAccount(Integer.parseInt(accountList));
    }

    public void start(String countTransaction) {
        for (int i = 0; i < Integer.parseInt(countTransaction); i++) {
            int[] pair = Util.getRandomPair(this.accountList.size());
            threadPool.execute(() -> accountService.makeTransaction(accountList,pair));
        }

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60000, TimeUnit.SECONDS)) {
                logger.warn("Некоторые транзакции не успели выполнится.");
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        logger.info("Приложение завершило свою работу. Проведено " + countTransaction + " транзакций. Счета: " + accountList.stream().map(account -> account.toString() + "\n").toList());
    }
}
