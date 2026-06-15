import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage

        while (true) {
            System.out.print("$ ");
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            if (s.equals("exit")) {
                break;
            }else if(s.equals("echo")){
                System.out.println(s);
            }
            System.out.println(s + ": command not found");
        }
    }
}
