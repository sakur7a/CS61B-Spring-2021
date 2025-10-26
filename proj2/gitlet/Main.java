package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

                String filename = args[1];
                File fileToAdd = join(CWD, filename);

                // 文件不存在，退出
                if (!fileToAdd.exists()) {
                    System.out.println("File does not exist.");
                    System.exit(0);
                }

                // 读取文件内容转化为字节流，计算出对应的sha1值（blobID）
                byte[] content = Utils.readContents(fileToAdd);
                String uid = sha1(content);

                // 如果 blob 不存在，则创建并保存它
                File blobFile = join(OBJECTS_DIR, uid);
                if (!blobFile.exists()) {
                    writeContents(blobFile, content);
                }

                // 更新暂存区，将index文件反序列化，获取暂存区 Map
                HashMap<String, String> stagingArea = new HashMap<>();
                if (INDEX.exists()) {
                    stagingArea = readObject(INDEX, HashMap.class);
                }

                // 将 文件名 -> blob ID 的映射关系放入暂存区
                stagingArea.put(filename, uid);

                // 将更新后的暂存区 Map 序列化并写回 index 文件
                writeObject(INDEX, stagingArea);

                break;
            case "commit":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }

                String message = args[1];

                if (message.isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }

                // 暂存区为空
                if (!INDEX.exists()) {
                    System.out.println("No changes added to the commit.");
                    System.exit(0);
                }

                Date date = new Date();





                break;
        }
    }
}
