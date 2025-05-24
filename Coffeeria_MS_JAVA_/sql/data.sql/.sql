--------------------------------------------------------
--  File created - Thứ Sáu-tháng 4-04-2025   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table TAIKHOAN
--------------------------------------------------------

  CREATE TABLE "ADMINPDB_CAFE"."TAIKHOAN" 
   (	"MATK" VARCHAR2(20 BYTE), 
	"TENTK" VARCHAR2(50 BYTE), 
	"MATKHAU" VARCHAR2(50 BYTE), 
	"LOAITK" VARCHAR2(20 BYTE), 
	"TRANGTHAI" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION DEFERRED 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  TABLESPACE "SYSTEM" ;
REM INSERTING into ADMINPDB_CAFE.TAIKHOAN
SET DEFINE OFF;
--------------------------------------------------------
--  DDL for Index SYS_C007428
--------------------------------------------------------

  CREATE UNIQUE INDEX "ADMINPDB_CAFE"."SYS_C007428" ON "ADMINPDB_CAFE"."TAIKHOAN" ("MATK") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  Constraints for Table TAIKHOAN
--------------------------------------------------------

  ALTER TABLE "ADMINPDB_CAFE"."TAIKHOAN" ADD PRIMARY KEY ("MATK")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  TABLESPACE "SYSTEM"  ENABLE;
