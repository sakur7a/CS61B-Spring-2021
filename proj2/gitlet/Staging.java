package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import static gitlet.Repository.INDEX;
import static gitlet.Utils.*;

public class Staging implements Serializable {
    public HashMap<String, String> map;


    // 更新暂存区，将index文件反序列化，获取暂存区 Map
    public Staging() {
        if (INDEX.exists()) {
            this.map = readObject(INDEX, HashMap.class);
        } else {
            this.map = new HashMap<>();
        }
    }

    // 将 文件名 -> blob ID 的映射关系放入暂存区
    public void add(String filename, String uid) {
        this.map.put(filename, uid);
    }

    // 将更新后的暂存区 Map 序列化并写回 index 文件
    public void save() {
        writeObject(INDEX, this.map);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    /** @return 一个包含暂存文件映射的 HashMap。 */
    public HashMap<String, String> getBlob() {
        return this.map;
    }

    /** 清空暂存区。 */
    public void clear() {
        if (INDEX.exists()) {
            INDEX.delete();
        }
    }

}
