; ******************************************************************** **
; Copyright notice                                                     **
; **																   **
; (c) 2003 Entagged Developpement Team				                   **
; http://www.sourceforge.net/projects/entagged                         **
; **																   **
; All rights reserved                                                  **
; **																   **
; This script is part of the Entagged project. The Entagged 		   **
; project is free software; you can redistribute it and/or modify      **
; it under the terms of the GNU General Public License as published by **
; the Free Software Foundation; either version 2 of the License, or    **
; (at your option) any later version.                                  **
; **																   **
; The GNU General Public License can be found at                       **
; http://www.gnu.org/copyleft/gpl.html.                                **
; **																   **
; This copyright notice MUST APPEAR in all copies of the file!         **
; ********************************************************************
;
; $Id: Entagged-Launcher.nsi,v 1.5 2004/07/17 14:42:11 kikidonk Exp $
;

;--------- CONFIGURATION ---------

;If defined run with the console, else not
!define MUI_PRODUCT "Entagged" ;Software name here
!define MUI_VERSION "0.35" ;Software version here
!define ICO "entagged-32x32.ico";Software icone

;!define CONSOLE

!define JARFILE "${MUI_PRODUCT}-${MUI_VERSION}.jar"

;---------------------------------

Name "${MUI_PRODUCT}"
Caption "${MUI_PRODUCT} Launcher"
OutFile "${MUI_PRODUCT}.exe"
Icon "image\${ICO}"

SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow

XPStyle on

Section ""
  ;goto NotFound ;Debug only
  ;Test Wether the JSDK is installed
  ClearErrors
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$R0" "JavaHome"
  IfErrors JavaRuntimeEnvironnement 0  ;If Not, look for a JRE
  goto launch
  
  JavaRuntimeEnvironnement:
  ;Test Wether the JRE is Installed
  ClearErrors
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R0" "JavaHome"
  IfErrors JavaEnvHome 0  ;if not, look for an environnement variable JAVA_HOME
  goto launch
  
  JavaEnvHome:
  ;Test Wether a path to the java VM is found in an environnemental variable
  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  IfErrors NotFound 0 ;if not print a message and abort
  goto launch
  
  NotFound:
  Sleep 800
  MessageBox MB_ICONEXCLAMATION|MB_YESNO \
                    'Could not find a Java Runtime Environment (JRE) installed on your computer ! \
                     $\n$\nWithout it you cannot run ${MUI_PRODUCT} ${MUI_VERSION} . \
                     $\n$\nYou have now the choice to download the JRE.\
                     $\nIf you choose to do so, simply click on "Free Download" in the page that will appear, \
                     then you will be prompted to install the Java Web Plugin, you have to accept to proceed. \
                     $\nAfter that, the JRE Installer will run. Proceed until you finish, then retry to open ${MUI_PRODUCT} ${MUI_VERSION}. \
                     $\n$\nWould you like to visit the Java website to download it? (Recommended)' \
                    IDNO +2
  ExecShell open "http://java.sun.com/getjava"
  Quit
  
  Launch:
!ifdef CONSOLE
  StrCpy $R0 "$R0\bin\java.exe"
!else
  StrCpy $R0 "$R0\bin\javaw.exe"
!endif
  IfFileExists $R0 0 NotFound

  StrCpy $R1 ""
  Call GetParameters
  Pop $R1

  SetOutPath "$EXEDIR"
  ;File "${JARFILE}"
  StrCpy $R0 '$R0 -jar "${JARFILE}" $R1'
  Exec "$R0"
  Quit
SectionEnd

Function GetParameters
  Push $R0
  Push $R1
  Push $R2
  StrCpy $R0 $CMDLINE 1
  StrCpy $R1 '"'
  StrCpy $R2 1
  StrCmp $R0 '"' loop
  StrCpy $R1 ' '
  loop:
    StrCpy $R0 $CMDLINE 1 $R2
    StrCmp $R0 $R1 loop2
    StrCmp $R0 "" loop2
    IntOp $R2 $R2 + 1
    Goto loop
  loop2:
    IntOp $R2 $R2 + 1
    StrCpy $R0 $CMDLINE 1 $R2
    StrCmp $R0 " " loop2
  StrCpy $R0 $CMDLINE "" $R2
  Pop $R2
  Pop $R1
  Exch $R0
FunctionEnd
