package jhw.multifile.send;

import jhw.filetransfer.FileSectionInfo;
import jhw.multifile.Resource.SourceFileList;
import jhw.multifile.receive.ResourceFilePool;
import source_discovery.ResourceInfo;

import java.util.List;

/**
 * @Author: wjh
 * @DateTime: 2020/7/31 9:16
 * @Description: TODO
 */
public class ResourceSender implements IResourceSender {
    private ResourceHolderNode holderNode;
    private FileSender fileSender;
//    private int sendCount;

    public ResourceSender() {
        this.holderNode = ResourceHolderNode.newInstance();
    }

    @Override
    public void send(String receiveIp, int receivePort, ResourceInfo baseInfo
            , List<FileSectionInfo> sectionInfos) {
        System.out.println("收到文件接收方的一次发送请求");
        SourceFileList sourceFileList = holderNode.getSourceFileList(baseInfo);
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        resourceFilePool.addFileInfoList(sourceFileList);

        this.fileSender = new FileSender(receiveIp, receivePort, resourceFilePool, sectionInfos);
        this.fileSender.startSend();
    }
}
