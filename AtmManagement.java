import java.io.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AtmManagement {

    private double money;
    private static final String BANK = "MoneyBank.txt";
    private static final String LOGS = "AccountActivity.txt";
    public double getMoney() {return money;}
    public void setMoney(double money) {this.money = money;}

    private boolean CheckFile() throws FileNotFoundException {
        File file = new File(BANK);
        if (!file.exists())
            throw new FileNotFoundException("File Not Found" + BANK);
        return file.length() == 0;
    }

    public boolean CheckAccount(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BANK))) {
            String line;
            if (CheckFile()) {
                System.out.println("File does not have data yet!");
            } else {
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split("\\s+");
                    if (username.equals(parts[0]))
                        return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error Occurred!" + e.getMessage());
        }
        return false;
    }

    public String viewBalance(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BANK))) {
            String line;
            if (CheckFile()) {
                System.out.println("File does not have data yet!");
            } else {
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.replace("|", "").trim().split("\\s+");
                    if (username.equals(parts[0]))
                        return parts[1];
                }
            }
        } catch (IOException e) {
            System.out.println("An error Occurred!" + e.getMessage());
        }
        return "Can't found your Account";
    }

    public void DepositMoney(String username, double amount){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(BANK,true))) {
            String account = String.format("%-10s|$%-5.2f%n", username, amount);
                writer.append(account);
                System.out.println("Deposited Successfully!");
                 RecordActivity(username, "+" + amount);
        }catch (IOException e){
            System.out.println("An error Occurred!" + e.getMessage());
        }
    }

    public void UpdateDeposit(String username, double deposited){
        ArrayList<String> temp = readBankFile();

        double newBalance = processDeposited(username,deposited,temp);
        if (newBalance >= 0) {
            System.out.println("\nDeposited Successfully! New balance : " + newBalance);
            RecordActivity(username, "+" + deposited);
            writeBackToFile(temp);
        }
    }

    public void withdrawMoney(String username, double withdrawn) {
        ArrayList<String> temp = readBankFile();

        double newBalance = processWithdrawal(username, withdrawn, temp);

        if (newBalance >= 0) {
            System.out.println("\nWithdrawn Successfully! New balance : " + newBalance);
            writeBackToFile(temp);
            RecordActivity(username, "-" + withdrawn);
        }
    }

    private ArrayList<String> readBankFile() {
        ArrayList<String> temp = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BANK))) {
            String line;
            while ((line = reader.readLine()) != null) {
                temp.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading: " + e.getMessage());
        }
        return temp;
    }

    private void writeBackToFile(ArrayList<String> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BANK))) {
            for (String line : records) {
                writer.append(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing: " + e.getMessage());
        }
    }

    private double processDeposited(String username, double deposited, ArrayList<String> records) {
        double newBalance = -1;
        for (int i = 0; i < records.size(); i++) {
            String[] parts = records.get(i).replace("|$", " ").trim().split("\\s+");
            if (username.equals(parts[0])) {
                double currentBalance = Double.parseDouble(parts[1]);
                    newBalance = currentBalance + deposited;
                    String account = String.format("%-10s|$%-5.2f", username, newBalance);
                    records.set(i, account);
            }
        }
        return newBalance;
    }

    private double processWithdrawal(String username, double withdrawn, ArrayList<String> records) {
        double newBalance = -1;
        for (int i = 0; i < records.size(); i++) {
            String[] parts = records.get(i).replace("|$", " ").trim().split("\\s+");
            if (username.equals(parts[0])) {
                double currentBalance = Double.parseDouble(parts[1]);
                if (withdrawn > currentBalance) {
                    System.out.println("Not enough money!");
                    return -1;
                } else {
                    newBalance = currentBalance - withdrawn;
                    String account = String.format("%-10s|$%-5.2f", username, newBalance);
                    records.set(i, account);
                }
            }
        }
        return newBalance;
    }

    private void RecordActivity(String username,String money){

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(LOGS,true))) {
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatted = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = formatted.format(time);
            String activity = String.format("%-10s|%-10s|%-10s",username, money,formattedDate);

             writer.append(activity);
             writer.newLine();
        }catch (IOException e) {
                System.out.println("An error occurred while writing: " + e.getMessage());
            }
    }

    public void ViewActivity(String username){
        try (BufferedReader reader = new BufferedReader(new FileReader(LOGS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if(username.equals(parts[0])) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading: " + e.getMessage());
        }
    }
}
