Type LanguageStrings
	Field txt$ = ""
	Field untrans_txt$ = ""
	Field found%
End Type

Dim LANG$(10000)
Dim LANG_Name$(10000)

Global LanguageAmount% = 0
Global SelectedLangString$ = GetINIString("NineTailedFoxMod\options.INI","options","current language","EN_US")+".lang"
Global SelectedLang% = 0

Function LoadLanguages()
	
	LANG$(0)="EN_US.lang"
	LANG_Name$(0)="English"
	DebugLog "Language found: "+LANG_Name$(0)
	
	myDir=ReadDir("NineTailedFoxMod\Languages\") 
	Repeat 
		file$=NextFile$(myDir) 
		If file$="" Then Exit 
		If FileType("NineTailedFoxMod\Languages\"+file$) <> 0 Then 
			If Right$(file$,5) = ".lang"
				LanguageAmount%=LanguageAmount%+1
				LANG$(LanguageAmount%)=file$
				LANG_Name$(LanguageAmount%)=GetINIString("NineTailedFoxMod\Languages\"+LANG$(LanguageAmount%),"global","language",LANG$(LanguageAmount%))
				DebugLog ("Language found: "+LANG_Name$(LanguageAmount%))
			EndIf
		EndIf 
	Forever 
	CloseDir myDir
	
	For i = 1 To LanguageAmount%+1
		If LANG$(i-1)=SelectedLangString$
			DebugLog "Selected Language ID: "+SelectedLang%
			Exit
		Else
			SelectedLang% = SelectedLang% + 1
		EndIf
	Next
	
	If SelectedLang% > LanguageAmount%
		SelectedLang% = 0
		SelectedLangString$ = "EN_US.lang"
	EndIf
	
	If SelectedLangString$ <> "EN_US.lang"
		FreeFont Font1%
		FreeFont Font2%
		FreeFont Font3%
		FreeFont Font4%
		FreeFont Font5%
		FreeFont Font6%
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font1","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font1% = LoadFont_Strict("GFX\cour.ttf", Int(18 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font1% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(18 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font1% = LoadFont_Strict("GFX\cour.ttf", Int(18 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font2","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font2% = LoadFont_Strict("NineTailedFoxMod\GFX\Capture it.ttf", Int(58* (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font2% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(58* (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font2% = LoadFont_Strict("NineTailedFoxMod\GFX\Capture it.ttf", Int(58* (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font3","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font3% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font3% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font3% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font4","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font4% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(60 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font4% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(60 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font4% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(60 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font5","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font5% = LoadFont_Strict("GFX\courbd.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font5% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font5% = LoadFont_Strict("GFX\courbd.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font6","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font6% = LoadFont_Strict("GFX\cour.ttf", Int(34 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font6% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(34 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font6% = LoadFont_Strict("GFX\cour.ttf", Int(34 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
	EndIf
	
End Function

Function Lang_Replace$(c_txt$)
	Local replaceTxt$
	
	If SelectedLang% = 0 ;EN_US.lang
		Return c_txt$
	Else ;other Language
		Local temp=0
		For ls.LanguageStrings = Each LanguageStrings
			If ls\found% = True
				If ls\untrans_txt$ = c_txt$
					temp=1
					Return ls\txt$
					Exit
				EndIf
			EndIf
		Next
		
		If temp=0
			replaceTxt$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"strings",c_txt$,c_txt$)
			ls.LanguageStrings = New LanguageStrings
			ls\txt$ = replaceTxt$
			ls\untrans_txt$ = c_txt$
			ls\found% = True
			DebugLog "Saved Language String: "+ls\txt$
			Return ls\txt$
		EndIf
	EndIf
	
End Function

Function Lang_Change(newlang%)
	Local TempString$ = ""
	
	If SelectedLang% <> newlang%
		For ls.LanguageStrings = Each LanguageStrings
			Delete ls
		Next
	EndIf
	
	SelectedLang% = newlang%
	SelectedLangString$ = LANG$(newlang%)
	
	If SelectedLangString$ <> "EN_US.lang"
		FreeFont Font1%
		FreeFont Font2%
		FreeFont Font3%
		FreeFont Font4%
		FreeFont Font5%
		FreeFont Font6%
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font1","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font1% = LoadFont_Strict("GFX\cour.ttf", Int(18 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font1% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(18 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font1% = LoadFont_Strict("GFX\cour.ttf", Int(18 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font2","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font2% = LoadFont_Strict("NineTailedFoxMod\GFX\Capture it.ttf", Int(58* (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font2% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(58* (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font2% = LoadFont_Strict("NineTailedFoxMod\GFX\Capture it.ttf", Int(58* (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font3","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font3% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font3% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font3% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font4","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font4% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(60 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font4% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(60 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font4% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(60 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font5","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font5% = LoadFont_Strict("GFX\courbd.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font5% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font5% = LoadFont_Strict("GFX\courbd.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
		
		TempString$ = GetINIString("NineTailedFoxMod\Languages\"+LANG$(SelectedLang%),"global","font6","")
		If TempString$ <> ""
			If FileType("NineTailedFoxMod\Languages\"+TempString$)<>1
				Font6% = LoadFont_Strict("GFX\cour.ttf", Int(34 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
			Else
				Font6% = LoadFont_Strict("NineTailedFoxMod\Languages\"+TempString$, Int(34 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
				DebugLog "Custom Font: "+TempString$
			EndIf
		Else
			Font6% = LoadFont_Strict("GFX\cour.ttf", Int(34 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		EndIf
	Else
		FreeFont Font1%
		FreeFont Font2%
		FreeFont Font3%
		FreeFont Font4%
		FreeFont Font5%
		FreeFont Font6%
		
		Font1% = LoadFont_Strict("GFX\cour.ttf", Int(18 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		Font2% = LoadFont_Strict("NineTailedFoxMod\GFX\Capture it.ttf", Int(58* (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		Font3% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		Font4% = LoadFont_Strict("GFX\DS-DIGI.ttf", Int(60 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		Font5% = LoadFont_Strict("GFX\courbd.ttf", Int(22 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
		Font6% = LoadFont_Strict("GFX\cour.ttf", Int(34 * (GraphicHeight / 1024.0)), 0,0,0,0, FT_DEFAULT)
	EndIf
	
	difficulties(SAFE)\name = Lang_Replace$("Safe")
	difficulties(SAFE)\description = Lang_Replace$("The game can be saved any time. However, as in the case of SCP Objects, a Safe classification does not mean that handling it does not pose a threat.")
	difficulties(EUCLID)\name = Lang_Replace$("Euclid")
	difficulties(EUCLID)\description = Lang_Replace$("Euclid-class objects are insufficiently understood or inherently unpredictable,")+" "
	difficulties(EUCLID)\description = difficulties(EUCLID)\description +Lang_Replace$("such that reliable containment is not always possible.")+" "
	difficulties(EUCLID)\description = difficulties(EUCLID)\description +Lang_Replace$("In Euclid difficulty, saving only allowed at specific locations marked by lit up computer screens.")
	difficulties(KETER)\name = Lang_Replace$("Keter")
	difficulties(KETER)\description = Lang_Replace$("Keter-class objects are considered the most dangerous ones in Foundation containment.")+" "
	difficulties(KETER)\description = difficulties(KETER)\description +Lang_Replace$("The same can be said for this difficulty level: the SCPs are more aggressive, and you have only one life - when you die, the game is over. ")
	difficulties(CUSTOM)\name = Lang_Replace$("Custom")
	
End Function