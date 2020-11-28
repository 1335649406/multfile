package jhw.multifile.Main;

import jhw.multifile.Resource.SourceFileList;
import jhw.multifile.receive.ReceiveStart;
import source_discovery.ResourceInfo;

/**
 * @Author: wjh
 * @DateTime: 2020/7/31 11:37
 * @Description: TODO
 */
public class ReceiveMain {
    public static void main(String[] args) {
        ResourceInfo resourceInfo = new ResourceInfo("0001", "02", "0.0.1");
        SourceFileList sourceFileList = new SourceFileList();
        sourceFileList.setAbsoluteRoot("E:\\BaiduNetdiskDownload\\QQ Files\\");
        sourceFileList.collectFiles();

        ReceiveStart receiveStart = new ReceiveStart();
        try {
            receiveStart.setResource(resourceInfo, sourceFileList);
            receiveStart.setAbsoluteRoot("E:\\tmp\\");
            receiveStart.setRegistryIp("127.0.0.1");
            receiveStart.setLocalPort(54175);
            receiveStart.setRegistryPort(54170);
            boolean res = receiveStart.getResourceFiles();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
