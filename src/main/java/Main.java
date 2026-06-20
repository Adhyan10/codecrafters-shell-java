import java.io.File;
import java.io.PrintWriter;
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

            String outputFile = null;
            String errorFile = null;
            boolean appendOutput = false;
            boolean appendError = false;

            List<String> filteredArgs = new ArrayList<>();

            for (int i = 0; i < inputArgs.length; i++) {

                if (inputArgs[i].equals(">") || inputArgs[i].equals("1>")) {
                    if (i + 1 < inputArgs.length) {
                        outputFile = inputArgs[i + 1];
                        appendOutput = false;
                        i++;
                    }
                }

                else if (inputArgs[i].equals(">>") || inputArgs[i].equals("1>>")) {
                    if (i + 1 < inputArgs.length) {
                        outputFile = inputArgs[i + 1];
                        appendOutput = true;
                        i++;
                    }
                }

                else if (inputArgs[i].equals("2>")) {
                    if (i + 1 < inputArgs.length) {
                        errorFile = inputArgs[i + 1];
                        appendError = false;
                        i++;
                    }
                }

                else if (inputArgs[i].equals("2>>")) {
                    if (i + 1 < inputArgs.length) {
                        errorFile = inputArgs[i + 1];
                        appendError = true;
                        i++;
                    }
                }

                else {
                    filteredArgs.add(inputArgs[i]);
                }
            }

            inputArgs = filteredArgs.toArray(new String[0]);

            String command = inputArgs[0];

            if (errorFile != null && !appendError) {
                new PrintWriter(errorFile).close();
            }

            if (command.equals("exit")) {
                break;
            }

            else if (command.equals("echo")) {

                String output = String.join(
                        " ",
                        Arrays.copyOfRange(inputArgs, 1, inputArgs.length)
                );

                if (outputFile != null) {
                    try (java.io.FileWriter fw =
                                 new java.io.FileWriter(outputFile, appendOutput);
                         PrintWriter pw = new PrintWriter(fw)) {
                        pw.println(output);
                    }
                } else {
                    System.out.println(output);
                }

            }

            else if (command.equals("type")) {

                if (inputArgs.length > 1) {
                    String cmdToType = inputArgs[1];

                    String output;

                    if (isBuiltin(cmdToType)) {
                        output = cmdToType + " is a shell builtin";
                    } else {
                        String path = getPath(cmdToType);

                        if (path != null) {
                            output = cmdToType + " is " + path;
                        } else {
                            output = cmdToType + ": not found";
                        }
                    }

                    if (outputFile != null) {
                        try (java.io.FileWriter fw =
                                     new java.io.FileWriter(outputFile, appendOutput);
                             PrintWriter pw = new PrintWriter(fw)) {
                            pw.println(output);
                        }
                    } else {
                        System.out.println(output);
                    }
                }

            }

            else {

                String path = getPath(command);

                if (path != null) {
                    ProcessBuilder pb = new ProcessBuilder(inputArgs);

                    if (outputFile != null) {
                        if (appendOutput) {
                            pb.redirectOutput(
                                    ProcessBuilder.Redirect.appendTo(
                                            new File(outputFile)
                                    )
                            );
                        } else {
                            pb.redirectOutput(new File(outputFile));
                        }
                    } else {
                        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    }

                    if (errorFile != null) {
                        if (appendError) {
                            pb.redirectError(
                                    ProcessBuilder.Redirect.appendTo(
                                            new File(errorFile)
                                    )
                            );
                        } else {
                            pb.redirectError(new File(errorFile));
                        }
                    } else {
                        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                    }

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
        boolean inDoubleQuote = false;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '\\' && inDoubleQuote) {
                if (i + 1 < input.length()) {
                    char next = input.charAt(i + 1);

                    if (next == '"' || next == '\\') {
                        current.append(next);
                        i++;
                    } else {
                        current.append('\\');
                        current.append(next);
                        i++;
                    }
                } else {
                    current.append('\\');
                }
            }

            else if (ch == '\\' && !inSingleQuote && !inDoubleQuote) {
                if (i + 1 < input.length()) {
                    current.append(input.charAt(i + 1));
                    i++;
                }
            }

            else if (ch == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            }

            else if (ch == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            }

            else if (Character.isWhitespace(ch) && !inSingleQuote && !inDoubleQuote) {
                if (current.length() > 0) {
                    args.add(current.toString());
                    current.setLength(0);
                }
            }

            else {
                current.append(ch);
            }
        }

        if (current.length() > 0) {
            args.add(current.toString());
        }

        return args.toArray(new String[0]);
    }
}