package jhw.multifile.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.mec.util.IMecView;

public class ReceiveFileProgress extends JPanel {
    private static final long serialVersionUID = 1017850420343075206L;
    public static final int HEIGHT = 30;

    private long receivedLen;
    private long fileSize;

    private JProgressBar jpgbReceiveFile;

    public ReceiveFileProgress(JPanel jpnlParent, String fileName, long fileSize) {
        this(jpnlParent, fileName, 0L, fileSize);
    }

    public ReceiveFileProgress(JPanel jpnlParent, String fileName, long receivedLen, long fileSize) {
        this.fileSize = fileSize;
        this.receivedLen = receivedLen;
        this.setLayout(new GridLayout(0, 1));
        int parentWidth = jpnlParent.getWidth();
        this.setSize(parentWidth, HEIGHT);

        JLabel jlblFileName = new JLabel(fileName, JLabel.CENTER);
        jlblFileName.setFont(IMecView.normalFont);
        this.add(jlblFileName);

        this.jpgbReceiveFile = new JProgressBar();
        this.jpgbReceiveFile.setFont(IMecView.normalFont);
        this.jpgbReceiveFile.setStringPainted(true);
        this.jpgbReceiveFile.setMaximum((int) this.fileSize);
        this.jpgbReceiveFile.setValue((int) this.receivedLen);
        this.add(this.jpgbReceiveFile);
    }

    public void receiveFileSection(int receiveLen) {
        this.receivedLen += receivedLen;
        this.jpgbReceiveFile.setValue((int) this.receivedLen);
    }

}
