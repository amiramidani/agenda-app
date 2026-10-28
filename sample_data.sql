-- Sample data for the Agenda app (run after schema.sql)
SET DEFINE OFF;
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('4','devoir',to_date('15/06/26','DD/MM/RR'),'08:00','ds poo','Personnel','1');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('6','Soutenance PFE',to_date('15/06/26','DD/MM/RR'),'09:00','Présentation finale du projet','Réunion','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('3','Soutenance PFE',to_date('15/06/26','DD/MM/RR'),'09:00',' Présentation finale du projet de fin d''études','Réunion','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('5','Réunion TP',to_date('10/05/26','DD/MM/RR'),'14:00','Présentation du projet','Réunion','1');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('7','Consultation dentaire',to_date('12/05/26','DD/MM/RR'),'10:30','Contrôle annuel chez le dentiste','Médical','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('8','Réunion équipe projet',to_date('14/05/26','DD/MM/RR'),'14:00','Point d avancement sprint 3','Travail','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('9','Anniversaire Amine',to_date('03/05/26','DD/MM/RR'),'14:00','Fête surprise chez Amine','Personnel','1');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('10','Cours de sport',to_date('10/05/26','DD/MM/RR'),'08:00','Session musculation salle de sport','Personnel','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('11','Entretien stage',to_date('18/05/26','DD/MM/RR'),'11:00','Entretien avec entreprise TechCorp','Travail','1');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('12','Visite médicale',to_date('01/06/26','DD/MM/RR'),'15:00','Bilan de santé général','Médical','1');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('13','Hackathon IA',to_date('05/06/26','DD/MM/RR'),'09:00','Compétition intelligence artificielle 48h','Travail','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('14','Dîner en famille',to_date('25/05/26','DD/MM/RR'),'20:00','Repas dominical chez les parents','Personnel','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('15','Réunion club informatique',to_date('28/05/26','DD/MM/RR'),'16:00','Préparation événement fin d année','Réunion','0');
Insert into EVENEMENT (ID,TITRE,DATE_EVENEMENT,HEURE,DESCRIPTION,TYPE_EVENEMENT,IMPORTANT) values ('41','mon anniversaire',to_date('06/10/26','DD/MM/RR'),'00:00','mon 22 anniversaire','Personnel','1');
--------------------------------------------------------
--  DDL for Index SYS_C008225
--------------------------------------------------------
--
  CREATE UNIQUE INDEX "AGENDA_USER"."SYS_C008225" ON "AGENDA_USER"."EVENEMENT" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table EVENEMENT
--------------------------------------------------------
--
  ALTER TABLE "AGENDA_USER"."EVENEMENT" MODIFY ("ID" NOT NULL ENABLE);
COMMIT;
