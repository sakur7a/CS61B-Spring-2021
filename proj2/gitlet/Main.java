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

                Staging stagingArea = Staging.fromFile();
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
                Staging staging = Staging.fromFile();

                // 暂存区为空，退出
                if (staging.isEmpty()) {
                    System.out.println("No changes added to the commit.");
                    System.exit(0);
                }

                // 获取上一次的commit，即head
                String headCommitid = getHeadCommitId();
                Commit parentCommit = readObject(getHeadCommitFile(), Commit.class);
                HashMap<String, String> currentBlobToFile = new HashMap<>(parentCommit.getPathToBlobId());

                // 应用现在的暂存区文件
                currentBlobToFile.putAll(staging.getFilenameToBlobId());

                // 删除被标记的文件
                for (String fileName : staging.getToRemoveFilename()) {
                    currentBlobToFile.remove(fileName);
                }


                List<String> parents = new ArrayList<>();
                parents.add(headCommitid);

                Commit commit = new Commit(message, new Date(), parents, currentBlobToFile);
                commit.save();

                updateHead(commit.getUid());

                // commmit之后清空暂存区
                staging.clear();
                break;
            case "rm":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                String filenameToRemove = args[1];

                Staging currentStaging = Staging.fromFile();
                Commit headCommit = readObject(getHeadCommitFile(), Commit.class);

                boolean flag = false;

                if (currentStaging.contains(filenameToRemove)) {
                    flag = true;
                    currentStaging.remove(filenameToRemove);
                }

                if (headCommit.contains(filenameToRemove)) {
                    flag = true;
                    currentStaging.addToRemove(filenameToRemove);
                    restrictedDelete(filenameToRemove);
                }

                if (!flag) {
                    System.out.println("No reason to remove the file.");
                    System.exit(0);
                } else {
                    currentStaging.save();
                }

                break;
            case "log":

                break;
            case "global-log":

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
