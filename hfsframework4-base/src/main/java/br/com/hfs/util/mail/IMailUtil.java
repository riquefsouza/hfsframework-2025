package br.com.hfs.util.mail;

import org.springframework.mail.MailException;

import jakarta.mail.MessagingException;


public interface IMailUtil {

	public void sendSimpleMessage(String to, String subject, String text) throws MailException;

	public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment)
			throws MailException, MessagingException;

}
