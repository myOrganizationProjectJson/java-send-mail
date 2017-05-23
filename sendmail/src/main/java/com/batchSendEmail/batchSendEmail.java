package com.batchSendEmail;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.util.HibernateUtil;
import com.properties.config;
import com.sendmail.mailInfo;
import com.sendmail.mailUser;
import com.sendmail.send;
import com.tools.ReadFromFile;

public class batchSendEmail {
	
    public void batchSendEmailStart(){
		Session session = HibernateUtil.currentSession();
		String hql = "SELECT id,email,username,status FROM batchEmailUser WHERE status !=2 AND status !=0 OR status = NULL ";
		List list  = session.createQuery(hql).list();
	    Iterator it1 = list.iterator();
	    String updateSql;
        while(it1.hasNext()){
        	Object[]  objects =(Object[]) it1.next();
    		mailUser mailuser=new mailUser();
    		mailuser.setUserEmail(objects[1].toString());
    		mailuser.setUsername(objects[2] != null ?objects[2].toString():"");
    		if(sendEmail(mailuser) != ""){
	    	    Transaction tx = session.beginTransaction();
	    		updateSql = "UPDATE batchEmailUser SET status = 2 ,time="+ System.currentTimeMillis()+" WHERE id ="+objects[0].toString() ;
				session.createQuery(updateSql).executeUpdate();
				tx.commit();   
    		}
    	     //间歇
    		if(Integer.parseInt(config.getConfig("sendMail_sendsleep") )>0 ){
	    		try {
					Thread.sleep(Integer.parseInt(config.getConfig("sendMail_sendsleep")));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
        }
    }
    
    public String sendEmail(mailUser mailuser){
		send send =new send();
		mailInfo mailinfo=new mailInfo();
		mailinfo.setSendusername(config.getConfig("sendMail_Sendusername"));
		mailinfo.setSendMail(config.getConfig("sendMail_SendMail"));
	    mailinfo.setTitle(config.getConfig("sendMail_Title"));
		mailinfo.setSubject(config.getConfig("sendMail_Subject"));
		String content=ReadFromFile.readFileByLines(config.getConfig("sendMail_emailContentFileName"));
		mailinfo.setContent(content);
		send.mailInfo=mailinfo;
		send.mailUser=mailuser;
		return send.send();
    }
    
    
    public void run(){
    	mailUser mailuser=new mailUser();
    	if(config.getConfig("sendMail_testMail").equals("1")){
    	 	mailuser.setUsername(config.getConfig("sendMail_testMailUsername"));
        	mailuser.setUserEmail(config.getConfig("sendMail_testMailEmail"));
        	this.sendEmail(mailuser);
    	}else{
    		this.batchSendEmailStart();
    	}
    }
    
    
    public static void main(String[] args) {
    	batchSendEmail batchSendEmail = new batchSendEmail();
    	batchSendEmail.run();
    }

}
