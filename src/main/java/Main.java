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
            }else if(s.startsWith("echo ")){
                System.out.println(s.substring(5));
            }else if(s.equals("type")){
                if(s.equals("type") || s.equals("exit") || s.equals("echo")){
                    System.out.println(s +" is a shell builtin");
                }
            }
            else{
            System.out.println(s + ": command not found");
            }
        }
    }
}
