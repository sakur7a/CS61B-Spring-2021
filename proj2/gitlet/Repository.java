package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    /** The staing area */
    public static final File INDEX = join(GITLET_DIR, "index");
    public static final File masterBranch = join(HEADS_DIR, "master");
    public HashMap<String, String> stagingArea;

    /* TODO: fill in the rest of this class. */

    // Gitlet init
    public static void init() throws IOException {
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















}
