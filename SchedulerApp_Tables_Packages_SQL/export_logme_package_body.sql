create or replace PACKAGE BODY logme_package
IS


PROCEDURE mail_send
     (v_from IN VARCHAR2,
      v_to IN VARCHAR2,
      v_cc IN VARCHAR2,
      v_bcc IN VARCHAR2,
      v_subject IN VARCHAR2,
      v_message IN clob
      )
    IS
   BEGIN 
   
   UTL_MAIL.SEND@REP_2 (
      sender => v_from,
      recipients => v_to,
      cc => v_cc,
      bcc => v_bcc,
      subject => v_subject,
      MIME_TYPE  => 'text/html; charset = Windows-1250',
      message => v_message
        );
	 commit;
   null;
   
   END;


  PROCEDURE log_time_send
     ( v_logid IN VARCHAR2,
       v_runtime OUT VARCHAR2)     
  IS
    var3 number :=0 ;
  BEGIN
      select count(1) as t into var3 
      from (
      select to_char(JOB_END,'YYYY.MM.DD HH24:Mi:SS') as last_end_date  from (
      select 
      JOB_END,
      ROW_NUMBER() OVER (PARTITION BY JOB_NO ORDER BY JOB_END DESC) RN
      from logme 
      where JOB_NO=v_logid and JOB_PROGRESS_PERCENT =100 and JOB_STATUS='CLOSED' )
      where RN=1);
    if 
      var3 = 0
    then
        v_runtime:='null'; 
        commit;
        null;
    else
        select to_char(JOB_END,'YYYY.MM.DD HH24:Mi:SS') as last_end_date into v_runtime from (
        select 
        JOB_END,
        ROW_NUMBER() OVER (PARTITION BY JOB_NO ORDER BY JOB_END DESC) RN
        from logme 
        where JOB_NO=v_logid and JOB_PROGRESS_PERCENT =100 and JOB_STATUS='CLOSED' )
        where RN=1;
        commit;
        null;
    end if;
    NULL;
  END log_time_send;


   PROCEDURE new_process
	 ( v_id OUT NUMBER,
       v_job_no IN NUMBER,
       job_name IN VARCHAR2(50 BYTE),
       job_info IN VARCHAR2(1000 BYTE) )
      
    IS
		avg_time number:=NULL;
   BEGIN
   
     insert into logme(id,job_id,job_sub_id,job_start,job_no,job_name,job_status,job_info)
     values(0,0,NULL,sysdate,v_job_no,job_name,'STARTED',job_info) returning id into v_id;
     commit;
     select round(avg((24*60*60*(a.JOB_END-a.JOB_START))),0) 
     into avg_time from logme a where a.JOB_NO=v_job_no and a.JOB_START>=sysdate-10
      and a.id!=v_id and a.JOB_SUB_ID is null;
     update logme a set a.job_id=v_id, a.JOB_AVG_RUNTIME=avg_time where a.id=v_id;
     commit;
     
   END;

   PROCEDURE close_process
	 ( v_id IN NUMBER,
       v_job_info IN VARCHAR2(1000 BYTE),
       v_error VARCHAR2(512 BYTE) default '' )
      
    IS
	 job_percent number:=NULL;
   BEGIN 
	 IF v_error is null THEN
		update logme a set a.job_end=sysdate, a.job_status='CLOSED',
				a.job_NEXTerror=v_error,a.job_progress_percent=100,
				a.job_info=v_job_info where a.id=v_id;
	 ELSE
		select avg(nvl(JOB_PROGRESS_PERCENT,0)) into job_percent from logme a 
				where a.job_id=v_id and a.JOB_SUB_ID is not null;
	    update logme a set a.job_end=sysdate, a.job_status='ERROR',
	    		a.job_NEXTerror=v_error, a.job_info=v_job_info where a.id=v_id; 
	    update logme a set a.job_status='ERROR' 
	    		where a.job_id=v_id and a.job_status='PROGRESS' and  JOB_SUB_ID is not null;
	  	update logme a set a.job_progress_percent=job_percent 
	  			where a.id=v_id and a.job_progress_percent is null;

	 END IF;
	 commit;
   END;

   PROCEDURE new_subprocess
     ( v_job_id IN NUMBER,
       job_sub_id IN NUMBER,
       job_info IN VARCHAR2(1000 BYTE) )
      
    IS
        v_job_no NUMBER;
        v_job_name VARCHAR2(50 BYTE);
   BEGIN
     select a.job_no into v_job_no from logme a where a.id=v_job_id;
     select a.job_name into v_job_name from logme a where a.id=v_job_id;
     insert into logme(id,job_id,job_sub_id,job_start,job_no,job_name,job_status,job_info)
     values(0,v_job_id,job_sub_id,sysdate,v_job_no,v_job_name,'PROGRESS',job_info);
     commit;
   END;

   PROCEDURE update_process
     ( v_job_id IN NUMBER,
       v_job_sub_id IN NUMBER,
       v_job_progress_percent IN NUMBER(3,0) default -1,
       v_job_info IN VARCHAR2(1000 BYTE) default 'NULL' )
      
    IS
   BEGIN
		IF v_job_sub_id is null then 
		
				 if(v_job_progress_percent=100)then
				  if(v_job_info is not null)then
				   update logme a set a.job_status='CLOSED',
				   a.job_progress_percent=100,a.job_end=sysdate,a.job_info=v_job_info 
				   where a.job_id=v_job_id and a.job_sub_id is null;
				 else
				   update logme a set a.job_status='CLOSED',
				   a.job_progress_percent=100,a.job_end=sysdate 
				   where a.job_id=v_job_id and a.job_sub_id is null;
				 end if;
			   else
				 if(v_job_info is not null)then
				   update logme a set a.job_status='PROGRESS',
				   a.job_progress_percent=v_job_progress_percent, a.job_info=v_job_info 
				   where a.job_id=v_job_id and a.job_sub_id is null;
				 else
				   update logme a set a.job_status='PROGRESS',
				   a.job_progress_percent=v_job_progress_percent 
				   where a.job_id=v_job_id and a.job_sub_id is null;
				 end if;
			   end if;	
		ELSE
				if(v_job_progress_percent=100)then
				 if(v_job_info is not null)then
				   update logme a set a.job_status='CLOSED',a.job_progress_percent=100,
				   a.job_end=sysdate,a.job_info=v_job_info 
				   where a.job_id=v_job_id and a.job_sub_id=v_job_sub_id;
				 else
				   update logme a set a.job_status='CLOSED',
				   a.job_progress_percent=100,a.job_end=sysdate 
				   where a.job_id=v_job_id and a.job_sub_id=v_job_sub_id;
				 end if;
			    else
				 if(v_job_info is not null)then
				   update logme a set a.job_status='PROGRESS',
				   a.job_progress_percent=v_job_progress_percent, 
				   a.job_info=v_job_info where a.job_id=v_job_id and a.job_sub_id=v_job_sub_id;
				 else
				   update logme a set a.job_status='PROGRESS',
				   a.job_progress_percent=v_job_progress_percent 
				   where a.job_id=v_job_id and a.job_sub_id=v_job_sub_id;
				 end if;
			    end if;
		END IF;		
       commit;
   end;
END;