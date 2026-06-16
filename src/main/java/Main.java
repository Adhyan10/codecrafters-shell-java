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
            } else if (s.startsWith("echo ")) {
                System.out.println(s.substring(5));
            } else if (s.startsWith("type ")) {
                String command = s.substring(5);
                if (command.equals("type") || command.equals("exit") || command.equals("echo")) {
                    System.out.println(command + " is a shell builtin");
                } else {
                    System.out.println(command + ": not found");
                }
            }else if(s.startsWith("type ")){
                String x = s.substring(5);
                if(x.equals("exit") || x.equals("type") || x.equals("echo")){
                    System.out.println(x + " is a shell builtin");
                }
            }else if(s.startsWith("type ")){
                String a = s.substring(5);
                if(a.equals("exit") || a.equals("type") || a.equals("echo")){
                    System.err.println(a + " is a shell builtin");
                }else if(a.exists() && a.canExecute()){
                    System.out.println(a +" is " + a.getAbsolutePath());
                }
            }
             else {
                System.out.println(s + ": command not found");
            }
        }
    }
}
