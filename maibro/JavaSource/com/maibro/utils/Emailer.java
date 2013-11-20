package com.maibro.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Class untuk sending email
 * 
 * Penjelasan untuk @Async = http://www.skill-guru.com/blog/2010/01/13/asynchronous-method-invocation-in-spring-3-0/
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:08:05 AM)
 *
 */
@Component //@Component berarti otomatis register sebagai bean, tanpa perlu didefinisikan di spring xml
public class Emailer {

	private static Logger logger = Logger.getLogger(Emailer.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Async //@Async method akan dijalankan secara asinkron, dalam thread berbeda (user tidak perlu menunggu send email selesai)
	public void send(String from, String[] to, String subject, String message) throws MessagingException {
		logger.info("----- SENDING EMAIL -----");

		//delay 5 detik, iseng aja
		//try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

		//kirim email
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		//
		helper.setFrom(from);
		helper.setReplyTo(from);
		helper.setTo(to);
		//
		helper.setSubject("[MaiBro] " + subject);
		helper.setText(message);
		//
		try{
			mailSender.send(helper.getMimeMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("----- SENDING EMAIL COMPLETED -----");
	}
	
	@Async //@Async method akan dijalankan secara asinkron, dalam thread berbeda (user tidak perlu menunggu send email selesai)
	public void send(String from, String[] to, String[] cc, String subject, String message) throws MessagingException {
		logger.info("----- SENDING EMAIL -----");

		//delay 5 detik, iseng aja
		//try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

		//kirim email
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		//
		helper.setFrom(from);
		helper.setReplyTo(from);
		helper.setTo(to);
		helper.setCc(cc);
		//
		helper.setSubject("[MaiBro] " + subject);
		helper.setText(message);
		//
		try{
			mailSender.send(helper.getMimeMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("----- SENDING EMAIL COMPLETED -----");
	}
	
	@Async //@Async method akan dijalankan secara asinkron, dalam thread berbeda (user tidak perlu menunggu send email selesai)
	public void send(String from, String[] to, String[] cc, String[] bcc, String subject, String message) throws MessagingException {
		logger.info("----- SENDING EMAIL -----");

		//delay 5 detik, iseng aja
		//try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

		//kirim email
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		//
		helper.setFrom(from);
		helper.setReplyTo(from);
		helper.setTo(to);
		helper.setCc(cc);
		helper.setBcc(bcc);
		//
		helper.setSubject("[MaiBro] " + subject);
		helper.setText(message);
		//
		try{
			mailSender.send(helper.getMimeMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("----- SENDING EMAIL COMPLETED -----");
	}
	
}