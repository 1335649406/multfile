package jhw.multifile.receive;

import com.mec.util.FrameIsNullException;
import jhw.filetransfer.FileSectionInfo;
import jhw.filetransfer.readwrite.FileReadWrite;
import jhw.filetransfer.readwrite.IFileReadWriteInterceptor;
import jhw.filetransfer.readwrite.ReadWriteInterceptorAdapter;
import jhw.filetransfer.sendRec.FileSectionSendReceive;
import jhw.filetransfer.sendRec.NetSendReceive;
import jhw.multifile.Resource.FileInfo;
import jhw.multifile.Resource.SourceFileList;
import jhw.multifile.unreceive.UnreceiveSection;
import jhw.multifile.view.ReceiveProgressDialog;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveServer implements Runnable {
    private ServerSocket server;
    private int port;
    private volatile boolean startup;
    private int receiveCount;

    private SourceFileList sourceFileList;
    private ResourceFilePool resFilePool;
    private FileSectionSendReceive fileSectionReceive;
    private FileReadWrite fileWrite;

    private IFileReadWriteInterceptor fileReadWriteInterceptor;
    private ReceiveProgressDialog receiveProgressDialog;

    public ReceiveServer() {
        this.startup = false;
        this.fileSectionReceive = new FileSectionSendReceive();
        this.fileWrite = new FileReadWrite();
        this.resFilePool = new ResourceFilePool();
        this.fileReadWriteInterceptor = new FileSectionReadWriteInterceptor();
        this.resFilePool.setFileReadWriteInterceptor(fileReadWriteInterceptor);
    }

    public void enableReceiveProgressDialog(JFrame parent, int receiveCount) {
        if (this.receiveProgressDialog != null) {
            return;
        }
        this.receiveProgressDialog = new ReceiveProgressDialog(parent, receiveCount) {
            @Override
            public void afterDialogShow() {
                new Thread(ReceiveServer.this).start();
            }
        };
        ((FileSectionReadWriteInterceptor) fileReadWriteInterceptor)
                .setReceiveProgressDialog(receiveProgressDialog);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSourceFileList(SourceFileList sourceFileList) {
        this.sourceFileList = sourceFileList;
        resFilePool.addFileInfoList(sourceFileList);
    }

    public void setResFilePool(ResourceFilePool resFilePool) {
        this.resFilePool = resFilePool;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public void startup() {
        if (startup) {
            return;
        }

        if (this.receiveProgressDialog != null) {
            new Thread(new ReceiveProgressShower(this.receiveProgressDialog)).start();
        }
        try {
            this.startup = true;
            this.server = new ServerSocket(port);
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        startup = false;
    }

    @Override
    public void run() {
        if (receiveCount <= 0) {
            System.out.println("senderCount:" + receiveCount);
            return;
        }
        for (int i = 0; i < receiveCount; i++) {
            try {
                Socket sender = server.accept();
                Receiver receiver = new Receiver(sender);
                new Thread(receiver).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Receiver implements Runnable {
        private Socket socket;
        private DataInputStream dis;
        private FileSectionSendReceive fileSectionSendReceive;
        private FileReadWrite fileReadWrite;

        public Receiver(Socket socket) throws IOException {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.fileSectionSendReceive = new FileSectionSendReceive();
            this.fileSectionSendReceive.setSendReceive(new NetSendReceive());
        }

        @Override
        public void run() {
            try {
                FileSectionInfo fileSectionInfo = fileSectionSendReceive.receiveFileSection(dis);
                while (fileSectionInfo.getLen() > 0) {
                    fileReadWrite = resFilePool.getFileReadWrite(fileSectionInfo.getFileNo());
                    fileReadWrite.writeFileSection(fileSectionInfo);

                    fileSectionInfo = fileSectionSendReceive.receiveFileSection(dis);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ReceiveProgressShower implements Runnable {
        private ReceiveProgressDialog receiveProgressDialog;

        public ReceiveProgressShower(ReceiveProgressDialog receiveProgressDialog) {
            this.receiveProgressDialog = receiveProgressDialog;
        }

        @Override
        public void run() {
            try {
                receiveProgressDialog.showView();
            } catch (FrameIsNullException e) {
                e.printStackTrace();
            }
        }
    }

    class FileSectionReadWriteInterceptor extends ReadWriteInterceptorAdapter {
        ReceiveProgressDialog receiveProgressDialog;

        public FileSectionReadWriteInterceptor() {
        }

        public void setReceiveProgressDialog(ReceiveProgressDialog receiveProgressDialog) {
            this.receiveProgressDialog = receiveProgressDialog;
        }

        @Override
        public void beforeWrite(String filePath, FileSectionInfo fileSectionInfo) {
            if (receiveProgressDialog == null) {
                return;
            }
            int fileNo = fileSectionInfo.getFileNo();
            if (!receiveProgressDialog.receiveFileExist(fileNo)) {
                try {
                    FileInfo fileInfo = sourceFileList.getFileInfoByNo(fileNo);
                    receiveProgressDialog.addReceiveFile(fileNo, filePath, fileInfo.getFileLen());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void afterWritten(FileSectionInfo fileSectionInfo) {
            long offset = fileSectionInfo.getOffset();
            int len = fileSectionInfo.getLen();
            int fileNo = fileSectionInfo.getFileNo();
            UnreceiveSection unreceiveSection = resFilePool.getUnreceiveSection(fileNo);
            try {
                unreceiveSection.receiveSection(offset, len);
                if (unreceiveSection.isReceived()) {
                    FileReadWrite fileReadWrite = resFilePool.getFileReadWrite(fileNo);
                    fileReadWrite.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (receiveProgressDialog != null) {
                receiveProgressDialog.receiveFileSection(fileNo, len);
                if (unreceiveSection.isReceived()) {
                    receiveProgressDialog.removeReceiveFile(fileNo);
                }
            }
        }
    }

}
