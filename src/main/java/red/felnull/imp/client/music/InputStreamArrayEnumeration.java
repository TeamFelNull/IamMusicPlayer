package red.felnull.imp.client.music;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class InputStreamArrayEnumeration implements Enumeration {
    private final List<InputStream> inpuStrereadmList;
    private int ct;

    public InputStreamArrayEnumeration() {
        this.inpuStrereadmList = new ArrayList<>();
    }

    @Override
    public boolean hasMoreElements() {
        return ct < inpuStrereadmList.size();
    }

    @Override
    public InputStream nextElement() {
        if (hasMoreElements()) {
            InputStream stream = inpuStrereadmList.get(ct);
            ct++;
            return stream;
        }
        return null;
    }

    public void add(InputStream stream) {
        this.inpuStrereadmList.add(stream);
    }

    public void clear() {
        ct = 0;
        this.inpuStrereadmList.clear();
    }

    public InputStream get(int index) {
        return inpuStrereadmList.get(index);
    }

    public int curentCont() {
        return ct;
    }

    public boolean isEmpty() {
        return inpuStrereadmList.isEmpty();
    }
}
