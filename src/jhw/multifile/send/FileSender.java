package jhw.multifile.send;

import jhw.filetransfer.FileSectionInfo;
import jhw.filetransfer.readwrite.FileReadWrite;
import jhw.filetransfer.sendRec.FileSectionSendReceive;
import jhw.filetransfer.sendRec.NetSendReceive;
import jhw.multifile.receive.ResourceFilePool;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class FileSender implements Runnable {
    private Socket client;
    private DataOutputStream dos;
    private ResourceFilePool resFilePool;
    private List<FileSectionInfo> fileSectionInfos;
    private FileSectionSendReceive fileSectionSendReceive;

    public FileSender(String receiveIp, int receivePort
            , ResourceFilePool resFilePool, List<FileSectionInfo> fileSectionInfos) {
        fileSectionSendReceive = new FileSectionSendReceive();
        fileSectionSendReceive.setSendReceive(new NetSendReceive());
        try {
            this.client = new Socket(receiveIp, receivePort);
            this.dos = new DataOutputStream(client.getOutputStream());
            this.resFilePool = resFilePool;
            this.fileSectionInfos = fileSectionInfos;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSend() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int i = 0; i < fileSectionInfos.size(); i++) {
            try {
                FileSectionInfo fileSectionInfo = fileSectionInfos.get(i);
                int fileNo = fileSectionInfo.getFileNo();
                FileReadWrite fileRead = resFilePool.getFileReadWrite(fileNo);
                fileSectionInfo = fileRead.readFileSection(fileSectionInfo);
                fileSectionSendReceive.sendFileSection(dos, fileSectionInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            fileSectionSendReceive.sendLastFileSection(dos);
        } catch (Exception e) {
        }
    }
}
