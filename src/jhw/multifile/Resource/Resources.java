package jhw.multifile.Resource;

import source_discovery.ResourceInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wjh
 * @DateTime: 2020/7/30 11:03
 * @Description: ����һ����Դӵ���ߣ����ǵ�����
 */
public class Resources {
    private static final Map<ResourceInfo, SourceFileList> resources;

    static {
        resources = new HashMap<ResourceInfo, SourceFileList>();
    }

    public Resources() {
    }

    public static void addResource(ResourceInfo resourceInfo, SourceFileList sourceFileList) {
        resources.put(resourceInfo, sourceFileList);
    }

    public static SourceFileList removeResource(ResourceInfo resourceInfo) {
        return resources.remove(resourceInfo);
    }

    public static SourceFileList getResource(ResourceInfo resourceInfo) {
        return resources.get(resourceInfo);
    }
}
