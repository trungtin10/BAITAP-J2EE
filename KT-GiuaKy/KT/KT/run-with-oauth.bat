@echo off
REM Chay ung dung voi Google OAuth - SUA 2 dong duoi bang Client ID va Secret that
REM Lay tu: https://console.cloud.google.com/apis/credentials?project=rosy-antler-490503-s9

set GOOGLE_CLIENT_ID=YOUR_CLIENT_ID
set GOOGLE_CLIENT_SECRET=YOUR_CLIENT_SECRET

echo Dang chay voi OAuth...
call mvn spring-boot:run
