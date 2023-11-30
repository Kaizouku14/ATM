import java.io.*;

public class UserManagement {

    private String username;
    private String password;
    private static final String BANK_USERS  = "BankAccounts.txt";

    //Getters and setters
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}

    private boolean CheckFile() throws FileNotFoundException {
        File file = new File(BANK_USERS);
        if(!file.exists())
            throw new FileNotFoundException("File Not Found" + BANK_USERS);
        return file.length() == 0;
    }

    public boolean checkPassword(String password){
         int length = password.length();
              if(length < 8 || length > 16){
                  System.out.println("Password must contain 8 characters!");
                  return true;
              }
        return false;
    }

    private boolean checkAccount(String username){
        try(BufferedReader reader = new BufferedReader(new FileReader(BANK_USERS))){
            String line;
            if(CheckFile()){
                System.out.println("File does not have data yet!");
            }else {
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.replace("|", " ").trim().split("\\s+");
                    if (parts[0].equalsIgnoreCase(username))
                        return true;
                }
            }
        }catch (IOException e) {
            System.out.println("An error Occurred!" + e.getMessage());
        }
        return false;
    }

    public void SignupAccount(String username, String password){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(BANK_USERS,true))) {
            if(checkAccount(username)){
                System.out.println("Account Already Exist!");
            }else{
                String data = String.format("%-10s|%-10s", username , password);
                writer.append(data);
                writer.newLine();
            }
        }catch (IOException e){
            System.out.println("An error Occurred!" + e.getMessage());
        }
    }

    public boolean LoginAccount(String username, String password){
        try(BufferedReader reader = new BufferedReader(new FileReader(BANK_USERS))){
              String line;
           if(CheckFile()){
                 System.out.println("File does not have data yet!");
           }else {
               while ((line = reader.readLine()) != null) {
                   String[] parts = line.replace("|", " ").trim().split("\\s+");
                   if (parts[0].equalsIgnoreCase(username) && parts[1].equalsIgnoreCase(password))
                        return true;
               }
           }
        }catch (IOException e) {
            System.out.println("An error Occurred!" + e.getMessage());
        }
        return false;
    }
}
