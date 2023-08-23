package com.smallworld;
import com.smallworld.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodingTestUT {

    private TransactionDataFetcher transactionDataFetcher;

    @BeforeEach
    public void setUp(){
        transactionDataFetcher = new TransactionDataFetcher();
    }

    @Test
    public void testGetTotalTransactionAmount(){
        double expectedTotalAmt = 4371.37;
        double actualTotalAmt = transactionDataFetcher.getTotalTransactionAmount();
        assertEquals(expectedTotalAmt,actualTotalAmt,0.01);
    }

    @Test
    public void testGetTotalTransactionAmountSentBy(){
        double expectedTotalAmt = 459.09;
        double actualTotalAmt = transactionDataFetcher.getTotalTransactionAmountSentBy("Billy Kimber");
        assertEquals(expectedTotalAmt,actualTotalAmt,0.01);
    }

    @Test
    public void testgetMaxTransactionAmount(){
        double expectedTotalAmt = 985;
        double actualTotalAmt = transactionDataFetcher.getMaxTransactionAmount();
        assertEquals(expectedTotalAmt,actualTotalAmt,0.01);
    }

    @Test
    public void testCountUniqueClients(){
        double expectedTotalAmt = 14;
        double actualTotalAmt = transactionDataFetcher.countUniqueClients();
        assertEquals(expectedTotalAmt,actualTotalAmt,0.01);
    }

    @Test
    public void testHasOpenComplianceIssues(){
        assertTrue(transactionDataFetcher.hasOpenComplianceIssues("Tom Shelby"));
    }

    @Test
    public void testGetTransactionsByBeneficiary() {
        Map<String, List<Transaction>> transactionsByBeneficiary = transactionDataFetcher.getTransactionsByBeneficiaryName("Alfie Solomons");


        String beneficiaryName = "Alfie Solomons";
        List<Transaction> beneficiaryTransactions = transactionsByBeneficiary.get(beneficiaryName);
        assertEquals(1, beneficiaryTransactions.size());


        String nonExistentBeneficiary = "NonExistentBeneficiary";
        List<Transaction> nonExistentTransactions = transactionsByBeneficiary.get(nonExistentBeneficiary);
        assertEquals(null, nonExistentTransactions);
    }

    @Test
    public void testGetUnsolvedIssueIds(){
        Set<Integer> ids = transactionDataFetcher.getUnsolvedIssueIds();
        assertEquals(5,ids.size());
    }

    @Test
    public void testGetAllSolvedIssueMessages(){
        List<String> msgs = transactionDataFetcher.getAllSolvedIssueMessages();
        assertTrue(msgs.contains("Never gonna run around and desert you"));
        assertEquals(3,msgs.size());
    }

    @Test
    public void testGetTop3TransactionsByAmount(){
        List<Transaction> transactionList = transactionDataFetcher.getTop3TransactionsByAmount();
        assertEquals(3,transactionList.size());
        assertEquals(985.0, transactionList.get(0).getAmount());
    }

    @Test
    public void testGetTopSender(){
        Optional<String> sender = transactionDataFetcher.getTopSender();
        assertEquals("Grace Burgess",sender.get());

    }
}
