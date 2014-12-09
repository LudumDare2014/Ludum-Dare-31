@echo off
:loop
set /p id="Enter ID:"
copy "clear.tmx" "map %id%.tmx"
goto loop