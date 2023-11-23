@echo off

:: Set the path to your virtual environment's activate.bat
set env_activate=.\env\Scripts\activate.bat

:: Activate the virtual environment
call "%env_activate%"

:: Check if the environment activation was successful
if %errorlevel% neq 0 (
    echo Error: Failed to activate the virtual environment.
    pause
    exit /b
)

@rem echo path
@rem pause

@rem start cmd.exe /k

@rem start cmd.exe


:: Launch Excel with the specified workbook
start excel ".\TryFrench.xlsm"
