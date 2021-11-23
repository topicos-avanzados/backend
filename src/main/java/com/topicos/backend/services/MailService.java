package com.topicos.backend.services;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  private static final String MAIL = "deres.proyecto@gmail.com";

  private static final String PASSWORD = "Hola?1234";

  private final String host;

  public MailService(String host) {
    this.host = host;
  }

  public MailService() {
    this.host = "";
  }

  public void sendMailWithToken(String email, String token) throws MessagingException {

    Properties props = new Properties();
    props.setProperty("mail.smtp.host", this.host);
    props.setProperty("mail.smtp.starttls.enable", "true");
    props.setProperty("mail.smtp.port", "587");
    props.setProperty("mail.smtp.user", MAIL);
    props.setProperty("mail.smtp.auth", "true");

    Session session = Session.getDefaultInstance(props);
    session.setDebug(false);
    MimeMessage message = new MimeMessage(session);

    message.setFrom(new InternetAddress(MAIL));
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
    message.setSubject("Link para activar su usuario");
    message.setText("Siga el link de activación: " + token
        + "Recuerde que éste código es de corta duración y es solo válido para la operación solicitada");

    Transport t = session.getTransport("smtp");
    t.connect(MAIL, PASSWORD);
    t.sendMessage(message, message.getAllRecipients());
    t.close();
  }

}