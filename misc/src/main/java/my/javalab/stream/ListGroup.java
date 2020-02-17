package my.javalab.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ListGroup {

    public static void main(String[] args) {
        List<BankTransaction> bankTransactions = createBankTransactions();
        //Group the list by type, all WIRE txs in one list and all CHEQ txs in another list
        Collection<List<BankTransaction>> byTypeBankTransactions =
                bankTransactions.stream().collect(Collectors.groupingBy(BankTransaction::getType)).values();

        byTypeBankTransactions.forEach(bankTxs -> {
            bankTxs.forEach(bankTx -> System.out.println(bankTx.getType()));
            System.out.println("    ===    ");
        });

        System.out.println("...\n");

        //second level group by debitAccount
        byTypeBankTransactions.forEach(bankTxs -> {
            Collection<List<BankTransaction>> byCurrencyBankTransactions =
                    bankTxs.stream().collect(Collectors.groupingBy(BankTransaction::getCurrency)).values();

            byCurrencyBankTransactions.forEach(sameCurrencybankTxs -> {
                sameCurrencybankTxs.forEach(bankTx -> System.out.println(bankTx));
                System.out.println("    ===    ");
            });
            
            System.out.println("... one subList is done...");
        }) ;
    }

    private static List<BankTransaction> createBankTransactions() {
        List<BankTransaction> bankTransactions = new ArrayList<>();
        bankTransactions.add(new BankTransaction("WIRE", "CAD", 123, 12.34, "00001"));
        bankTransactions.add(new BankTransaction("WIRE", "USD", 345, 100, "00002"));
        bankTransactions.add(new BankTransaction("WIRE", "CAD", 123, 12.35, "00003"));
        bankTransactions.add(new BankTransaction("WIRE", "CAD", 567, 12.36,  "00004"));
        bankTransactions.add(new BankTransaction("CHEQ", "CAD", 123, 1234, "00005"));
        bankTransactions.add(new BankTransaction("WIRE", "USD", 891, 200, "00006"));
        bankTransactions.add(new BankTransaction("CHEQ", "CAD", 123, 5678, "00005"));
        return bankTransactions;
    }

}

class BankTransaction {
    private String type;
    private String currency;
    private int debitAccount;
    private double amount;
    private String guid;

    public BankTransaction(String type, String currency, int debitAccount, double amount, String guid) {
        this.type = type;
        this.currency = currency;
        this.debitAccount = debitAccount;
        this.amount = amount;
        this.guid = guid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(int debitAccount) {
        this.debitAccount = debitAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BankTransaction.class.getSimpleName() + "[", "]")
                .add("type='" + type + "'")
                .add("currency='" + currency + "'")
                .add("debitAccount=" + debitAccount)
                .add("amount=" + amount)
                .add("guid='" + guid + "'")
                .toString();
    }
}