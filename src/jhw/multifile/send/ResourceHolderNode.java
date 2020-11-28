package jhw.multifile.send;

import com.mec.util.PropertiesParser;
import jhw.multifile.Resource.Resources;
import jhw.multifile.Resource.SourceFileList;
import rmi.core.RMIFactory;
import source_discovery.ResourceInfo;
import source_discovery.resourcer.ResourceHolder;

public class ResourceHolderNode extends ResourceHolder {
    private static volatile ResourceHolderNode me;
    private static String registryIp;
    private static int registryPort;
    private static String ip;
    private static int port;

    public static void scanRMIMapping() {
        RMIFactory.scanMapping("/RegistryCenterMapping.xml");
        RMIFactory.scanMapping("/RMIMapping1.xml");
    }

    public static void initConfig(String filePath) {
        PropertiesParser parser = new PropertiesParser();
        parser.loadProperties(filePath);
        registryIp = parser.value("registryIp");
        registryPort = Integer.parseInt(parser.value("registryPort"));
        port = Integer.parseInt(parser.value("holderPort"));
    }

    public static ResourceHolderNode newInstance() {
        if (me == null) {
            synchronized (ResourceHolderNode.class) {
                if (me == null) {
                    System.out.println("文件拥有者开启RMIServer侦听");
                    me = new ResourceHolderNode();
                }
            }
        }
        return me;
    }

    private ResourceHolderNode() {
        super(ResourceHolderNode.port);
        super.setRegistryIp(ResourceHolderNode.registryIp);
        super.setRegistryPort(ResourceHolderNode.registryPort);
    }

    public void registry(ResourceInfo resourceInfo, SourceFileList sourceFileList) {
        Resources.addResource(resourceInfo, sourceFileList);
        me.registry(resourceInfo);
    }

    public void logout(ResourceInfo resourceInfo) {
        Resources.removeResource(resourceInfo);
        me.logout(resourceInfo);
    }

    public SourceFileList getSourceFileList(ResourceInfo resourceInfo) {
        return Resources.getResource(resourceInfo);
    }
}
