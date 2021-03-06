CREATE OR REPLACE PACKAGE REPAPP.logme_package
  IS
  
  PROCEDURE mail_send
     (v_from IN VARCHAR2,
      v_to IN VARCHAR2,
      v_cc IN VARCHAR2,
      v_bcc IN VARCHAR2,
      v_subject IN VARCHAR2,
      v_message IN clob
      );
  
   PROCEDURE log_time_send
   	 ( v_logid IN VARCHAR2,
       v_runtime OUT VARCHAR2);
       
   PROCEDURE new_process
     ( v_id OUT NUMBER,
       v_job_no IN NUMBER,
       job_name IN VARCHAR2,
       job_info IN VARCHAR2);

   PROCEDURE close_process
     ( v_id IN NUMBER,
       v_job_info IN VARCHAR2,
       v_error VARCHAR2 default '' );

   PROCEDURE new_subprocess
     ( v_job_id IN NUMBER,
       job_sub_id IN NUMBER,
       job_info IN VARCHAR2 );

   PROCEDURE update_process
     ( v_job_id IN NUMBER,
       v_job_sub_id IN NUMBER,
       v_job_progress_percent IN NUMBER default -1,
       v_job_info IN VARCHAR2 default 'NULL' );
END;
