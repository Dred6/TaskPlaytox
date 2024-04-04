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
        logger.info("������������� ������ � ���������� " + count);
        return accountList;
    }

    public void makeTransaction(List<Account> accountList, int[] pair){
        makeTransactionFromTo(accountList.get(pair[0]), accountList.get(pair[1]), Util.getRandomAmount());
    }

    private void makeTransactionFromTo(Account fromAccount, Account toAccount, int countMoney){
        logger.info("������ ���������� �� �������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                + toAccount.getId() + " �� ����� " + countMoney + " �.�. � ������ " + Thread.currentThread().getName());
        try {
            if (countMoney < 0) throw new IncorrectAmountTransferException("������������ ����� ��������");
            makeTransfer(fromAccount,toAccount,countMoney);
            logger.info("��������� ���������� �� �������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                    + toAccount.getId() + " �� ����� " + countMoney + " �.�. � ������ " + Thread.currentThread().getName());
        } catch (IncorrectAmountTransferException e){
            logger.warn("���������� �� �������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                    + toAccount.getId() + " �� ����� " + countMoney + " �.�. ��������. ������������ ����� ��������");
        } catch (NegativeAmountMoneyException e) {
            logger.warn("���������� �� �������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                    + toAccount.getId() + " �� ����� " + countMoney + " �.�. ��������. ������������ ������� �� ����� " + fromAccount.getId());
        }

    }

    private void makeTransfer(Account fromAccount, Account toAccount, int countMoney) throws NegativeAmountMoneyException {
        logger.info("������ ������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                + toAccount.getId() + " �� ����� " + countMoney + " �.�.");
        try {
            fromAccount.makeDebit(countMoney);
            toAccount.makeTransfer(countMoney);
        } catch (MakeDebitException e) {
            try {
                fromAccount.makeTransfer(countMoney);
            } catch (InterruptedException ex) {
                logger.error("���������� �� �������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                        + toAccount.getId() + " �� ����� " + countMoney + " �.�. ��������. ��������� ������ ��������. �������� ��������� �� ���� " + fromAccount.getId());
            }
        }  catch (InterruptedException e) {
            logger.error("���������� �� �������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                    + toAccount.getId() + " �� ����� " + countMoney + " �.�. ��������. ��������� ������ ��������. �������� ��������� �� ���� " + fromAccount.getId());
        }
        logger.info("������� ����� �� ����� " + fromAccount.getId() + " �� ���� "
                + toAccount.getId() + " �� ����� " + countMoney + " �.�. ��������. ���� ����������� - " + fromAccount.getMoney() + ". ���� ���������� - " + toAccount.getMoney());
    }
}
