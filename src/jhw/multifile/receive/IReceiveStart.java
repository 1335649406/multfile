package jhw.multifile.receive;

import jhw.multifile.Resource.SourceFileList;
import source_discovery.ResourceInfo;

/**
 * @Author: wjh
 * @DateTime: 2020/7/30 14:50
 * @Description: �ṩ��app�����ļ��Ľӿ�
 */
public interface IReceiveStart {
    void setResource(ResourceInfo resourceInfo, SourceFileList sourceFileList) throws Exception;

    boolean getResourceFiles() throws Exception;
}
