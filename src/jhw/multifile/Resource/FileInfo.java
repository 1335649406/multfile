package jhw.multifile.Resource;

import java.io.File;
import java.util.Objects;

/**
 * @Author: wjh
 * @DateTime: 2020/7/30 11:03
 * @Description: SourceFileList�о��Ը��µ�һ���ļ���Ϣ
 */
public class FileInfo {
    //���Ը��µ����·��
    private String relativePaths;
    private long fileLen;
    //��SourceFileList���Ը��£������ļ��еı��
    private int fileNo;

    public FileInfo() {
    }

    public String getRelativePaths() {
        return relativePaths;
    }

    public void setRelativePaths(String relativePaths, String absoluteRoot, int fileNo) throws Exception {
        String absolutePath = absoluteRoot + relativePaths;
        File file = new File(absolutePath);
        if (file == null) {
            throw new Exception("�ļ�:" + absolutePath + "������");
        }
        this.fileLen = file.length();
        this.relativePaths = relativePaths;
        this.fileNo = fileNo;
    }

    public int getFileNo() {
        return fileNo;
    }

    public void setFileNo(int fileNo) {
        this.fileNo = fileNo;
    }

    public long getFileLen() {
        return fileLen;
    }

    public void setFileLen(long fileLen) {
        this.fileLen = fileLen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileInfo)) return false;
        FileInfo fileInfo = (FileInfo) o;
        return getFileNo() == fileInfo.getFileNo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRelativePaths(), getFileLen(), getFileNo());
    }

    @Override
    public String toString() {
        return fileNo + relativePaths + ":" + fileLen;
    }
}
