package jhw.multifile.send;

import jhw.filetransfer.FileSectionInfo;
import source_discovery.ResourceInfo;

import java.util.List;

public interface IResourceSender {
    void send(String receiveIp, int receivePort, ResourceInfo baseInfo, List<FileSectionInfo> sectionInfos);
}
