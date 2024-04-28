

import jdk.internal.vm.annotation.*;
import java.util.Random;

// Value Classes 

@LooselyConsistentValue
@ImplicitlyConstructible
value class BalanceValue {
  private double dollars;
  private double cents;
  BalanceValue(double dollars, double cents) {
     this.dollars = dollars;
     this.cents = cents;
  }
  double getBalance() {
     return dollars + (cents / 100);
  }
  public static BalanceValue initBalance() {
     return new BalanceValue(1.0, 0.0);
  }
}

class CustomerValue {
  private long custid;
  private String name;
  @NullRestricted
  private BalanceValue balance;
  public CustomerValue(long custid, String name, BalanceValue balance) {
     this.custid = custid;
     this.name = name;
     this.balance = balance;
  }
  String getCustName() {
     return name;
  }
  double getCustAccBalance() {
     return balance.getBalance(); 
  }
  long getCustId() {
     return custid;
  }
  BalanceValue getBalance() {
     return balance;
  }
  void updateBalance(BalanceValue balance) {
     this.balance = balance;
  }
}

// Identity Classes

class Balance {
  private double dollars;
  private double cents;
  Balance(double dollars, double cents) {
     this.dollars = dollars;
     this.cents = cents;
  }
  double getBalance() {
     return dollars + (cents / 100);
  }
  public static Balance initBalance() {
     return new Balance(1.0, 0.0);
  }
}

class Customer {
  private long custid;
  private String name;
  private Balance balance;
  Customer(long custid, String name, Balance balance) {
     this.custid = custid;
     this.name = name;
     this.balance = balance;
  }
  String getCustName() {
     return name;
  }
  double getCustAccBalance() {
     return balance.getBalance(); 
  }
  Balance getBalance() {
     return balance;
  }
  void updateBalance(Balance balance) {
     this.balance = balance;
  }
  long getCustId() {
     return custid;
  }
}


public class update_balance {
   public Customer [] records;
   public CustomerValue [] value_records;

   public void genSynthCustData(int num_of_acc) {
        Random rd = new Random(1000);
        StringBuilder sb = new StringBuilder("Customer");
        records = new Customer[num_of_acc];       
        value_records = new CustomerValue[num_of_acc];       
        for (int i = 0; i < num_of_acc; i++) {
            long custid = rd.nextLong();
            records[i] = new Customer(custid, sb.append(i).toString(), Balance.initBalance());
            value_records[i] = new CustomerValue(custid, sb.toString(), BalanceValue.initBalance());
        } 
   }

   public void computeAndAddEOQInterest(double interest_pc) {
      for (int i = 0; i < records.length; i++) {
         double balance = records[i].getCustAccBalance(); 
         balance += (balance * interest_pc) / 100;
         records[i].updateBalance(new Balance(balance, 0.0));
      }
   }
   public void computeAndAddEOQInterestValues(double interest_pc) {
      for (int i = 0; i < value_records.length; i++) {
         double balance = value_records[i].getCustAccBalance(); 
         balance += (balance * interest_pc) / 100;
         value_records[i].updateBalance(new BalanceValue(balance, 0.0));
      }
   }
   
   public double addllCustomerBalance() {
      double total = 0.0;
      for (int i = 0; i < records.length; i++) {
          total += records[i].getCustAccBalance();
      }
      return total;
   } 
   
   public double addllCustomerBalanceValues() {
      double total = 0.0;
      for (int i = 0; i < value_records.length; i++) {
          total += value_records[i].getCustAccBalance();
      }
      return total;
   } 
    
   public static final int warmup_iters = 1000;
   public static final int bm_iters = 1000;

   public static void main (String [] args) {
      update_balance obj = new update_balance();
      int num_of_acc = Integer.parseInt(args[0]);
      obj.genSynthCustData(num_of_acc); 
      for (int i = 0; i < warmup_iters; i++) {
         obj.computeAndAddEOQInterest(8);
      } 
      long t1 = System.currentTimeMillis();
      for (int i = 0; i < bm_iters; i++) {
         obj.computeAndAddEOQInterest(8);
      }
      long t2 = System.currentTimeMillis();
      System.out.println("[baseline time] " + (t2 - t1) + " ms " + "[all cust balance] " + obj.addllCustomerBalance());

      for (int i = 0; i < warmup_iters; i++) {
         obj.computeAndAddEOQInterestValues(8);
      } 
      t1 = System.currentTimeMillis();
      for (int i = 0; i < bm_iters; i++) {
         obj.computeAndAddEOQInterestValues(8);
      }
      t2 = System.currentTimeMillis();
      System.out.println("[valhalla time] " + (t2 - t1) + " ms " + "[all cust balance] " + obj.addllCustomerBalanceValues());
   }
} 

