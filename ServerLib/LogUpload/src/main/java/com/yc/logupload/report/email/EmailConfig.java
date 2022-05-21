package com.yc.logupload.report.email;


import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 配置文件
 *     revise :
 * </pre>
 */
public final class EmailConfig {
    
    private String mReceiveEmail;
    private String mSendEmail;
    private String mSendPassword;
    private String mHost;
    private String mPort;

    public String getReceiveEmail() {
        return mReceiveEmail;
    }

    public String getSendEmail() {
        return mSendEmail;
    }

    public String getSendPassword() {
        return mSendPassword;
    }

    public String getHost() {
        return mHost;
    }

    public String getPort() {
        return mPort;
    }

    private EmailConfig(Builder builder) {
        mReceiveEmail = builder.receiveEmail;
        mSendEmail = builder.sendEmail;
        mSendPassword = builder.sendPassword;
        mHost = builder.host;
        mPort = builder.port;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private String receiveEmail;
        private String sendEmail;
        private String sendPassword;
        private String host;
        private String port;


        public Builder() {

        }

        public Builder receiveEmail(String receiveEmail) {
            this.receiveEmail = receiveEmail;
            return this;
        }

        public Builder sendEmail(String sendEmail) {
            this.sendEmail = sendEmail;
            return this;
        }

        public Builder sendPassword(String sendPassword) {
            this.sendPassword = sendPassword;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(String port) {
            this.port = port;
            return this;
        }

        public EmailConfig build() {
            return new EmailConfig(this);
        }
    }
}
