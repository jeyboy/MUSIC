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
; $Id: Entagged-Installer.nsi,v 1.9 2004/10/30 23:06:09 kikidonk Exp $
;

!define PRODUCT "Entagged" ;Software name here
!define VERSION "0.35" ;Software version here

!include "MUI.nsh"

;--------------------------------
;Configuration

	;General
	Name "${PRODUCT}"
	OutFile "Entagged-install-w32-${VERSION}.exe"
	
	;Folder selection page
	InstallDir "$PROGRAMFILES\${PRODUCT}"
	
	XPStyle on
	CRCCheck force
	ShowInstDetails show
	ShowUninstDetails show
	SetDateSave on
	SetDatablockOptimize on
	Caption "${PRODUCT} Installation"
	BrandingText "${PRODUCT} Installation (built with NSIS)"

;--------------------------------
;Options
!define MUI_ABORTWARNING
!define MUI_UNABORTWARNING
!define MUI_ICON "image\setup.ico"      ;icon installer
!define MUI_UNICON "image\normal-uninstall.ico"  ;icon uninstaller
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "image\header.bmp"
!define MUI_WELCOMEFINISHPAGE_BITMAP "image\wizard.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP "image\wizard.bmp"
!define MUI_COMPONENTSPAGE_CHECKBITMAP "image\checks.bmp"
;!define MUI_SPECIALBITMAP "image\wizard.bmp"

;--------------------------------
;Modern UI Configuration

  	!insertmacro MUI_PAGE_WELCOME
	!insertmacro MUI_PAGE_LICENSE "build\docs\license.txt"
	!insertmacro MUI_PAGE_COMPONENTS
  	!insertmacro MUI_PAGE_DIRECTORY
  
	Var STARTMENU_FOLDER
		!insertmacro MUI_PAGE_STARTMENU "Application" $STARTMENU_FOLDER
		!define MUI_STARTMENUPAGE_DEFAULTFOLDER "${PRODUCT}"
		!define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU"
		!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\${PRODUCT}"
		!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
		!define TEMP $R0
	
	!insertmacro MUI_PAGE_INSTFILES

	!insertmacro MUI_PAGE_FINISH
		!define MUI_FINISHPAGE_NOAUTOCLOSE
		!define MUI_FINISHPAGE_NOREBOOTSUPPORT
		;!define MUI_PAGE_FINISH_SHOWREADME "$INSTDIR\docs\readme.txt"

	!insertmacro MUI_UNPAGE_WELCOME
	!insertmacro MUI_UNPAGE_CONFIRM
	!insertmacro MUI_UNPAGE_INSTFILES
	!insertmacro MUI_UNPAGE_FINISH
		!define MUI_UNFINISHPAGE_NOAUTOCLOSE

;--------------------------------
;Languages
 
	!insertmacro MUI_LANGUAGE "English"
 
;--------------------------------
;Data
  
	;LicenseData "build\docs\license.txt"

;--------------------------------
;Installer Sections

Section "Entagged Core" CoreFiles
	SectionIn RO
	
	SetOutPath "$INSTDIR"
	File "build\*.exe"
    File "build\*.jar"
	SetOutPath "$INSTDIR\docs\"
	File "build\docs\*.txt"	
	;Store install folder
	;WriteRegStr HKCU "Software\${PRODUCT}" "" $INSTDIR
	
	!insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    	
		;Create shortcuts
		CreateDirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
		CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\TagEditor.lnk" "$INSTDIR\tageditor.exe"
		CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\TagEditor-Debug.lnk" "$INSTDIR\tageditor-debug.exe"
		CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall.lnk" "$INSTDIR\Uninstall.exe"

	!insertmacro MUI_STARTMENU_WRITE_END
	
	;Uninstall menu infos
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "DisplayName" "${PRODUCT} ${VERSION}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "UninstallString" '"$INSTDIR\Uninstall.exe"'
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "InstallLocation" "$INSTDIR"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "DisplayIcon" "$INSTDIR\resources\images\entagged-icon.gif"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "UninstallString" "$INSTDIR\Uninstall.exe"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "URLInfoAbout" "http://entagged.sourceforge.net/"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "DisplayVersion" "${VERSION}"
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "NoRepair" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}" "NoModify" 1
	
	;Ask for Desktop Shortcuts
	MessageBox MB_YESNO|MB_ICONQUESTION|MB_TOPMOST "Do you want to install Desktop Shortcuts ?" IDNO NoDesktopShortcut
	CreateShortCut "$DESKTOP\TagEditor.lnk" "$INSTDIR\tageditor.exe"
	
	NoDesktopShortcut:
	;Create uninstaller
	WriteUninstaller "$INSTDIR\Uninstall.exe"

SectionEnd


;Add source distribution to this package.. not needed...
;Section "Entagged Sources" SourceFiles
;
; SetOutPath "$INSTDIR\src\entagged\"
; File /r "build\src\entagged\*.*"
; SetOutPath "$INSTDIR\src\javadoc\"
; File /r "build\src\javadoc\*.*"
  
;SectionEnd



Section "Entagged Documentation" DocFiles

	SetOutPath "$INSTDIR\docs\"
	File "build\docs\*.*"
	SetOutPath "$INSTDIR\docs\images\"
	File "build\docs\images\*.*"
	  
SectionEnd

;--------------------------------
;Descriptions
	;Language Strings

	;Description
	LangString DESC_CoreFiles ${LANG_ENGLISH} "Installs ${PRODUCT} ${VERSION} application (needed)."
	;LangString DESC_SourceFiles ${LANG_ENGLISH} "Installs ${PRODUCT} ${VERSION} Java source files."
	LangString DESC_DocFiles ${LANG_ENGLISH} "Installs ${PRODUCT} ${VERSION} documentation."

!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
	!insertmacro MUI_DESCRIPTION_TEXT ${CoreFiles} $(DESC_CoreFiles)
	;!insertmacro MUI_DESCRIPTION_TEXT ${SourceFiles} $(DESC_SourceFiles)
	!insertmacro MUI_DESCRIPTION_TEXT ${DocFiles} $(DESC_DocFiles)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"

	;Remove the uninstall infos
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT}"
	
	
	RMDir  /r "$INSTDIR"
	Delete "$DESKTOP\TagEditor.lnk"
	
	;Remove shortcut
	ReadRegStr ${TEMP} "${MUI_STARTMENUPAGE_REGISTRY_ROOT}" "${MUI_STARTMENUPAGE_REGISTRY_KEY}" "${MUI_STARTMENUPAGE_REGISTRY_VALUENAME}"
	
	StrCmp ${TEMP} "" noshortcuts
		
		Delete "$SMPROGRAMS\$STARTMENU_FOLDER\TagEditor.lnk"
		Delete "$SMPROGRAMS\$STARTMENU_FOLDER\TagEditor-Debug.lnk"
		Delete "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall.lnk"
		RMDir "$SMPROGRAMS\$STARTMENU_FOLDER\" ;Only if empty, so it won't delete other shortcuts

	noshortcuts:
	;Desktop Shortcuts
	DeleteRegKey ${MUI_STARTMENUPAGE_REGISTRY_ROOT} ${MUI_STARTMENUPAGE_REGISTRY_KEY}

SectionEnd

Function .onInit
	InitPluginsDir
	File /oname=$PLUGINSDIR\splash.bmp "image\splash.bmp"
	#optional
	#File /oname=$PLUGINSDIR\splash.wav "image\splash.wav"
	
	advsplash::show 800 300 200 0xFFFFFF $PLUGINSDIR\splash
	
	Pop $0 ; $0 has '1' if the user closed the splash screen early,
		  ; '0' if everything closed normal, and '-1' if some error occured.

FunctionEnd