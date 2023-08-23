package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TransactionDataFetcher {

    private ObjectMapper objectMapper;
    private List<Transaction> transactions;

    public TransactionDataFetcher(){
        objectMapper = new ObjectMapper();
        try{
            byte[] jsonData = Files.readAllBytes(Paths.get("transactions.json"));
            transactions = objectMapper.readValue(jsonData, new TypeReference<List<Transaction>>() {});

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        double totalAmount = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        return totalAmount;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        double totalAmount = transactions.stream()
                .filter(transaction -> transaction.getSenderFullName().equals(senderFullName))
                .mapToDouble(Transaction::getAmount)
                .sum();
        return totalAmount;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        double highestAmount = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0);

        return highestAmount;
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        Set<String> uniqueClients = new HashSet<>();
        for (Transaction transaction : transactions) {
            uniqueClients.add(transaction.getSenderFullName());
            uniqueClients.add(transaction.getBeneficiaryFullName());
        }

        return uniqueClients.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
                .anyMatch(transaction ->
                        (transaction.getSenderFullName().equals(clientFullName) || transaction.getBeneficiaryFullName().equals(clientFullName))
                                && !transaction.getIssueSolved());
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    //I had to change the return type to List<Transaction>, as par my understanding you want all transactions thats why i used List
    //I also had to put the argument in method because the statement states to get ALL transaction by BENEFICIARY NAME

    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName(String beneficiaryName) {
        Map<String,List<Transaction>> transactionsByBeneficiary = new HashMap<>();
        List<Transaction> transactionList = new ArrayList<>();

        for (Transaction transaction:transactions) {
            if(transaction.getBeneficiaryFullName()!=null && !transaction.getBeneficiaryFullName().isEmpty() && transaction.getBeneficiaryFullName().equals(beneficiaryName)){
                transactionList.add(transaction);
            }
        }
        transactionsByBeneficiary.put(beneficiaryName,transactionList);
        return transactionsByBeneficiary;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = new HashSet<>();
        for (Transaction transaction:transactions
             ) {
            Integer issueId = transaction.getIssueId();
            if(issueId!=null && transaction.getIssueSolved()==false){
                unsolvedIssueIds.add(issueId);
            }
        }
        return unsolvedIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = new ArrayList<>();
        for (Transaction transaction:transactions
        ) {
            if(transaction.getIssueMessage()!=null && transaction.getIssueSolved()==true){
                solvedIssueMessages.add(transaction.getIssueMessage());
            }
        }
        return solvedIssueMessages;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        transactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
        return transactions.subList(0,3);
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Map<String, Double> sentAmountBySender = new HashMap<>();
        for (Transaction transaction : transactions) {
            sentAmountBySender.merge(transaction.getSenderFullName(), transaction.getAmount(), Double::sum);
        }

        if(sentAmountBySender.isEmpty()){
            return Optional.empty();
        }

        String highestSender = null;
        double highestAmt = 0.0;

        for(Map.Entry<String,Double> entry : sentAmountBySender.entrySet()){
            if (entry.getValue()>highestAmt){
                highestAmt= entry.getValue();
                highestSender = entry.getKey();
            }
        }

        return Optional.ofNullable(highestSender);
    }

}
