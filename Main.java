import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        UserManagement users = new UserManagement();
        Scanner sc = new Scanner(System.in);
        int selected;

       while(true) {
           System.out.println("\nAUTOMATED TELLER MACHINE SYSTEM");
           System.out.println("[1] Login Account");
           System.out.println("[2] Register Account\n");
           System.out.println("[3] Exit Program");
           System.out.print("Select Option : ");
           try {

           selected = sc.nextInt();

           switch (selected) {
               case 1 -> {
                   System.out.println("\nLogin Account");
                   sc.nextLine();
                   System.out.print("Username : ");
                   String username = sc.nextLine();

                   System.out.print("Password : ");
                   String password = sc.nextLine();

                   if (users.LoginAccount(username, password))
                       BankMenu(sc, username);
                   else
                       System.out.println("Login failed!");
               }
               case 2 -> {
                   System.out.println("\nRegister Account");
                   sc.nextLine();
                   System.out.print("Username : ");
                   users.setUsername(sc.nextLine());

                   System.out.print("Password : ");
                   String password = sc.nextLine();

                   if (users.checkPassword(password)) {
                       System.out.print("Password : ");
                       password = sc.nextLine();

                       if (users.checkPassword(password))
                           System.out.println("Password must contain 8 characters!");
                       else
                           users.setPassword(password);
                   } else {
                       users.setPassword(password);
                   }
                   users.SignupAccount(users.getUsername(), users.getPassword());
               }
               case 3 -> {
                   System.out.println("Exiting program...");
                   System.exit(0);
                   sc.close();
               }
               default -> System.out.println("Invalid Input! , please try again");
           }
       }catch(InputMismatchException e){
            sc.nextLine();
            System.out.println("Please enter a numerical value");
        }
       }
    }

    public static void BankMenu(Scanner sc, String username){
        AtmManagement bank = new AtmManagement();

        int choice;
        while (true) {

            System.out.println("\nPlease choose an option:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transaction History\n");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            try{
                choice = sc.nextInt();

                switch (choice){
                  case 1 ->
                      System.out.println("\nYour Current Balance is : " + bank.viewBalance(username));
                  case 2 ->{
                      System.out.println("\nDeposit Money");
                      System.out.print("Enter the amount of money to be deposited: ");
                    try{
                        bank.setMoney(sc.nextDouble());

                        if(bank.CheckAccount(username)) {
                            bank.UpdateDeposit(username, bank.getMoney());
                        }else {
                            bank.DepositMoney(username, bank.getMoney());
                        }
                    }catch(InputMismatchException e){
                        sc.nextLine();
                        System.out.println("Please enter a numerical value");
                    }
                  }
                  case 3 -> {
                      System.out.println("\nWithdraw Money");
                      System.out.print("Enter the amount of money to be withdrawn : ");
                      try{
                           bank.setMoney(sc.nextDouble());
                           if(bank.CheckAccount(username)) {
                               bank.withdrawMoney(username, bank.getMoney());
                           }
                      }catch(InputMismatchException e){
                          sc.nextLine();
                          System.out.println("Please enter a numerical value");
                      }
                  }
                  case 4 ->{
                      System.out.println("View Transaction History");
                      System.out.printf("%-10s|%-10s|%-10s%n", "USERS", "ACTIVITY", "TIME OF LOG");
                         bank.ViewActivity(username);
                  }
                  case 5 ->{
                       return;
                  }
                default -> System.out.println("Invalid Input! , please try again");
            }
            }catch (InputMismatchException e){
                sc.nextLine();
                System.out.println("Please enter a numerical value");
            }
        }
    }
}
