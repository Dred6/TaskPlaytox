package ru.davydov.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.davydov.entity.Account;
import ru.davydov.exception.IncorrectAmountTransferException;
import ru.davydov.exception.MakeDebitException;
import ru.davydov.exception.NegativeAmountMoneyException;
import ru.davydov.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountService {
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public List<Account> generateListAccount(int count){
        Properties appProperties = Util.getAppProperties();
        List<Account> accountList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            accountList.add(new Account(appProperties.getProperty("max_money")));
        }
        logger.info("Сгенерировано счетов в количестве " + count);
        return accountList;
    }

    public void makeTransaction(List<Account> accountList, int[] pair){
        makeTransactionFromTo(accountList.get(pair[0]), accountList.get(pair[1]), Util.getRandomAmount());
    }

    private void makeTransactionFromTo(Account fromAccount, Account toAccount, int countMoney){
        logger.info("Начали транзакцию по переводу денег со счета " + fromAccount.getId() + " на счет "
                + toAccount.getId() + " на сумму " + countMoney + " у.е. В потоке " + Thread.currentThread().getName());
        try {
            if (countMoney < 0) throw new IncorrectAmountTransferException("Некорректная сумма перевода");
            makeTransfer(fromAccount,toAccount,countMoney);
            logger.info("Завершили транзакцию по переводу денег со счета " + fromAccount.getId() + " на счет "
                    + toAccount.getId() + " на сумму " + countMoney + " у.е. В потоке " + Thread.currentThread().getName());
        } catch (IncorrectAmountTransferException e){
            logger.warn("Транзакцию по переводу денег со счета " + fromAccount.getId() + " на счет "
                    + toAccount.getId() + " на сумму " + countMoney + " у.е. отменена. Некорректная сумма перевода");
        } catch (NegativeAmountMoneyException e) {
            logger.warn("Транзакция по переводу денег со счета " + fromAccount.getId() + " на счет "
                    + toAccount.getId() + " на сумму " + countMoney + " у.е. отменена. Недостаточно средств на счете " + fromAccount.getId());
        }

    }

    private void makeTransfer(Account fromAccount, Account toAccount, int countMoney) throws NegativeAmountMoneyException {
        logger.info("Начали перевод денег со счета " + fromAccount.getId() + " на счет "
                + toAccount.getId() + " на сумму " + countMoney + " у.е.");
        try {
            fromAccount.makeDebit(countMoney);
            toAccount.makeTransfer(countMoney);
        } catch (MakeDebitException e) {
            try {
                fromAccount.makeTransfer(countMoney);
            } catch (InterruptedException ex) {
                logger.error("Транзакция по переводу денег со счета " + fromAccount.getId() + " на счет "
                        + toAccount.getId() + " на сумму " + countMoney + " у.е. отменена. Фатальная ошибка перевода. Средства вернулись на счет " + fromAccount.getId());
            }
        }  catch (InterruptedException e) {
            logger.error("Транзакция по переводу денег со счета " + fromAccount.getId() + " на счет "
                    + toAccount.getId() + " на сумму " + countMoney + " у.е. отменена. Фатальная ошибка перевода. Средства вернулись на счет " + fromAccount.getId());
        }
        logger.info("Перевод денег со счета " + fromAccount.getId() + " на счет "
                + toAccount.getId() + " на сумму " + countMoney + " у.е. завершен. Счет отправителя - " + fromAccount.getMoney() + ". Счет получателя - " + toAccount.getMoney());
    }
}
