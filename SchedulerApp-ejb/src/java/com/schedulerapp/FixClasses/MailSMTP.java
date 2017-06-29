/*
 * Copyright (C) 2017 Fekete András Demeter
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.schedulerapp.FixClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 *
 * @author Fekete András Demeter 
 */

public class MailSMTP {
    
   private final boolean debug = false;

    /**
     *
     * @param jobid job id
     * @param v_to To
     * @param v_cc CC
     * @param v_bcc BCC
     * @param v_subject mail subject 
     * @param filefullpath file attachment full path
     * @param ripurl report url path if upload example  to Share
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws MessagingException MessagingException
     * @throws java.io.FileNotFoundException
     */
    public void mailSMTP(String jobid,String v_to,String v_cc,String v_bcc, String v_subject,String filefullpath, String ripurl) throws UnsupportedEncodingException, MessagingException, FileNotFoundException, IOException, Exception {
        
         Properties prop=new Properties();
         prop.put("mail.smtp.auth", "false");
         prop.put("mail.smtp.host", "mail.host.com");
         prop.put("mail.smtp.ssl.trust", "mail.host.com");
         prop.put("mail.smtp.starttls.enable", "true");

         Session session = Session.getDefaultInstance(prop);
         session.setDebug(debug);

         Calendar now = Calendar.getInstance();
         int year = now.get(Calendar.YEAR);
         String yearInString = String.valueOf(year);  
         
         DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
         Date today = Calendar.getInstance().getTime();        
         String reportDate = df.format(today);
         ripurl = ripurl.replace("//share.host.intra@SSL/DavWWWRoot" , "https://share.host.intra");
         
         String mail_image ="'http://hostip:8080/jpg/charts-graphs-cholesterol.png'";
         String messripmail;
         String buttonn;       
         
         if (!filefullpath.equals("-")){
               messripmail = "A tárgyi riportot a csatolt elemek között találjátok.<br>";
               buttonn = "";
               
         } else {
               messripmail = "A tárgyi riportot az alábbi linken éritek el.<br>";   
               
               buttonn="<tr>\n" +                       
                       "<td data-bgcolor=\"bg-button\" data-size=\"size button\" data-min=\"10\" data-max=\"16\" class=\"btn\" align=\"center\" style=\"font:12px/14px Arial, Helvetica, sans-serif; color:#E20074; text-transform:uppercase; mso-padding-alt:12px 10px 10px; border-radius:2px;\" bgcolor=\"#E20074\">\n" +
                       "<a target=\"_blank\" style=\"text-decoration:none; color:#fffafd; display:block; padding:12px 10px 10px;\" href=\""+ripurl+"\">Riport megtekintése</a>\n" +
                       "</td>\n" +
                       "</tr>\n" ;
         }
         
         String messagehtml ="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                                "<head>\n" +
                                "<title>Internal_email-29</title>\n" +
                                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                                "<style type=\"text/css\">\n" +
                                "* {\n" +
                                "-ms-text-size-adjust:100%;\n" +
                                "-webkit-text-size-adjust:none;\n" +
                                "-webkit-text-resize:100%;\n" +
                                "text-resize:100%;\n" +
                                "}\n" +
                                "a{\n" +
                                "outline:none;\n" +
                                "color:#40aceb;\n" +
                                "text-decoration:underline;\n" +
                                "}\n" +
                                "a:hover{text-decoration:none !important;}\n" +
                                ".nav a:hover{text-decoration:underline !important;}\n" +
                                ".title a:hover{text-decoration:underline !important;}\n" +
                                ".title-2 a:hover{text-decoration:underline !important;}\n" +
                                ".btn:hover{opacity:0.8;}\n" +
                                ".btn a:hover{text-decoration:none !important;}\n" +
                                ".btn{\n" +
                                "-webkit-transition:all 0.3s ease;\n" +
                                "-moz-transition:all 0.3s ease;\n" +
                                "-ms-transition:all 0.3s ease;\n" +
                                "transition:all 0.3s ease;\n" +
                                "}\n" +
                                "table td {border-collapse: collapse !important;}\n" +
                                ".ExternalClass, .ExternalClass a, .ExternalClass span, .ExternalClass b, .ExternalClass br, .ExternalClass p, .ExternalClass div{line-height:inherit;}\n" +
                                "@media only screen and (max-width:500px) {\n" +
                                "table[class=\"flexible\"]{width:100% !important;}\n" +
                                "table[class=\"center\"]{\n" +
                                "float:none !important;\n" +
                                "margin:0 auto !important;\n" +
                                "}\n" +
                                "*[class=\"hide\"]{\n" +
                                "display:none !important;\n" +
                                "width:0 !important;\n" +
                                "height:0 !important;\n" +
                                "padding:0 !important;\n" +
                                "font-size:0 !important;\n" +
                                "line-height:0 !important;\n" +
                                "}\n" +
                                "td[class=\"img-flex\"] img{\n" +
                                "width:100% !important;\n" +
                                "height:auto !important;\n" +
                                "}\n" +
                                "td[class=\"aligncenter\"]{text-align:center !important;}\n" +
                                "th[class=\"flex\"]{\n" +
                                "display:block !important;\n" +
                                "width:100% !important;\n" +
                                "}\n" +
                                "td[class=\"wrapper\"]{padding:0 !important;}\n" +
                                "td[class=\"holder\"]{padding:30px 15px 20px !important;}\n" +
                                "td[class=\"nav\"]{\n" +
                                "padding:20px 0 0 !important;\n" +
                                "text-align:center !important;\n" +
                                "}\n" +
                                "td[class=\"h-auto\"]{height:auto !important;}\n" +
                                "td[class=\"description\"]{padding:30px 20px !important;}\n" +
                                "td[class=\"i-120\"] img{\n" +
                                "width:120px !important;\n" +
                                "height:auto !important;\n" +
                                "}\n" +
                                "td[class=\"footer\"]{padding:5px 20px 20px !important;}\n" +
                                "td[class=\"footer\"] td[class=\"aligncenter\"]{\n" +
                                "line-height:25px !important;\n" +
                                "padding:20px 0 0 !important;\n" +
                                "}\n" +
                                "tr[class=\"table-holder\"]{\n" +
                                "display:table !important;\n" +
                                "width:100% !important;\n" +
                                "}\n" +
                                "th[class=\"thead\"]{display:table-header-group !important; width:100% !important;}\n" +
                                "th[class=\"tfoot\"]{display:table-footer-group !important; width:100% !important;}\n" +
                                "}\n" +
                                "</style>\n" +
                                "</head>\n" +
                                "<body style=\"margin:0; padding:0;\" bgcolor=\"#eaeced\">\n" +
                                "<table style=\"min-width:320px;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#eaeced\">\n" +
                                "<tr>\n" +
                                "<td class=\"hide\">\n" +
                                "<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px !important;\">\n" +
                                "<tr>\n" +
                                "<td style=\"min-width:600px; font-size:0; line-height:0;\">&nbsp;</td>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                "<td class=\"wrapper\" style=\"padding:0 10px;\">\n" +
                                "<!-- module 1 -->\n" +
                                "<table data-module=\"module-1\" data-thumb=\"thumbnails/01.png\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\n" +
                                "<table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<td style=\"padding:29px 0 30px;\">\n" +
                                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<th class=\"flex\" width=\"113\" align=\"left\" style=\"padding:0;\">\n" +
                                "<table class=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<td style=\"line-height:0;\">\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "</th>\n" +
                                "<th class=\"flex\" align=\"left\" style=\"padding:0;\">\n" +
                                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<td data-color=\"text\" data-size=\"size navigation\" data-min=\"10\" data-max=\"22\" data-link-style=\"text-decoration:none; color:#888;\" class=\"nav\" align=\"right\" style=\"font:bold 16px/19px Arial, Helvetica, sans-serif; color:#888;\">\n" +
                                "<a target=\"_blank\" style=\"text-decoration:none; color:#888;\" href=\"mailto:reporting@host.com?Subject=jobid:"+jobid+" subject:"+v_subject+" mail_received:"+reportDate+"&body=Message%20%3A%0D%0A\">Reply email</a>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "</th>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "<!-- module 2 -->\n" +
                                "<table data-module=\"module-2\" data-thumb=\"thumbnails/02.png\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\n" +
                                "<table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<td class=\"img-flex\"><img src="+mail_image+" style=\"vertical-align:top;\" width=\"600\" height=\"306\" alt=\"\" /></td>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                "<td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:58px 60px 52px;\" bgcolor=\"#f9f9f9\">\n" +
                                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                "<tr>\n" +
                                "<td data-color=\"title\" data-size=\"size title\" data-min=\"25\" data-max=\"45\" data-link-color=\"link title color\" data-link-style=\"text-decoration:none; color:#292c34;\" class=\"title\" align=\"left\" style=\"font:18px/28px Arial, Helvetica, sans-serif; color:#292c34; padding:0 0 24px;\">\n" +
                                
                                            "Kedves Kollégák" +
                                
                                "\n<br></br>"+
                                "</td>\n" +
                                "</tr>\n" + 
                                "<tr>\n" +
                                "<td data-color=\"title\" data-size=\"size title\" data-min=\"25\" data-max=\"45\" data-link-color=\"link title color\" data-link-style=\"text-decoration:none; color:#292c34;\" class=\"title\" align=\"left\" style=\"font:16px/26px Arial, Helvetica, sans-serif; color:#292c34; padding:0 0 24px;\">\n" +
                                
                                            messripmail
                 
                                            + " (Kérdés esetén, kérlek a jobb felső sarokban lévő válasz email gombot használjátok.)"  +
                                
                 
                                "\n</td>\n" +
                                "</tr>\n" +            
                                "<td style=\"padding:0 0 20px;\">\n" +
                                "<table width=\"134\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\n" +                     
                                "</tr>\n" +
                                
                                            buttonn  +

                 
                                "</table>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                "<td data-color=\"text\" data-size=\"size text\" data-min=\"10\" data-max=\"26\" data-link-color=\"link text color\" data-link-style=\"font-weight:bold; text-decoration:underline; color:#40aceb;\" align=\"left\" style=\"font:bold 14px/20px Arial, Helvetica, sans-serif; color:#888; padding:0 0 23px;\">\n" +
                                
                                            "Üdvözlettel,"               + "<br>\n" +
                                            "TEAM Name"     +  
                                "</td>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                "<tr style='mso-yfti-irow:2;height:.85pt'>\n" +
                                "<td style='border:none;padding:1.0pt 15.0pt 1.0pt 15.0pt;height:.85pt'>\n" +
                                "<div class=MsoNormal align=center style='margin-bottom:0cm;margin-bottom:\n" +
                                ".0001pt;text-align:center;line-height:normal'><span style='font-size:12.0pt;\n" +
                                "font-family:'Times New Roman',serif;mso-fareast-font-family:'Times New Roman''>\n" +
                                "<hr size=1 width=100% style='width:100%' align=center>\n" +
                                "</span></div>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "<tr style='mso-yfti-irow:3;height:3.7pt'>\n" +
                                "<td style='border:none;padding:0cm 15.75pt 0cm 15.75pt;height:3.7pt'>\n" +
                                "<p class=MsoNormal style='margin-bottom:0cm;margin-bottom:.0001pt;line-height:\n" +
                                "normal'><span style='font-size:9.0pt;font-family:Tele-GroteskEENor'>"+yearInString+"\n" +
                                "Company<o:p></o:p></span></p>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "<tr style='mso-yfti-irow:4'>\n" +
                                "<td style='border:none;padding:1.0pt 15.0pt 1.0pt 15.0pt'>\n" +
                                "<div class=MsoNormal align=center style='margin-bottom:0cm;margin-bottom:\n" +
                                ".0001pt;text-align:center'><span style='font-size:12.0pt;line-height:115%;\n" +
                                "font-family:'Times New Roman',serif;mso-fareast-font-family:'Times New Roman''>\n" +
                                "<hr size=1 width=100% style='width:100%' align=center>\n" +
                                "</span></div>\n" +
                                "</td>\n" +        
                                "</tr>\n" +
                                "<tr style='mso-yfti-irow:5;mso-yfti-lastrow:yes;height:15.75pt'>\n" +
                                "<td style='border:none;padding:0cm 0cm 0cm 0cm;height:15.75pt'></td>\n" +
                                "</table>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "<tr><td height=\"28\"></td></tr>\n" +
                                "</table>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "<tr>\n" +
                                "<p class=MsoNormal style='margin-bottom:0cm;margin-bottom:.0001pt;line-height:\n" +
                                "normal'><span style='font-size:12.0pt;font-family:'Times New Roman',serif'><o:p></o:p></span></p>\n" +
                                "</td>\n" +
                                "</tr>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "</div>\n" +
                                "<p class=MsoNormal><span style='font-family:'Times New Roman',serif'><o:p>&nbsp;</o:p></span></p>" +                      
                                "<td style=\"line-height:0;\"><div style=\"display:none; white-space:nowrap; font:15px/1px courier;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div></td>\n" +
                                "</tr>\n" +
                                "</table>\n" +
                                "</body>\n" +
                                "</html>";
        

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress("Reporting_noreply@noreply.host.hu","Reporting (noreply)"));
        
        if (!v_to.equals("-")) {
            String toaddress = v_to;
            InternetAddress[] toiAdressArray = InternetAddress.parse(toaddress.replaceAll(";", ","));
            message.setRecipients(Message.RecipientType.TO, toiAdressArray);
        }
        
        if (!v_cc.equals("-")) {
            String ccaddress = v_cc;
            InternetAddress[] cciAdressArray = InternetAddress.parse(ccaddress.replaceAll(";", ","));
            message.setRecipients(Message.RecipientType.CC, cciAdressArray);
        }
        
        if (!v_bcc.equals("-")) {
            String bccaddress = v_bcc;
            InternetAddress[] bcciAdressArray = InternetAddress.parse(bccaddress.replaceAll(";", ","));
            message.setRecipients(Message.RecipientType.BCC, bcciAdressArray);
        }
        
        message.setSubject(v_subject);
        
        if (!filefullpath.equals("-")){
            
      try{
                
                File fille = new File(filefullpath);
                Multipart multipart = new MimeMultipart(); // creating a multipart is OK
                BodyPart htmlBodyPart = new MimeBodyPart(); //4
                htmlBodyPart.setContent(messagehtml ,"text/html; charset=UTF-8"); //5
                multipart.addBodyPart(htmlBodyPart); // 6
                // Creating the first body part of the multipart, it's OK
                DataSource source = null;
                
                if ((int)(fille.length()/1024/1024) > 10) {
            
                    FileInputStream fis = new FileInputStream(filefullpath);
                    FileOutputStream fos = new FileOutputStream(filefullpath+".gz");
                    GZIPOutputStream gos = new GZIPOutputStream(fos);

                    doCopy(fis, gos); // copy and compress
                    
                    fille = new File(filefullpath+".gz");
                    source = new FileDataSource(filefullpath+".gz");
                    
                } else {
                    
                    fille = new File(filefullpath);
                    source = new FileDataSource(filefullpath);
                }
                
                if ((int)(fille.length()/1024/1024) > 15) {
                    message.setSubject("Attachment send fail! "+v_subject);
                    message.setContent(multipart);
                    Transport.send(message);
                    System.out.println("Sent message successfully....without attachment, its over 15 mb");
                    
                } else {
                    
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(fille.getName());
                    multipart.addBodyPart(messageBodyPart);
                    message.setContent(multipart);
                    Transport.send(message);
                    System.out.println("Sent message successfully....with attachment");
                    
                }
                
            } catch (MessagingException mex) {
                
                mex.getMessage();
            
            }
            
        } else {
            
            try{
                Multipart multipart = new MimeMultipart(); // creating a multipart is OK
                MimeBodyPart htmlBodyPart = new MimeBodyPart(); //4
                htmlBodyPart.setContent(messagehtml ,"text/html; charset=UTF-8"); //5
                multipart.addBodyPart(htmlBodyPart); // 6
                message.setContent(multipart);
                Transport.send(message);
                System.out.println("Sent message successfully....without attachment");
            }catch (MessagingException mex) {
                mex.getMessage();
            }
            
        }

   }

   public static void doCopy(InputStream is, OutputStream os) throws Exception {
		int oneByte;
		while ((oneByte = is.read()) != -1) {
			os.write(oneByte);
		}
		os.close();
		is.close();

   }
}
