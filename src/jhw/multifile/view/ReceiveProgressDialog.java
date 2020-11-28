package jhw.multifile.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.RootPaneContainer;

import com.mec.util.AutomaticUnit;
import com.mec.util.IMecView;
import com.mec.util.TimeDate;

public abstract class ReceiveProgressDialog extends JDialog implements IMecView {
    private static final long serialVersionUID = -7786721631557848720L;
    public static final int WIDTH = 340;
    public static final int MIN_HEIGHT = 170;

    public static final String RECEIVE_FILE_TOTAL_COUNT = "���ι�����#���ļ�";

    private int receiveFileCount;
    private JLabel jlblReceiveFileCount;
    private JProgressBar jpgbReceiveFileCount;

    private JLabel jlblCurrentSpeed;
    private JLabel jlblAverageSpeed;
    private JPanel jpnlFiles;

    private Map<Integer, ReceiveFileProgress> receiveFilePool;

    public ReceiveProgressDialog(JFrame parent, int receiveFileCount) {
        super(parent, true);
        this.receiveFileCount = receiveFileCount;
        this.receiveFilePool = new HashMap<>();
        initView();
    }

    public void removeReceiveFile(int fileNo) {
        ReceiveFileProgress receiveFileProgress =
                this.receiveFilePool.get(fileNo);
        if (receiveFileProgress != null) {
            this.jpnlFiles.remove(receiveFileProgress);
            receiveFilePool.remove(fileNo);

            resizeDialog();
        }
    }

    public boolean receiveFileExist(int fileNo) {
        return this.receiveFilePool.containsKey(fileNo);
    }

    public void receiveFileSection(int fileNo, int receiveLen) {
        ReceiveFileProgress receiveFileProgress = this.receiveFilePool.get(fileNo);
        receiveFileProgress.receiveFileSection(receiveLen);
    }

    public void addReceiveFile(int fileNo, String fileName, long fileSize) {
        addReceiveFile(fileNo, fileName, 0L, fileSize);
    }

    public void addReceiveFile(int fileNo, String fileName, long receivedSize, long fileSize) {
        ReceiveFileProgress receiveFileProgress =
                new ReceiveFileProgress(jpnlFiles, fileName, receivedSize, fileSize);
        this.receiveFilePool.put(fileNo, receiveFileProgress);
        this.jpnlFiles.add(receiveFileProgress);
        resizeDialog();
    }

    private void resizeDialog() {
        int receiveFileProgressCount = this.receiveFilePool.size();
        int height = MIN_HEIGHT
                + receiveFileProgressCount * (ReceiveFileProgress.HEIGHT)
                + (receiveFileProgressCount > 1
                ? (receiveFileProgressCount - 1) * IMecView.PADDING * 3
                : 0);
        this.setSize(WIDTH, height);
        this.setLocationRelativeTo(this.getParent());
    }

    private String getFileCountString(int fileCount) {
        return RECEIVE_FILE_TOTAL_COUNT.replaceAll("#", String.valueOf(fileCount));
    }

    @Override
    public void reinit() {
    }

    public abstract void afterDialogShow();

    @Override
    public void dealEvent() {
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                afterDialogShow();
            }
        });
    }

    @Override
    public RootPaneContainer getFrame() {
        return this;
    }

    @Override
    public void init() {
        this.setSize(WIDTH, MIN_HEIGHT);
        this.setLocationRelativeTo(this.getParent());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        JLabel jlblEast = new JLabel(" ");
        jlblEast.setFont(smallFont);
        this.add(jlblEast, BorderLayout.EAST);

        JLabel jlblWest = new JLabel(" ");
        jlblWest.setFont(smallFont);
        this.add(jlblWest, BorderLayout.WEST);

        // ������ļ���������
        JPanel jpnlHead = new JPanel(new BorderLayout());
        this.add(jpnlHead, BorderLayout.NORTH);

        JLabel jlblHeadEast = new JLabel(" ");
        jlblHeadEast.setFont(smallFont);
        jpnlHead.add(jlblHeadEast, BorderLayout.EAST);

        JLabel jlblHeadWest = new JLabel(" ");
        jlblHeadWest.setFont(smallFont);
        jpnlHead.add(jlblHeadWest, BorderLayout.WEST);

        JLabel jlblTopic = new JLabel("�ļ����ս���", JLabel.CENTER);
        jlblTopic.setFont(topicFont);
        jlblTopic.setForeground(topicColor);
        jpnlHead.add(jlblTopic, BorderLayout.NORTH);

        JPanel jpnlFileCount = new JPanel(new GridLayout(0, 1));
        jpnlHead.add(jpnlFileCount, BorderLayout.CENTER);

        this.jlblReceiveFileCount = new JLabel(
                getFileCountString(this.receiveFileCount), JLabel.CENTER);
        this.jlblReceiveFileCount.setFont(normalFont);
        jpnlFileCount.add(this.jlblReceiveFileCount);

        this.jpgbReceiveFileCount = new JProgressBar(0, receiveFileCount);
        this.jpgbReceiveFileCount.setFont(normalFont);
        this.jpgbReceiveFileCount.setStringPainted(true);
        this.jpgbReceiveFileCount.setString("0 / " + this.receiveFileCount);
        jpnlFileCount.add(this.jpgbReceiveFileCount);

        // �ļ����ս���
        jpnlFiles = new JPanel();
        GridLayout gdltFiles = new GridLayout(0, 1);
        gdltFiles.setVgap(PADDING);
        jpnlFiles.setLayout(gdltFiles);
        this.add(jpnlFiles, BorderLayout.CENTER);

        // ���ټ��	
        JPanel jpnlFooter = new JPanel(new GridLayout(1, 2));
        this.add(jpnlFooter, BorderLayout.SOUTH);

        JPanel jpnlCurrentSpeed = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpnlFooter.add(jpnlCurrentSpeed);

        JLabel jlblCurrentSpeedTopic = new JLabel("��ǰ���� : ");
        jlblCurrentSpeedTopic.setFont(smallFont);
        jpnlCurrentSpeed.add(jlblCurrentSpeedTopic);

        this.jlblCurrentSpeed = new JLabel(
                AutomaticUnit.longToBytesUnit(0L));
        this.jlblCurrentSpeed.setFont(smallFont);
        jpnlCurrentSpeed.add(this.jlblCurrentSpeed);

        JPanel jpnlAverageSpeed = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpnlFooter.add(jpnlAverageSpeed);

        JLabel jlblAverageSpeedTopic = new JLabel("ƽ������ : ");
        jlblAverageSpeedTopic.setFont(smallFont);
        jpnlAverageSpeed.add(jlblAverageSpeedTopic);

        this.jlblAverageSpeed = new JLabel(
                AutomaticUnit.longToBytesUnit(0L));
        this.jlblAverageSpeed.setFont(smallFont);
        jpnlAverageSpeed.add(this.jlblAverageSpeed);
    }

}
