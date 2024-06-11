package br.com.freire.uber.resource;

public interface MailerGateway {

    public void send (String recipient, String subject, String content);
}
