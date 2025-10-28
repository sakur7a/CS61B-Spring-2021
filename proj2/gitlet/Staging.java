package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Repository.INDEX;
import static gitlet.Utils.*;

public class Staging implements Serializable {
    private HashMap<String, String> filenameToBlobId;
    private HashSet<String> toRemoveFilename;


    // 更新暂存区，将index文件反序列化，获取暂存区 Map
    public Staging() {
        this.filenameToBlobId = new HashMap<>();
        this.toRemoveFilename = new HashSet<>();
    }

    public static Staging fromFile() {
        if (!INDEX.exists()) {
            return new Staging(); // 如果文件不存在，返回一个新的空暂存区
        }
        return readObject(INDEX, Staging.class);
    }

    // 将 文件名 -> blob ID 的映射关系放入暂存区
    public void add(String filename, String uid) {
        this.filenameToBlobId.put(filename, uid);
    }

    // 将暂存区序列化并写回 index 文件
    public void save() {
        writeObject(INDEX, this);
    }

    // 暂存区没有修改或者标记为删除的文件
    public boolean isEmpty() {
        return this.filenameToBlobId.isEmpty() && this.toRemoveFilename.isEmpty();
    }

    /** @return 一个包含暂存文件映射的 HashMap。 */
    public HashMap<String, String> getFilenameToBlobId() {
        return this.filenameToBlobId;
    }

    public HashSet<String> getToRemoveFilename() {
        return this.toRemoveFilename;
    }

    /** 清空暂存区。 */
    public void clear() {
        restrictedDelete(INDEX);
    }

    public void remove(String filename) {
        this.filenameToBlobId.remove(filename);
    }

    public boolean contains(String filename) {
        return this.filenameToBlobId.containsKey(filename);
    }

    public void addToRemove(String filename) {
        toRemoveFilename.add(filename);
    }

}
