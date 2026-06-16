import java.util.Scanner;
import java.io.File;

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
                    // Search in PATH
                    String path = System.getenv("PATH");

                    if (path == null) {
                        System.out.println(command + ": not found");
                        break;
                    }

                    String[] directories = path.split(File.pathSeparator);

                    for (int i = 0; i < directories.length; i++) {
                        String dir = directories[i];
                        File file = new File(dir, command);
                        if (file.isFile() && file.canExecute()) {
                            System.out.println(command + " is " + file.getAbsolutePath());
                           break;
                        }
                    }

                    System.out.println(command + ": not found");
                }
            } else {
                System.out.println(s + ": command not found");
            }
        }
    }
}
