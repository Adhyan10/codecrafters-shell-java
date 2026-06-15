import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
        // System.out.print("$ ");

       
        
        // String s = sc.nextLine();

        // System.out.print(s + ": command not found");

        while(true){
             System.out.print("$ ");
              Scanner sc = new Scanner(System.in);
             String s = sc.nextLine();
            System.out.println(s + ": command not found");
        }
    }
}
 