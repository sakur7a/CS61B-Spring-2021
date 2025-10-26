package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                    Repository.init();
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
                File fileToAdd = join(CWD, filename);

                // 文件不存在，退出
                if (!fileToAdd.exists()) {
                    System.out.println("File does not exist.");
                    System.exit(0);
                }

                // 读取文件内容转化为字节流，计算出对应的sha1值（blobID）
                byte[] bytes = Utils.readContents(fileToAdd);

                Blob blob = new Blob(bytes);
                blob.save();

                Staging stagingArea = new Staging();
                stagingArea.add(filename, blob.getUid());
                stagingArea.save();
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

                // 读取暂存区
                Staging staging = new Staging();

                // 暂存区为空，退出
                if (staging.isEmpty()) {
                    System.out.println("No changes added to the commit.");
                    System.exit(0);
                }

                String headCommitid = getHeadCommitId();
                List<String> parents = new ArrayList<>();
                parents.add(headCommitid);

                Commit commit = new Commit(message, new Date(), parents, staging.getBlob());
                commit.save();

                updateHead(commit.getUid());

                // commmit之后清空暂存区
                staging.clear();
                break;
        }
    }
}
