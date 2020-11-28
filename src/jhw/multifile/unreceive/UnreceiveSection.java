package jhw.multifile.unreceive;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: wjh
 * @DateTime: 2020/8/1 9:56
 * @Description: һ���ļ���Ӧ��δ���ܵ���Ƭ����Ϣ
 */
public class UnreceiveSection {
    private int fileNo;
    private List<SectionInfo> sectionList;

    public UnreceiveSection(int fileNo, long len) {
        this.fileNo = fileNo;
        this.sectionList = new LinkedList<SectionInfo>();
        this.sectionList.add(new SectionInfo(0, len));
    }

    public UnreceiveSection(int fileNo) {
        this.fileNo = fileNo;
        this.sectionList = new LinkedList<>();
    }

    public void addUnSectionInfo(SectionInfo sectionInfo) {
        this.sectionList.add(sectionInfo);
    }

    public int searchSection(long rec) throws Exception {
        for (int i = 0; i < sectionList.size(); i++) {
            SectionInfo sectionInfo1 = sectionList.get(i);
            if (sectionInfo1.isRange(rec)) {
                return i;
            }
        }
        throw new Exception("�ļ��ã�" + fileNo + "ƫ������" + rec);
    }

    public void receiveSection(long recOff, long recLen)
            throws Exception {
        // TODO Ϊ�˽�������ն��쳣��������޷���ȡδ����Ƭ����Ϣ��
        // ���⣬��Ҫ�����ﴦ��������Ƭ�α��浽��档
        // ����log4J����־����ʵ�������뷨��

        int index = searchSection(recOff);
        SectionInfo curSection = sectionList.get(index);

        long curOff = curSection.getOffset();
        long curLen = curSection.getLen();

        long lOff = curOff;
        long lLen = recOff - curOff;
        long rOff = recOff + recLen;
        long rLen = curOff + curLen - rOff;

        sectionList.remove(index);
        if (rLen > 0) {
            sectionList.add(index, new SectionInfo(rOff, rLen));
        }
        if (lLen > 0) {
            sectionList.add(index, new SectionInfo(lOff, lLen));
        }
    }

    public boolean isReceived() {
        return sectionList.isEmpty();
    }

    public List<SectionInfo> getSectionList() {
        return sectionList;
    }
}
