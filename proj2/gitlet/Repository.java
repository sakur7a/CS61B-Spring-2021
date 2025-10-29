package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
            Commit currentCommmit = readObject(join(OBJECTS_DIR, currentCommitUid), Commit.class);

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
            File objectFile = join(OBJECTS_DIR, uid);
            try {
                Commit commit = readObject(objectFile, Commit.class);
                commit.print();
            } catch (IllegalArgumentException | ClassCastException e) {
                // 如果文件不是一个 Commit 对象，readObject 会抛出异常。
                // 我们捕获这个异常并忽略它，继续处理下一个文件。
            }
        }
    }

    // Gitlet find
    public static void gitletFind(String message) {
        List<String> objectUid = plainFilenamesIn(OBJECTS_DIR);
        if (objectUid == null) {
            return;
        }

        // 标记有没有找到对应的commit
        boolean flag = false;

        for (String uid : objectUid) {
            File objectFile = join(OBJECTS_DIR, uid);
            try {
                Commit commit = readObject(objectFile, Commit.class);
                if (commit.getMessage().equals(message)) {
                    flag = true;
                    System.out.println(uid);
                }
            } catch (IllegalArgumentException | ClassCastException e) {
                // 如果文件不是一个 Commit 对象，readObject 会抛出异常。
                // 我们捕获这个异常并忽略它，继续处理下一个文件。
            }
        }

        if (!flag) {
            System.out.println("Found no commit with that message.");
        }
    }

    // Gitlet status
    public static void gitletStatus() {
        System.out.println("=== Branches ===");
        List<String> branchName = plainFilenamesIn(HEADS_DIR);
        String currentBranch = getCurrentBranchName();
        if (!branchName.isEmpty()) {
            for (String branch : branchName) {
                if (branch.equals(currentBranch)) {
                    System.out.print("*");
                }
                System.out.println(branch);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        Staging currentStaging = Staging.fromFile();
        HashMap<String, String> filename = currentStaging.getFilenameToBlobId();
        Set<String> keys = filename.keySet();
        for (String key : keys) {
            System.out.println(key);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        HashSet<String> toRemoveFilename = currentStaging.getToRemoveFilename();
        for (String file : toRemoveFilename) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    // Gitlet checkout situation 1
    public static void checkoutFile(String filename) {
        checkoutHelper(getHeadCommitId(), filename);
    }

    // Gitlet checkout situation 2
    public static void checkoutFileFromCommit(String commitUid, String filename) {
        List<String> objectIds = plainFilenamesIn(OBJECTS_DIR);
        String fullUid = null;

        if (!objectIds.isEmpty()) {
            for (String uid : objectIds) {
                if (uid.startsWith(commitUid)) {
                    fullUid = uid;
                    break;
                }
            }
        }

        if (fullUid == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        checkoutHelper(fullUid, filename);
    }

    // Gitlet checkout situation 3
    public static void checkoutBranch(String branch) {
        // 分支不存在
        List<String> allBranchs = plainFilenamesIn(HEADS_DIR);
        if (allBranchs == null || !allBranchs.contains(branch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        // 切换分支是当前分支
        if (branch.equals(getCurrentBranchName())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        // 工作区有未跟踪的文件会被覆盖

        // 获取目标分支commit中跟踪的文件
        String targetCommitUid = readContentsAsString(join(HEADS_DIR branch));
        Commit targetCommit = readObject(join(OBJECTS_DIR, targetCommitUid) ,Commit.class)
        Set<String> targetTrackedFile = targetCommit.getPathToBlobId().keySet();

        // 获取当前commit和暂存区跟踪的文件
        Commit headCommit = readObject(getHeadCommitFile(), Commit.class);
        Set<String> headTractedFile = headCommit.getPathToBlobId().keySet();
        Staging currentStaging = Staging.fromFile();
        Set<String> stagingFile = currentStaging.getFilenameToBlobId().keySet();

        // 找出工作目录的未跟踪文件，并检查其是否会被覆盖
        List<String> cwdFile = plainFilenamesIn(CWD);
        if (cwdFile != null) {
            for (String file : cwdFile) {
                // 条件：文件未被当前 commit 跟踪，也未被暂存，但将被目标分支检出
                if (!headTractedFile.contains(file) && !stagingFile.contains(file) && targetCommit.contains(file)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }


        // TODO: 检出目标分支的所有文件
        // TODO: 删除多余的被跟踪文件
        // TODO: 清空暂存区
        // TODO: 更新 HEAD 指针


    }

    private static void checkoutHelper(String uid, String filename) {
        Commit commit = readObject(join(OBJECTS_DIR, uid), Commit.class);
        HashMap<String, String> filePathToBlobId = commit.getPathToBlobId();

        if (!filePathToBlobId.containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        // 获取对应的blobId，读取内容并写入到工作区文件
        String blobId = filePathToBlobId.get(filename);
        File file = join(OBJECTS_DIR, blobId);
        byte[] fileContent = readContents(file);

        File fileInCWD = join(CWD, filename);
        writeContents(fileInCWD, fileContent);
    }


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

    public static void checkInit() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }




}
