# 📅 Agenda Personnel

Personal agenda desktop application built with **Java (JavaFX)**, **JDBC** and an **Oracle** database.

## Features
- Full CRUD on events (title, date, time, description, type, important flag)
- Search by different criteria
- Export events to a text file

## Setup
1. Create an Oracle user `agenda_user` and run `schema.sql` (optionally `sample_data.sql` for demo events) (Oracle XE, service `XEPDB1`).
2. Adjust the credentials in `src/agendaapp/ConnexionDB.java` if needed.
3. Add the Oracle JDBC driver (`ojdbc11`) to the project libraries in NetBeans.
4. Run `AgendaApp`.

## Docs
Project report (French): [`docs/rapport-agenda.pdf`](docs/rapport-agenda.pdf)

## Authors
Eya Ibn Abdelhedi & Amira Midani – 2nd year Licence Nationale, Business Computing (Informatique de Gestion), ISG Tunis, 2025/2026.
