package jhw.multifile.receive;

import balance.DefaultNetNode;
import jhw.filetransfer.FileSectionInfo;
import jhw.multifile.Resource.SourceFileList;
import jhw.multifile.send.IResourceSender;
import jhw.multifile.strategy.FileDistributionStrategy;
import jhw.multifile.strategy.IDistributionStrategy;
import jhw.multifile.strategy.ISenderSelectedStrategy;
import rmi.core.RMIClient;
import rmi.core.RMIFactory;
import source_discovery.ResourceInfo;
import source_discovery.resourcer.ResourceRequester;

import javax.swing.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;

/**
 * @Author: wjh
 * @DateTime: 2020/7/30 14:49
 * @Description: �ļ����ܵĿ�ʼ
 */
public class ReceiveStart implements IReceiveStart {
    private ResourceInfo resourceInfo;
    private SourceFileList sourceFileList;
    private ResourceRequester resourceRequester;

    private RMIClient rmiClient;
    private IResourceSender sender;
    private ReceiveServer receiveServer;

    private String ip;
    private int localPort;
    private int senderCount;

    private ISenderSelectedStrategy senderSelected;
    private IDistributionStrategy distributionStrategy;

    static {
        RMIFactory.scanMapping("/RMIMapping1.xml");
        RMIFactory.scanMapping("/RegistryCenterMapping.xml");
    }

    public ReceiveStart() {
        this.resourceRequester = new ResourceRequester();
        this.rmiClient = new RMIClient();
        this.receiveServer = new ReceiveServer();

        this.distributionStrategy = new FileDistributionStrategy();
    }

    public void setAbsoluteRoot(String absoluteRoot) {
        this.sourceFileList.setAbsoluteRoot(absoluteRoot);
    }

    public void setRegistryIp(String ip) {
        resourceRequester.setRegistryIp(ip);
    }

    public void setRegistryPort(int port) {
        this.resourceRequester.setRegistryPort(port);
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setDistributionStrategy(IDistributionStrategy distributionStrategy) {
        this.distributionStrategy = distributionStrategy;
    }

    @Override
    public void setResource(ResourceInfo resourceInfo, SourceFileList sourceFileList) {
        this.resourceInfo = resourceInfo;
        this.sourceFileList = sourceFileList;
    }

    @Override
    public boolean getResourceFiles() throws Exception {
        List<DefaultNetNode> addressList = resourceRequester.getAddressList(resourceInfo);
        if (addressList == null) {
            return false;
        }

        InetAddress localHost = InetAddress.getLocalHost();
        this.ip = localHost.getHostAddress();
        List<List<FileSectionInfo>> lists = fileDistribution(addressList);

        this.receiveServer.setPort(localPort);
        this.receiveServer.setReceiveCount(senderCount);
        this.receiveServer.setSourceFileList(sourceFileList);
        this.receiveServer.enableReceiveProgressDialog(new JFrame("wen"), senderCount);
        this.receiveServer.startup();
        for (int i = 0; i < addressList.size(); i++) {
            DefaultNetNode senderNode = addressList.get(i);
            List<FileSectionInfo> sectionInfos = lists.get(i);
            rmiClient.setPort(senderNode.getPort());
            rmiClient.setIp(senderNode.getIp());
            try {
                this.sender = rmiClient.getProxy(IResourceSender.class);
                System.out.println("告诉客户端文件具体信息");
                sender.send(Inet4Address.getLocalHost().getHostAddress(), localPort, resourceInfo, sectionInfos);
                rmiClient.shutdown();
            } catch (Exception e) {

            }
        }

        return true;
    }

    private List<List<FileSectionInfo>> fileDistribution(List<DefaultNetNode> addressList) {
        if (senderSelected == null) {
            senderSelected = new SenderSelected();
        }
        addressList = senderSelected.senderSelected(addressList);
        this.senderCount = addressList.size();
        List<List<FileSectionInfo>> lists = distributionStrategy
                .distributionStrategy(sourceFileList, senderCount);

        return lists;
    }

    class SenderSelected implements ISenderSelectedStrategy {

        @Override
        public List<DefaultNetNode> senderSelected(List<DefaultNetNode> addressList) {
            System.out.println("拿到的结点：" + addressList.size());
            return addressList;
        }
    }
}
