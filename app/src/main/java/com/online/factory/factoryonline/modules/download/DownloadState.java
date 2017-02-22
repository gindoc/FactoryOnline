package com.online.factory.factoryonline.modules.download;

/**
 * 作者: GIndoc
 * 日期: 2017/2/22 10:43
 * 作用:
 */

public class DownloadState {
    private long bytesRead;
    private long contentLength;
    private boolean isDone;

    public DownloadState() {
    }

    public DownloadState(long bytesRead, long contentLength, boolean isDone) {
        this.bytesRead = bytesRead;
        this.contentLength = contentLength;
        this.isDone = isDone;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
