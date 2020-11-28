package jhw.multifile.Main;

import jhw.multifile.Resource.SourceFileList;
import jhw.multifile.send.ResourceHolderNode;
import source_discovery.ResourceInfo;

/**
 * @Author: wjh
 * @DateTime: 2020/7/31 11:38
 * @Description: TODO
 */
public class Sender1Main {
    public static void main(String[] args) {
        ResourceInfo resourceInfo = new ResourceInfo("0001", "02", "0.0.1");
        SourceFileList sourceFileList = new SourceFileList();
        sourceFileList.setAbsoluteRoot("E:\\BaiduNetdiskDownload\\QQ Files\\");
        sourceFileList.collectFiles();

        ResourceHolderNode.initConfig("/Holder2.properties");
        ResourceHolderNode.scanRMIMapping();
        ResourceHolderNode resourceHolderNode = ResourceHolderNode.newInstance();
        resourceHolderNode.registry(resourceInfo, sourceFileList);
    }
}
