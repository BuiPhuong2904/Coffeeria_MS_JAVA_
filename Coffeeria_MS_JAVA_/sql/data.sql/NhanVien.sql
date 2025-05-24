--------------------------------------------------------
--  File created - Thứ Sáu-tháng 4-04-2025   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table NHANVIEN
--------------------------------------------------------

  CREATE TABLE "ADMINPDB_CAFE"."NHANVIEN" 
   (	"MANV" VARCHAR2(20 BYTE), 
	"HOTEN" VARCHAR2(100 BYTE), 
	"NGSINH" DATE, 
	"SDT" VARCHAR2(15 BYTE), 
	"EMAIL" VARCHAR2(100 BYTE), 
	"NGAYVL" DATE, 
	"CHUCVU" VARCHAR2(50 BYTE), 
	"LUONG" NUMBER(10,2), 
	"MAQL" VARCHAR2(20 BYTE), 
	"MATK" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION DEFERRED 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  TABLESPACE "SYSTEM" ;
REM INSERTING into ADMINPDB_CAFE.NHANVIEN
SET DEFINE OFF;
--------------------------------------------------------
--  DDL for Index SYS_C007429
--------------------------------------------------------

  CREATE UNIQUE INDEX "ADMINPDB_CAFE"."SYS_C007429" ON "ADMINPDB_CAFE"."NHANVIEN" ("MANV") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  Constraints for Table NHANVIEN
--------------------------------------------------------

  ALTER TABLE "ADMINPDB_CAFE"."NHANVIEN" ADD PRIMARY KEY ("MANV")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  TABLESPACE "SYSTEM"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table NHANVIEN
--------------------------------------------------------

  ALTER TABLE "ADMINPDB_CAFE"."NHANVIEN" ADD FOREIGN KEY ("MATK")
	  REFERENCES "ADMINPDB_CAFE"."TAIKHOAN" ("MATK") ENABLE;
