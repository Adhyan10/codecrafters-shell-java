import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String input = sc.nextLine();

            if (input.trim().isEmpty()) {
                continue;
            }

            String[] inputArgs = parseArguments(input);
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
                    ProcessBuilder pb = new ProcessBuilder(inputArgs);
                    pb.inheritIO();

                    Process process = pb.start();
                    process.waitFor();
                } else {
                    System.out.println(command + ": command not found");
                }
            }
        }

        sc.close();
    }

    private static boolean isBuiltin(String cmd) {
        return cmd.equals("exit")
                || cmd.equals("echo")
                || cmd.equals("type");
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

    private static String[] parseArguments(String input) {
        List<String> args = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inSingleQuote = false;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '\'') {
                inSingleQuote = !inSingleQuote;
            } else if (Character.isWhitespace(ch) && !inSingleQuote) {
                if (current.length() > 0) {
                    args.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(ch);
            }
        }

        if (current.length() > 0) {
            args.add(current.toString());
        }

        return args.toArray(new String[0]);
    }
}