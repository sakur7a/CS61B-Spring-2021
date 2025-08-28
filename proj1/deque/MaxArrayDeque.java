package deque;

import java.util.Comparator;



public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmp = c;
    }

    public T max() {

        return max(cmp);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        int len = size();
        T mx = get(0);
        for (int i = 1; i < len; i++) {
            if (c.compare(get(i), mx) > 0) {
                mx = get(i);
            }
        }
        return mx;
    }
}

