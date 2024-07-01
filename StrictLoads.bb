; ID: 2975
; Author: RifRaf, further modified by MonocleBios
; Date: 2012-09-11 11:44:22
; Title: Safe Loads (b3d) ;strict loads sounds more appropriate IMO
; Description: Get the missing filename reported

;safe loads for mav trapping media issues




;basic wrapper functions that check to make sure that the file exists before attempting to load it, raises an RTE if it doesn't
;more informative alternative to MAVs outside of debug mode, makes it immiediately obvious whether or not someone is loading resources
;likely to cause more crashes than 'clean' CB, as this prevents anyone from loading any assets that don't exist, regardless if they are ever used
;added zero checks since blitz load functions return zero sometimes even if the filetype exists
Function LoadImage_Strict(file$)
	Local imgReplace%=False, imgReplace2%=False
	
	If FileType(file$)<>1 Then
		imgReplace% = True
		imgReplace2% = True
	EndIf
	If (Not imgReplace%)
		tmp = LoadImage(file$)
		If tmp = 0 Then tmp = LoadImage(file$)
	Else
		CreateConsoleMsg("Image " + file$ + " not found.")
		ConsoleInput = ""
		If (Not NTF_DisableConsoleOpening%)
			ConsoleOpen = True
		EndIf
	EndIf 
	If tmp = 0 Then imgReplace% = True
	If imgReplace%
		If ReplaceIMG%=0
			LoadMissingTextures(0)
			tmp = ReplaceIMG%
		Else
			tmp = CopyImage(ReplaceIMG%)
		EndIf
		If (Not imgReplace2%)
			CreateConsoleMsg("Failed to load Image: " + file$)
			ConsoleInput = ""
			If (Not NTF_DisableConsoleOpening%)
				ConsoleOpen = True
			EndIf
		EndIf
		NTF_ErrorAmount% = NTF_ErrorAmount% + 1
	EndIf
	Return tmp
End Function

Function LoadSound_Strict(file$)
	
	If FileType(file$)<>1 Then 
		CreateConsoleMsg("Sound " + file$ + " not found.")
		ConsoleInput = ""
		If (Not NTF_DisableConsoleOpening%)
			ConsoleOpen = True
		EndIf
		NTF_ErrorAmount% = NTF_ErrorAmount% + 1
		Return 0
	EndIf
    tmp = LoadSound(file$)
	If tmp = 0 Then 
		CreateConsoleMsg("Failed to load Sound:" + file$)
		ConsoleInput = ""
		If (Not NTF_DisableConsoleOpening%)
			ConsoleOpen = True
		EndIf
		NTF_ErrorAmount% = NTF_ErrorAmount% + 1
	EndIf
	Return tmp
End Function

Function LoadMesh_Strict(File$,parent=0)
	Local noLQ% = False, isNTFmodel% = False, File2$ = ""
	
	;If Lower(Left(File$, Instr(File$, "/") - 1)) = "NineTailedFoxMod" Then isNTFmodel% = True
	StrTemp$ = Lower(Left(File$, Instr(File$, "\") - 1))
	If Lower(StrTemp$) = "NineTailedFoxMod" Then isNTFmodel% = True
	
	DebugLog File
	
	If NTF_LQModels%
		If FileType(File$)<>1 Then RuntimeError "3D Mesh " + File$ + " not found."
		If noLQ% = False
			If (Not isNTFmodel%)
				File2$ = "NineTailedFoxMod\"+File$
			Else
				File2$ = File$
			EndIf
			File2$ = Replace(File2$,".","_lq.")
			tmp = LoadMesh(File2$, parent)
			If tmp <> 0 Then DebugLog "Low poly model: "+File2$
			If tmp = 0 Then noLQ% = True
			If tmp = 0
				tmp = LoadMesh(File$, parent)
				If tmp = 0 Then RuntimeError "Failed to load 3D Mesh: " + File$
			EndIf
		Else
			tmp = LoadMesh(File$, parent)
			If tmp = 0 Then RuntimeError "Failed to load 3D Mesh: " + File$
		EndIf
	Else
		If FileType(File$)<>1 Then RuntimeError "3D Mesh " + File$ + " not found."
		tmp = LoadMesh(File$, parent)
		If tmp = 0 Then RuntimeError "Failed to load 3D Mesh: " + File$
	EndIf
	 
	Return tmp  
End Function   

Function LoadAnimMesh_Strict(File$,parent=0)
	Local noLQ% = False, isNTFmodel% = False, File2$ = ""
	
	;If Lower(Left(File$, Instr(File$, "/") - 1)) = "NineTailedFoxMod" Then isNTFmodel% = True
	StrTemp$ = Lower(Left(File$, Instr(File$, "\") - 1))
	If Lower(StrTemp$) = "NineTailedFoxMod" Then isNTFmodel% = True
	
	DebugLog File
	
	If NTF_LQModels%
		If FileType(File$)<>1 Then RuntimeError "3D Animated Mesh " + File$ + " not found."
		If noLQ% = False
			If (Not isNTFmodel%)
				File2$ = "NineTailedFoxMod\"+File$
			Else
				File2$ = File$
			EndIf
			File2$ = Replace(File2$,".","_lq.")
			tmp = LoadAnimMesh(File2$, parent)
			If tmp <> 0 Then DebugLog "Low Poly model: "+File2$
			If tmp = 0 Then noLQ% = True
			If tmp = 0
				tmp = LoadAnimMesh(File$, parent)
				If tmp = 0 Then RuntimeError "Failed to load 3D Animated Mesh: " + File$
			EndIf
		Else
			tmp = LoadAnimMesh(File$, parent)
			If tmp = 0 Then RuntimeError "Failed to load 3D Animated Mesh: " + File$
		EndIf
	Else
		If FileType(File$)<>1 Then RuntimeError "3D Animated Mesh " + File$ + " not found."
		tmp = LoadAnimMesh(File$, parent)
		If tmp = 0 Then RuntimeError "Failed to load 3D Animated Mesh: " + File$
	EndIf
	
	Return tmp
End Function   

;don't use in LoadRMesh, as Reg does this manually there. If you wanna fuck around with the logic in that function, be my guest 
Function LoadTexture_Strict(File$,flags=1)
	Local texReplace%=False, texReplace2%=False
	
	If FileType(File$)<>1 Then
		texReplace% = True
		texReplace2% = True
	EndIf
	If (Not texReplace%)
		tmp = LoadTexture(File$, flags)
		If tmp = 0 Then tmp = LoadTexture(File$, flags)
	Else
		CreateConsoleMsg("Texture " + file$ + " not found.")
		ConsoleInput = ""
		If (Not NTF_DisableConsoleOpening%)
			ConsoleOpen = True
		EndIf
	EndIf
	If tmp = 0 Then texReplace% = True
	If texReplace%
		;tmp = CreateTexture(128,128,flags)
		If ReplaceTexture=0
			LoadMissingTextures(1)
			tmp = ReplaceTexture%
		Else
			tmp = CopyTexture(ReplaceTexture%)
		EndIf
		If (Not texReplace2%)
			CreateConsoleMsg("Failed to load Texture: " + file$)
			ConsoleInput = ""
			If (Not NTF_DisableConsoleOpening%)
				ConsoleOpen = True
			EndIf
		EndIf
		NTF_ErrorAmount% = NTF_ErrorAmount% + 1
	EndIf
	Return tmp 
End Function   

Function LoadBrush_Strict(file$,flags,u#=1.0,v#=1.0)
	Local brushReplace%=False, brushReplace2%=False
	
	If FileType(file$)<>1 Then
		brushReplace% = True
		brushReplace2% = True
	EndIf
	If (Not brushReplace%)
		tmp = LoadBrush(file$, flags, u, v)
		If tmp = 0 Then tmp = LoadBrush(file$, flags, u, v)
	Else
		CreateConsoleMsg("Brush Texture " + file$ + "not found.")
		ConsoleInput = ""
		If (Not NTF_DisableConsoleOpening%)
			ConsoleOpen = True
		EndIf
	EndIf
	If tmp = 0 Then brushReplace% = True
	If brushReplace%
		tmp = CreateBrush()
		If ReplaceTexture%=0 Then LoadMissingTextures(1)
		BrushTexture tmp,ReplaceTexture%
		If (Not brushReplace2%)
			CreateConsoleMsg("Failed to load Brush: " + file$)
			ConsoleInput = ""
			If (Not NTF_DisableConsoleOpening%)
				ConsoleOpen = True
			EndIf
		EndIf
		NTF_ErrorAmount% = NTF_ErrorAmount% + 1
	EndIf
	Return tmp 
End Function 

;Modified for Fasttext
Function LoadFont_Strict(file$="Tahoma", height=13, bold=0, italic=0, underline=0, angle#=0, smooth=FT_ANTIALIASED, encoding=FT_ASCII)
	Local fontReplace%=False, fontReplace2%=False
	
	If FileType(file$)<>1 Then
		fontReplace% = True
		fontReplace2% = True
	EndIf
	If (Not fontReplace%)
		tmp = LoadFont(file, height, bold, italic, underline, angle, smooth, encoding)  
		If tmp = 0 Then tmp = LoadFont(file, height, bold, italic, underline, angle, smooth, encoding)
	Else
		CreateConsoleMsg("Font: " + file$ + " not found.")
		ConsoleInput = ""
		If (Not NTF_DisableConsoleOpening%)
			ConsoleOpen = True
		EndIf
	EndIf
	If tmp = 0 Then fontReplace% = True
	If fontReplace%
		tmp = LoadFont("Arial", height, bold, italic, underline, angle, smooth, encoding)
		If (Not fontReplace2%)
			CreateConsoleMsg("Failed to load Font: " + file$)
			ConsoleInput = ""
			If (Not NTF_DisableConsoleOpening%)
				ConsoleOpen = True
			EndIf
		EndIf
		NTF_ErrorAmount% = NTF_ErrorAmount% + 1
	EndIf
	Return tmp
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D

Function LoadMissingTextures(numb%)
	
	If numb%=0
		ReplaceIMG% = CreateImage(128,128)
		SetBuffer ImageBuffer(ReplaceImg%)
		Color 1,1,1
		Rect 0,0,32,32
		Rect 32,32,32,32
		Rect 64,64,32,32
		Rect 96,96,32,32
		Rect 0,64,32,32
		Rect 64,0,32,32
		Rect 32,96,32,32
		Rect 96,32,32,32
		Color 255,0,255
		Rect 0,32,32,32
		Rect 32,0,32,32
		Rect 32,64,32,32
		Rect 64,32,32,32
		Rect 64,96,32,32
		Rect 96,64,32,32
		Rect 0,96,32,32
		Rect 96,0,32,32
		SetBuffer BackBuffer()
	Else
		ReplaceTexture% = CreateTexture(128,128)
		SetBuffer TextureBuffer(ReplaceTexture%)
		ClsColor 255,255,255
		Cls
		Color 1,1,1
		Rect 0,0,32,32
		Rect 32,32,32,32
		Rect 64,64,32,32
		Rect 96,96,32,32
		Rect 0,64,32,32
		Rect 64,0,32,32
		Rect 32,96,32,32
		Rect 96,32,32,32
		Color 255,0,255
		Rect 0,32,32,32
		Rect 32,0,32,32
		Rect 32,64,32,32
		Rect 64,32,32,32
		Rect 64,96,32,32
		Rect 96,64,32,32
		Rect 0,96,32,32
		Rect 96,0,32,32
		SetBuffer BackBuffer()
	EndIf
	
End Function

Function CopyTexture(texture, flags = 1)
	tw = TextureWidth (texture): th = TextureHeight (texture)
	tex = CreateTexture (tw, th, flags)
	tb = TextureBuffer (texture)
	txb = TextureBuffer (tex)
	LockBuffer txb
	LockBuffer tb
	For x = 0 To tw - 1
		For y = 0 To th - 1
			WritePixelFast x, y, ReadPixelFast (x, y, tb), txb
		Next
	Next
	UnlockBuffer tb
	UnlockBuffer txb
	Return tex
End Function