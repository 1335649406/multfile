package jhw.multifile.receive;

import jhw.filetransfer.readwrite.FileReadWrite;
import jhw.filetransfer.readwrite.IFileReadWriteInterceptor;
import jhw.filetransfer.readwrite.ReadWriteInterceptorAdapter;
import jhw.multifile.Resource.FileInfo;
import jhw.multifile.Resource.SourceFileList;
import jhw.multifile.unreceive.UnreceiveSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceFilePool {
    private Map<Integer, FileReadWrite> fileReadWriteMap;
    private Map<Integer, UnreceiveSection> unreceiveSectionMap;
    private IFileReadWriteInterceptor fileReadWriteInterceptor;

    public ResourceFilePool() {
        this.fileReadWriteMap = new HashMap<Integer, FileReadWrite>();
        this.unreceiveSectionMap = new HashMap<Integer, UnreceiveSection>();
        this.fileReadWriteInterceptor = new ReadWriteInterceptorAdapter();
    }

    public void addFileInfo(SourceFileList sourceFileList, FileInfo fileInfo) {
        int fileNo = fileInfo.getFileNo();
        FileReadWrite fileReadWrite = new FileReadWrite(fileNo, sourceFileList.getAbsoluteRoot() + fileInfo.getRelativePaths());
        fileReadWrite.setReadWriteInterceptor(fileReadWriteInterceptor);
        this.unreceiveSectionMap.put(fileNo, new UnreceiveSection(fileNo, fileInfo.getFileLen()));
        fileReadWriteMap.put(fileNo, fileReadWrite);
    }

    public void addFileInfoList(SourceFileList sourceFileList) {
        List<FileInfo> fileInfoList = sourceFileList.getFileInfoList();
        for (FileInfo fileInfo : fileInfoList) {
            addFileInfo(sourceFileList, fileInfo);
        }
    }

    public FileReadWrite getFileReadWrite(int fileNo) {
        return fileReadWriteMap.get(fileNo);
    }

    public UnreceiveSection getUnreceiveSection(int fileNo) {
        return this.unreceiveSectionMap.get(fileNo);
    }

    public void setFileReadWriteInterceptor(IFileReadWriteInterceptor fileReadWriteInterceptor) {
        this.fileReadWriteInterceptor = fileReadWriteInterceptor;
    }
}
