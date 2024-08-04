package edu.java.type;

import java.time.OffsetDateTime;

public class LogRecord {
    private String remoteAddr;
    private String remoteUser;
    private OffsetDateTime timeLocal;
    private String request;
    private int status;
    private long bodyBytesSent;
    private String httpReferer;
    private String httpUserAgent;
    private String fileName;

    @SuppressWarnings({"ParameterNumber"})
    public LogRecord(String remoteAddr, String remoteUser, OffsetDateTime timeLocal, String request,
        int status, long bodyBytesSent, String httpReferer, String httpUserAgent, String fileName) {
        this.remoteAddr = remoteAddr;
        this.remoteUser = remoteUser;
        this.timeLocal = timeLocal;
        this.request = request;
        this.status = status;
        this.bodyBytesSent = bodyBytesSent;
        this.httpReferer = httpReferer;
        this.httpUserAgent = httpUserAgent;
        this.fileName = fileName;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public OffsetDateTime getTimeLocal() {
        return timeLocal;
    }

    public String getRequest() {
        return request;
    }

    public int getStatus() {
        return status;
    }

    public long getBodyBytesSent() {
        return bodyBytesSent;
    }

    public String getHttpReferer() {
        return httpReferer;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRequestMethod() {
        // Assuming the request format is "GET /downloads/product_1 HTTP/1.1"
        String[] requestParts = request.split(" ");
        return requestParts.length > 0 ? requestParts[0] : "";
    }

    public String getRequestURI() {
        String[] requestParts = request.split(" ");
        return requestParts.length > 1 ? requestParts[1] : "";
    }

    public String getRequestProtocol() {
        String[] requestParts = request.split(" ");
        return requestParts.length > 2 ? requestParts[2] : "";
    }
}
