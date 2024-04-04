package ru.davydov.entity;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ru.davydov.exception.MakeDebitException;
import ru.davydov.exception.NegativeAmountMoneyException;
import ru.davydov.util.Util;

public class Account {
    private final Logger logger = LoggerFactory.getLogger(Account.class);
    private String id;
    private int money;

    public Account(String money) {
        this.id = Util.getRandomAccountId();
        this.money = Integer.parseInt(money);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account id= " + id + " " + ", money= " + money;
    }

    public synchronized void makeTransfer(int amount) throws InterruptedException {
        Thread.sleep(Util.getRandomSleepTime());
        this.money += amount;
    }

    public synchronized void makeDebit(int amount) throws NegativeAmountMoneyException, MakeDebitException {
        int newMoneyValue = this.money - amount;
        if (newMoneyValue < 0){
            logger.info("Недостаточно средств на счете id - " + this.id);
            throw new NegativeAmountMoneyException("Недостаточно средств на счете id - " + this.id);
        } else {
            try {
                this.money -= amount;
            } catch (Exception e){
                throw new MakeDebitException("Ошибка списания средств");
            }
        }
    }
}
