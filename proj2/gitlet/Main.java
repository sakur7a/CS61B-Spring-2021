package gitlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0);
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if (!Repository.GITLET_DIR.exists()) {
                    Repository.init();
                } else {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    System.exit(0);
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command


                break;
            // TODO: FILL THE REST IN
        }
    }
}
