package jhw.multifile.strategy;

import jhw.filetransfer.FileSectionInfo;
import jhw.multifile.Resource.FileInfo;
import jhw.multifile.Resource.SourceFileList;

import java.util.ArrayList;
import java.util.List;

public interface IDistributionStrategy {
    List<List<FileSectionInfo>> distributionStrategy(SourceFileList sourceFileList
            , int sendCount);
}
