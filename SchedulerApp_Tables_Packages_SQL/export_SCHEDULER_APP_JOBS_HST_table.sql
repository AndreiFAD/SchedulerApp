
--------------------------------------------------------
--  DDL for Table SCHEDULER_APP_JOBS_HST
--------------------------------------------------------

  CREATE TABLE "rep_1"."SCHEDULER_APP_JOBS_HST" 
   (	"JOBID" VARCHAR2(2000 BYTE), 
	"JOBNAME" VARCHAR2(2000 BYTE), 
	"JOBCLASSNAME" VARCHAR2(2000 BYTE), 
	"DESCRIPTION" CLOB, 
	"STARTDATE" DATE, 
	"ENDDATE" DATE, 
	"SECOND" VARCHAR2(100 BYTE), 
	"MINUTE" VARCHAR2(100 BYTE), 
	"HOUR" VARCHAR2(100 BYTE), 
	"DAYOFWEEK" VARCHAR2(100 BYTE), 
	"DAYOFMONTH" VARCHAR2(100 BYTE), 
	"MONTH" VARCHAR2(100 BYTE), 
	"YEAR" VARCHAR2(100 BYTE), 
	"FILEPATH" VARCHAR2(2000 BYTE), 
	"FILENAME" VARCHAR2(2000 BYTE), 
	"FILEFORMAT" VARCHAR2(2000 BYTE), 
	"DBNAME" VARCHAR2(2000 BYTE), 
	"SQLQUERY" CLOB, 
	"METHODTYPE" VARCHAR2(2000 BYTE), 
	"FILENAMEFIX" VARCHAR2(2000 BYTE), 
	"EXCELPATH" VARCHAR2(2000 BYTE), 
	"EXCELNAME" VARCHAR2(2000 BYTE), 
	"MACRONAME" VARCHAR2(2000 BYTE), 
	"EXEFILENAME" VARCHAR2(2000 BYTE), 
	"MYSQLURL" VARCHAR2(2000 BYTE), 
	"MYSQLUSER" VARCHAR2(2000 BYTE), 
	"MYSQLPASS" VARCHAR2(2000 BYTE), 
	"MYSQLQUERY" CLOB, 
	"ORACLEDBNAME" VARCHAR2(2000 BYTE), 
	"ORACLETABEL" VARCHAR2(2000 BYTE), 
	"ORADELETETABEL" VARCHAR2(2000 BYTE), 
	"ORACLECOL" VARCHAR2(2000 BYTE), 
	"FILEFOLDERPATH" VARCHAR2(2000 BYTE), 
	"FILESTRINGNAME" VARCHAR2(2000 BYTE), 
	"CSVSPLITBY" VARCHAR2(2000 BYTE), 
	"MOVEPATCH" VARCHAR2(2000 BYTE), 
	"FILEFORMATTYPETOORA" VARCHAR2(2000 BYTE), 
	"EMAILSUBJECT" VARCHAR2(2000 BYTE), 
	"EMAILTO" VARCHAR2(2000 BYTE), 
	"EMAILCC" VARCHAR2(2000 BYTE), 
	"EMAILBCC" VARCHAR2(2000 BYTE), 
	"EMAILATTACH" VARCHAR2(2000 BYTE), 
	"EMAILMACROFINALFILE" VARCHAR2(2000 BYTE), 
	"USERNAME" VARCHAR2(2000 BYTE), 
	"JOBCHECK" VARCHAR2(2000 BYTE), 
	"NEEDJOBID" VARCHAR2(2000 BYTE), 
	"MOD_USER" VARCHAR2(2000 BYTE), 
	"DATE_STR" VARCHAR2(2000 BYTE), 
	"MOD" VARCHAR2(2000 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "rep_1" 
 LOB ("DESCRIPTION") STORE AS SECUREFILE (
  TABLESPACE "rep_1" ENABLE STORAGE IN ROW CHUNK 8192
  NOCACHE LOGGING  NOCOMPRESS  KEEP_DUPLICATES 
  STORAGE(INITIAL 106496 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) 
 LOB ("SQLQUERY") STORE AS SECUREFILE (
  TABLESPACE "rep_1" ENABLE STORAGE IN ROW CHUNK 8192
  NOCACHE LOGGING  NOCOMPRESS  KEEP_DUPLICATES 
  STORAGE(INITIAL 106496 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) 
 LOB ("MYSQLQUERY") STORE AS SECUREFILE (
  TABLESPACE "rep_1" ENABLE STORAGE IN ROW CHUNK 8192
  NOCACHE LOGGING  NOCOMPRESS  KEEP_DUPLICATES 
  STORAGE(INITIAL 106496 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;
--------------------------------------------------------
--  DDL for Index SCHEDULER_APP_JOBS_HST_INDEX1
--------------------------------------------------------

  CREATE INDEX "rep_1"."SCHEDULER_APP_JOBS_HST_INDEX1" ON "rep_1"."SCHEDULER_APP_JOBS_HST" ("JOBID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "rep_1" ;