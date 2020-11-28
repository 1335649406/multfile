package jhw.multifile.strategy;

import jhw.filetransfer.FileSectionInfo;
import jhw.multifile.Resource.FileInfo;
import jhw.multifile.Resource.SourceFileList;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wjh
 * @DateTime: 2020/7/30 19:43
 * @Description: TODO
 */
public class FileDistributionStrategy implements IDistributionStrategy {
    private static final int DEFAULT_SECTION_LEN = 1 << 23;   //8M
    private long sectionLen;

    public FileDistributionStrategy() {
        this.sectionLen = DEFAULT_SECTION_LEN;
    }

    public long getSectionLen() {
        return sectionLen;
    }

    public void setSectionLen(long sectionLen) {
        this.sectionLen = sectionLen;
    }

    @Override
    public List<List<FileSectionInfo>> distributionStrategy(SourceFileList sourceFileList, int sendCount) {
        List<FileInfo> fileInfoList = sourceFileList.getFileInfoList();
        List<List<FileSectionInfo>> res = new ArrayList<List<FileSectionInfo>>();

        for (int i = 0; i < sendCount; i++) {
            res.add(i, new ArrayList<FileSectionInfo>());
        }
        int index = 0;
        for (FileInfo fileInfo : fileInfoList) {
            long restLen = fileInfo.getFileLen();
            long offset = 0;
            int len = 0;

            while (restLen > 0) {
                len = (int) (Math.min(restLen, sectionLen));
                FileSectionInfo fileSectionInfo = new FileSectionInfo(fileInfo.getFileNo(), offset, len);
                offset += len;
                restLen -= len;

                res.get(index++).add(fileSectionInfo);
                if (index >= sendCount) {
                    index = index % sendCount;
                }
            }
        }

        return res;
    }
}
