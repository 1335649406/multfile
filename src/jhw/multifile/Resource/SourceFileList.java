package jhw.multifile.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SourceFileList {
    private List<FileInfo> fileInfoList;
    private String absoluteRoot;

    public SourceFileList() {
        fileInfoList = new ArrayList<FileInfo>();
    }

    public String getAbsoluteRoot() {
        return absoluteRoot;
    }

    public void setAbsoluteRoot(String absoluteRoot) {
        this.absoluteRoot = absoluteRoot;
    }

    public void addFileInfo(String filePath) {
        FileInfo fileInfo = new FileInfo();
        filePath = filePath.replace(absoluteRoot, "");
        int size = fileInfoList.size();
        try {
            fileInfo.setRelativePaths(filePath, absoluteRoot, size);
            fileInfoList.add(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileInfo getFileInfoByNo(int fileNo) throws Exception {
        FileInfo temp = new FileInfo();
        temp.setFileNo(fileNo);
        FileInfo target = fileInfoList.get(fileInfoList.indexOf(temp));
        if (target == null) {
            throw new Exception("û���ļ����Ϊ��" + fileNo + "���ļ�");
        }
        return target;
    }

    public void collectFiles() {
        if (this.absoluteRoot == null) {
            return;
        }
        collectFiles(absoluteRoot);
    }

    private void collectFiles(String curPath) {
        File file = new File(curPath);
        File[] files = file.listFiles();

        for (File file1 : files) {
            String absolutePath = file1.getAbsolutePath();
            if (file1.isFile()) {
                addFileInfo(file1.getAbsolutePath());
            } else {
                collectFiles(file1.getAbsolutePath());
            }
        }
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(absoluteRoot + ":");
        for (FileInfo fileInfo : fileInfoList) {
            buffer.append("\n\t");
            buffer.append(fileInfo);
        }
        return buffer.toString();
    }
}
