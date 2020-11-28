package jhw.multifile.unreceive;

/**
 * @Author: wjh
 * @DateTime: 2020/8/1 9:50
 * @Description: ���ܵ�Ƭ����Ϣ
 */
public class SectionInfo {
    private long offset;
    private long len;

    public SectionInfo() {
    }

    public SectionInfo(long offset, long len) {
        this.offset = offset;
        this.len = len;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLen() {
        return len;
    }

    public void setLen(long len) {
        this.len = len;
    }

    public boolean isRange(long rec) {
        return len + offset > rec;
    }

    @Override
    public String toString() {
        return offset + ":" + len;
    }
}
