package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 * CWD
 * └── .gitlet（GITLET_DIR）
 *     ├── objects（OBJECTS_DIR）
 *     ├── refs（REFS_DIR）
 *     ├   └── heads（HEADS_DIR）
 *     ├── addingstage
 *     └── HEAD（HEAD_FILE，文件）
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** 存commit和blob文件，文件名为其uid。 */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    /** The staing area */
    public static final File INDEX = join(GITLET_DIR, "index");
    public static final File masterBranch = join(HEADS_DIR, "master");


    // Gitlet init
    public static void gitletInit() throws IOException {
        OBJECTS_DIR.mkdirs();
        HEADS_DIR.mkdirs();
        HEAD_FILE.createNewFile();

        // Create initial commit
        Commit initCommit = new Commit(
                "initial commit",
                new Date(0),
                new ArrayList<String>(),
                new HashMap<>()
        );
        initCommit.save();

        // Setting HEAD point to master branch
        writeContents(HEAD_FILE, "ref: refs/heads/master");

        // Create master branch and point to the initial commit
        updateHead(initCommit.getUid());
    }

    // Gitlet add
    public static void gitletAdd(String filename) {
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
    }

    // Gitlet commmit
    public static void gitletCommit(String message) {
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
    }

    // Gitlet rm
    public static void gitletRm(String filenameToRemove) {
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

    }

    // Gitlet log
    public static void gitletLog() {
        String currentCommitUid = getHeadCommitId();

        while (currentCommitUid != null) {
            Commit currentCommmit = readObject(getFile(currentCommitUid), Commit.class);

            // 打印信息
            currentCommmit.print();

            // 如果是初始提交就break，否则后面get（0）会报错
            if (currentCommmit.getParents().isEmpty()) {
                break;
            }
            currentCommitUid = currentCommmit.getParents().get(0);
        }
    }

    // Gitlet global-log
    public static void gitletGlobalLog() {
        List<String> objectUid = plainFilenamesIn(OBJECTS_DIR);
        if (objectUid == null) {
            return;
        }
        for (String uid : objectUid) {
            File objectFile = getFile(uid);
            try {
                Commit commit = readObject(objectFile, Commit.class);
                commit.print();
            } catch (IllegalArgumentException | ClassCastException e) {
                // 如果文件不是一个 Commit 对象，readObject 会抛出异常。
                // 我们捕获这个异常并忽略它，继续处理下一个文件。
            }
        }
    }

    //
    public static String getCurrentBranchName() {
        String headContent = readContentsAsString(HEAD_FILE);
        String branchPath = headContent.split(" ")[1];
        return new File(branchPath).getName();
    }

    public static String getHeadCommitId() {
        String currentBranchName = getCurrentBranchName();
        File branchFile = join(HEADS_DIR, currentBranchName);
        return readContentsAsString(branchFile);
    }

    public static void updateHead(String commitUid) {
        String currentBranchName = getCurrentBranchName();
        File currentBranchFile = join(HEADS_DIR, currentBranchName);
        writeContents(currentBranchFile, commitUid);
    }

    public static File getHeadCommitFile() {
        return join(OBJECTS_DIR, getHeadCommitId());
    }

    public static File getFile(String uid) {
        return join(OBJECTS_DIR, uid);
    }
















}
