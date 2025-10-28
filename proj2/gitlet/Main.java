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
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                if (!GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    System.exit(0);
                }

                String filename = args[1];
                gitletAdd(filename);
                break;

            case "commit":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                if (!GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
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
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                String filenameToRemove = args[1];
                gitletRm(filenameToRemove);
                break;

            case "log":
                gitletLog();
                break;

            case "global-log":
                gitletGlobalLog();
                break;

            case "find":

                break;

            case "status":

                break;

            case "checkout":

                break;

            case "branch":

                break;

            case "rm-branch":

                break;

            case "reset":

                break;

            case "merge":

                break;
        }
    }
}
