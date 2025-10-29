package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0);
        }
        // TODO: args 错误的几种情况

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (!GITLET_DIR.exists()) {
                    Repository.gitletInit();
                } else {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    System.exit(0);
                }
                break;

            case "add":
                checkInit();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                String filename = args[1];
                gitletAdd(filename);
                break;

            case "commit":
                checkInit();

                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                String message = args[1];

                if (message.isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }

                gitletCommit(message);
                break;

            case "rm":
                checkInit();

                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                String filenameToRemove = args[1];
                gitletRm(filenameToRemove);
                break;

            case "log":
                checkInit();
                gitletLog();
                break;

            case "global-log":
                checkInit();
                gitletGlobalLog();
                break;

            case "find":
                checkInit();
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                String messageInFind = args[1];
                gitletFind(messageInFind);
                break;

            case "status":
                checkInit();
                gitletStatus();
                break;

            case "checkout":
                checkInit();
                if (args.length == 3) {
                    if (!args[1].equals("--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    checkoutFile(args[2]);

                } else if (args.length == 4) {
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    checkoutFileFromCommit(args[1], args[3]);

                } else if (args.length == 2) {
                    checkoutBranch(args[1]);

                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;

            case "branch":
                checkInit();

                break;

            case "rm-branch":
                checkInit();

                break;

            case "reset":
                checkInit();

                break;

            case "merge":
                checkInit();

                break;
        }
    }
}
