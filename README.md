# Chirp — EPAW Final Project

## Requirements
- Java 21
- Maven 3.8+

## How to run

1. Clone or unzip the project.

2. Open a terminal in the project root (where pom.xml lives).

3. Start the server:
   ```
   mvn jetty:run
   ```

4. Open your browser at:
   http://localhost:8080

The database is created automatically on every startup from DB.txt.
Any data created at runtime (posts, follows, likes, bans) is appended
to DB.txt and survives restarts.

## Seed accounts

| Role          | Email                    | Password   |
|---------------|--------------------------|------------|
| Admin         | admin@xgames.com         | Admin123!  |
| Regular user  | alice@example.com        | Alice123!  |
| Regular user  | bob@example.com          | Bob123!    |
| Banned user   | carol@example.com        | Carol123!  |
| Regular user  | dave@example.com         | Dave123!   |

## Notes
- Tweet images must be .jpg or .svg (PNG is rejected).
- Images are stored in src/main/webapp/img/tweets/ and served as
  static files — they persist across restarts independently of DB.txt.
- To fully reset the app, delete lab4.db and clear the runtime-appended
  lines at the bottom of DB.txt (everything after the seed data block).
