--------------------------------------------------------
--  DDL for Table LOGME
--------------------------------------------------------

  CREATE TABLE "LOGME" 
   (	"ID" NUMBER, 
	"JOB_START" DATE, 
	"JOB_END" DATE, 
	"JOB_AVG_RUNTIME" NUMBER, 
	"JOB_NO" NUMBER, 
	"JOB_NAME" VARCHAR2(50 BYTE), 
	"JOB_STATUS" VARCHAR2(50 BYTE), 
	"JOB_PROGRESS_PERCENT" NUMBER(3,0), 
	"JOB_INFO" VARCHAR2(1000 BYTE), 
	"JOB_FIRSTERROR" VARCHAR2(512 BYTE), 
	"JOB_NEXTERROR" VARCHAR2(512 BYTE), 
	"JOB_ID" NUMBER, 
	"JOB_SUB_ID" NUMBER
   ) ;
   
--------------------------------------------------------
--  DDL for Index PK_LOGME_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "PK_LOGME_ID" ON "LOGME" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS ;
  
--------------------------------------------------------
--  DDL for Index LOGME_DATE_IDX
--------------------------------------------------------

  CREATE INDEX "LOGME_DATE_IDX" ON "LOGME" ("JOB_START", "JOB_END") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS ;
  
--------------------------------------------------------
--  DDL for Index LOGME_I
--------------------------------------------------------

  CREATE INDEX "LOGME_I" ON "LOGME" ("JOB_NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS ;
  
--------------------------------------------------------
--  DDL for Index LOGME_I2
--------------------------------------------------------

  CREATE INDEX "LOGME_I2" ON "LOGME" ("JOB_STATUS") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS ;
  
--------------------------------------------------------
--  DDL for Index LOGME_I3
--------------------------------------------------------

  CREATE INDEX "LOGME_I3" ON "LOGME" ("JOB_START") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS ;
  
--------------------------------------------------------
--  Constraints for Table LOGME
--------------------------------------------------------

  ALTER TABLE "LOGME" ADD CONSTRAINT "PK_LOGME_ID" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS ;
  
--------------------------------------------------------
--  DDL for Trigger TR_LOGME_ID
--------------------------------------------------------

CREATE OR REPLACE TRIGGER "TR_LOGME_ID" 
BEFORE INSERT ON LOGME
    FOR EACH ROW
    BEGIN
      SELECT SEQ_LOGME_ID.NEXTVAL
      INTO   :new.ID
      FROM   dual;
    END;
    /
ALTER TRIGGER "TR_LOGME_ID" ENABLE;
