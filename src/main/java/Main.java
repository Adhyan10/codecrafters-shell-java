import java.io.File;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String input = sc.nextLine();

            if (input.trim().isEmpty()) {
                continue;
            }

            String[] inputArgs = input.trim().split("\\s+");
            String command = inputArgs[0];

            if (command.equals("exit")) {
                break;
            } else if (command.equals("echo")) {
                System.out.println(
                    String.join(" ", Arrays.copyOfRange(inputArgs, 1, inputArgs.length))
                );
            } else if (command.equals("type")) {
                if (inputArgs.length > 1) {
                    String cmdToType = inputArgs[1];

                    if (isBuiltin(cmdToType)) {
                        System.out.println(cmdToType + " is a shell builtin");
                    } else {
                        String path = getPath(cmdToType);

                        if (path != null) {
                            System.out.println(cmdToType + " is " + path);
                        } else {
                            System.out.println(cmdToType + ": not found");
                        }
                    }
                }
            } else {
                String path = getPath(command);

                if (path != null) {
                    String[] processArgs = inputArgs.clone();
                    processArgs[0] = path;

                    ProcessBuilder pb = new ProcessBuilder(processArgs);
                    pb.inheritIO();

                    Process process = pb.start();
                    process.waitFor();
                } else {
                    System.out.println(command + ": command not found");
                }
            }
        }
    }

    private static boolean isBuiltin(String cmd) {
        return cmd.equals("exit") || cmd.equals("echo") || cmd.equals("type");
    }

    private static String getPath(String command) {
        String pathEnv = System.getenv("PATH");

        if (pathEnv != null) {
            String[] directories = pathEnv.split(File.pathSeparator);

            for (String dir : directories) {
                File file = new File(dir, command);

                if (file.isFile() && file.canExecute()) {
                    return file.getAbsolutePath();
                }
            }
        }

        return null;
    }
}