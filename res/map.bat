@echo off
:loop
set /p id="Enter ID:"
copy "clear.tmx" "map_%id%.tmx"
goto loop