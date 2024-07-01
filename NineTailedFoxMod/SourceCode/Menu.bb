;Global MenuBack% = LoadImage_Strict("GFX\menu\back.jpg")
Global MenuBack% = LoadImage_Strict("NineTailedFoxMod\GFX\menu\back.jpg")
Global MenuText% = LoadImage_Strict("GFX\menu\scptext.jpg")
;Global Menu173% = LoadImage_Strict("GFX\menu\173back.jpg")
Global Menu173% = LoadImage_Strict("NineTailedFoxMod\GFX\menu\MTFback.jpg")
MenuWhite = LoadImage_Strict("GFX\menu\menuwhite.jpg")
MenuBlack = LoadImage_Strict("GFX\menu\menublack.jpg")
MaskImage MenuBlack, 255,255,0


ResizeImage(MenuBack, ImageWidth(MenuBack) * MenuScale, ImageHeight(MenuBack) * MenuScale)
ResizeImage(MenuText, ImageWidth(MenuText) * MenuScale, ImageHeight(MenuText) * MenuScale)
ResizeImage(Menu173, ImageWidth(Menu173) * MenuScale, ImageHeight(Menu173) * MenuScale)

For i = 0 To 3
	ArrowIMG(i) = LoadImage_Strict("GFX\menu\arrow.png")
	RotateImage(ArrowIMG(i), 90 * i)
	HandleImage(ArrowIMG(i), 0, 0)
Next

Global RandomSeed$

Dim MenuBlinkTimer%(2), MenuBlinkDuration%(2)
MenuBlinkTimer%(0) = 1
MenuBlinkTimer%(1) = 1

Global MenuStr$, MenuStrX%, MenuStrY%

Global MainMenuTab%


Global IntroEnabled% = True

Global SelectedInputBox%

Global SavePath$ = "Saves\"

;nykyisen tallennuksen nimi ja samalla miss� kansiossa tallennustiedosto sijaitsee saves-kansiossa
Global CurrSave$

Global SaveGameAmount%
Dim SaveGames$(SaveGameAmount+1) 
Dim SaveGameTime$(SaveGameAmount + 1)
Dim SaveGameDate$(SaveGameAmount + 1)

Const MAXSAVEDMAPS = 20
Dim SavedMaps$(MAXSAVEDMAPS)
Global SelectedMap$

LoadSaveGames()

;[Block]
Dim KeyName$(211)
KeyName(1)="Esc"
For i = 2 To 10
	KeyName(i)=i-1
Next
KeyName(11)="0"
KeyName(12)="-"
KeyName(13)="="
KeyName(14)="Backspace"
KeyName(15)="TAB"
KeyName(16)="Q"
KeyName(17)="W"
KeyName(18)="E"
KeyName(19)="R"
KeyName(20)="T"
KeyName(21)="Y"
KeyName(22)="U"
KeyName(23)="I"
KeyName(24)="O"
KeyName(25)="P"
KeyName(26)="["
KeyName(27)="]"
KeyName(28)="Enter"
KeyName(29)="Left Ctrl"
KeyName(30)="A"
KeyName(31)="S"
KeyName(32)="D"
KeyName(33)="F"
KeyName(34)="G"
KeyName(35)="H"
KeyName(36)="J"
KeyName(37)="K"
KeyName(38)="L"
KeyName(39)=";"
KeyName(40)="'"
KeyName(42)="Left Shift"
KeyName(43)="\"
KeyName(44)="Z"
KeyName(45)="X"
KeyName(46)="C"
KeyName(47)="V"
KeyName(48)="B"
KeyName(49)="N"
KeyName(50)="M"
KeyName(51)=","
KeyName(52)="."
KeyName(54)="Right Shift"
KeyName(56)="Left Alt"
KeyName(57)="Space"
KeyName(58)="Caps Lock"
KeyName(59)="F1"
KeyName(60)="F2"
KeyName(61)="F3"
KeyName(62)="F4"
KeyName(63)="F5"
KeyName(64)="F6"
KeyName(65)="F7"
KeyName(66)="F8"
KeyName(67)="F9"
KeyName(68)="F10"
KeyName(157)="Right Control"
KeyName(184)="Right Alt"
KeyName(200)="Up"
KeyName(203)="Left"
KeyName(205)="Right"
KeyName(208)="Down"
;[End Block]

Function UpdateMainMenu()
	Local x%, y%, width%, height%, temp%
	
	Color 0,0,0
	Rect 0,0,GraphicWidth,GraphicHeight,True
	
	ShowPointer()
	
	DrawImage(MenuBack, 0, 0)
	
	;If (MilliSecs() Mod MenuBlinkTimer(0)) >= Rand(MenuBlinkDuration(0)) Then
	DrawImage(Menu173, GraphicWidth - ImageWidth(Menu173), GraphicHeight - ImageHeight(Menu173))
	;EndIf
	
	;If Rand(300) = 1 Then
	;	MenuBlinkTimer(0) = Rand(4000, 8000)
	;	MenuBlinkDuration(0) = Rand(200, 500)
	;End If
	
	SetFont Font1
	
	;MenuBlinkTimer(1)=MenuBlinkTimer(1)-FPSfactor
	;If MenuBlinkTimer(1) < MenuBlinkDuration(1) Then
	;	Color(50, 50, 50)
	;	Text(MenuStrX + Rand(-5, 5), MenuStrY + Rand(-5, 5), MenuStr, True)
	;	If MenuBlinkTimer(1) < 0 Then
	;		MenuBlinkTimer(1) = Rand(700, 800)
	;		MenuBlinkDuration(1) = Rand(10, 35)
	;		MenuStrX = Rand(700, 1000) * MenuScale
	;		MenuStrY = Rand(100, 600) * MenuScale
	;		
	;		Select Rand(0, 22)
	;			Case 0, 2, 3
	;				MenuStr = "DON'T BLINK"
	;			Case 4, 5
	;				MenuStr = "Secure. Contain. Protect."
	;			Case 6, 7, 8
	;				MenuStr = "You want happy endings? Fuck you."
	;			Case 9, 10, 11
	;				MenuStr = "Sometimes we would have had time to scream."
	;			Case 12, 19
	;				MenuStr = "NIL"
	;			Case 13
	;				MenuStr = "NO"
	;			Case 14
	;				MenuStr = "black white black white black white gray"
	;			Case 15
	;				MenuStr = "Stone does not care"
	;			Case 16
	;				MenuStr = "9341"
	;			Case 17
	;				MenuStr = "It controls the doors"
	;			Case 18
	;				MenuStr = "e8m106]af173o+079m895w914"
	;			Case 20
	;				MenuStr = "It has taken over everything"
	;			Case 21
	;				MenuStr = "The spiral is growing"
	;			Case 22
	;				MenuStr = "''Some kind of gestalt effect due to massive reality damage''"
	;		End Select
	;	EndIf
	;EndIf
	
	SetFont Font2
	
	DrawImage(MenuText, GraphicWidth / 2 - ImageWidth(MenuText) / 2, GraphicHeight - 20 * MenuScale - ImageHeight(MenuText))
	
	If GraphicWidth > 1240 * MenuScale Then
		DrawTiledImageRect(MenuWhite, 0, 5, 512, 7 * MenuScale, 985.0 * MenuScale, 407.0 * MenuScale, (GraphicWidth - 1240 * MenuScale) + 300, 7 * MenuScale)
	EndIf
	
	If MainMenuTab = 0 Then
		For i% = 0 To 4
			temp = False
			x = 159 * MenuScale
			y = (286 + 100 * i) * MenuScale
			
			width = 400 * MenuScale
			height = 70 * MenuScale
			
			temp = (MouseHit1 And MouseOn(x, y, width, height))
			
			Local txt$
			Select i
				Case 0
					txt = Lang_Replace("NEW GAME")
					RandomSeed = ""
					If temp Then 
						If Rand(15)=1 Then 
							Select Rand(10)
								Case 1 
									RandomSeed = "NIL"
								Case 2
									RandomSeed = "NO"
								Case 3
									RandomSeed = "d9341"
								Case 4
									RandomSeed = "5CP_I73"
								Case 5
									RandomSeed = "DONTBLINK"
								Case 6
									RandomSeed = "CRUNCH"
								Case 7
									RandomSeed = "die"
								Case 8
									RandomSeed = "HTAED"
								Case 9
									RandomSeed = "rustledjim"
								Case 10
									RandomSeed = "larry"
							End Select
						Else
							n = Rand(4,8)
							For i = 1 To n
								If Rand(3)=1 Then
									RandomSeed = RandomSeed + Rand(0,9)
								Else
									RandomSeed = RandomSeed + Chr(Rand(97,122))
								EndIf
							Next							
						EndIf
						
						;RandomSeed = MilliSecs()
						MainMenuTab = 1
					EndIf
				Case 1
					txt = Lang_Replace("LOAD GAME")
					If temp Then
						LoadSaveGames()
						MainMenuTab = 2
					EndIf
				Case 2
					txt = Lang_Replace("OPTIONS")
					;If temp Then MainMenuTab = 3
					If temp Then MainMenuTab = 6
				Case 3
					txt = Lang_Replace("ACHIEVEMENTS")
					If temp Then MainMenuTab = 10
				Case 4
					txt = Lang_Replace("QUIT")
					If temp Then
						DeInitExt
						End
					EndIf
			End Select
			
			DrawButton(x, y, width, height, txt)
			
			;rect(x + 4, y + 4, width - 8, height - 8)
			;color 255, 255, 255	
			;text(x + width / 2, y + height / 2, Str, True, True)
		Next	
		
	Else
		
		x = 159 * MenuScale
		y = 286 * MenuScale
		
		width = 400 * MenuScale
		height = 70 * MenuScale
		
		DrawFrame(x, y, width, height)
		
		If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Lang_Replace("BACK"), False) Then 
			Select MainMenuTab
				Case 3,6,7,8,9 ;save the options
					PutINIValue(OptionFile, "options", "music volume", MusicVolume)
					PutINIValue(OptionFile, "options", "mouse sensitivity", MouseSens)
					PutINIValue(OptionFile, "options", "invert mouse y", InvertMouse)
					PutINIValue(OptionFile, "options", "bump mapping enabled", BumpEnabled)			
					PutINIValue(OptionFile, "options", "HUD enabled", HUDenabled)
					PutINIValue(OptionFile, "options", "screengamma", ScreenGamma)
					
					PutINIValue(OptionFile, "options", "Right key", KEY_RIGHT)
					PutINIValue(OptionFile, "options", "Left key", KEY_LEFT)
					PutINIValue(OptionFile, "options", "Up key", KEY_UP)
					PutINIValue(OptionFile, "options", "Down key", KEY_DOWN)
					PutINIValue(OptionFile, "options", "Blink key", KEY_BLINK)
					PutINIValue(OptionFile, "options", "Sprint key", KEY_SPRINT)
					PutINIValue(OptionFile, "options", "Inventory key", KEY_INV)
					PutINIValue(OptionFile, "options", "Crouch key", KEY_CROUCH)
					
					PutINIValue("NineTailedFoxMod\options.ini", "options", "disable aim cross", NTF_DisableAimCross)
					PutINIValue("NineTailedFoxMod\options.ini", "console", "disable opening", NTF_DisableConsoleOpening)
					PutINIValue("NineTailedFoxMod\options.ini", "options", "reload key", KEY_RELOAD)
					PutINIValue("NineTailedFoxMod\options.ini", "options", "chat key", KEY_CHAT)
					PutINIValue("NineTailedFoxMod\options.ini", "options", "togglegun key", KEY_TOGGLEGUN)
					PutINIValue("NineTailedFoxMod\options.ini", "options", "radiotoggle key", KEY_RADIOTOGGLE)
					PutINIValue("NineTailedFoxMod\options.ini", "ingame", "texture details", NTF_LODMode)
					PutINIValue("NineTailedFoxMod\options.ini", "ingame", "seperate zones", NTF_ZoneLoading)
					PutINIValue("NineTailedFoxMod\options.ini", "ingame", "sfx volume", SFXVolume)
					PutINIValue("NineTailedFoxMod\options.ini", "ingame", "gun sfx pitch",GunPitchShift)
					PutINIValue("NineTailedFoxMod\options.ini", "ingame", "lq models",NTF_LQModels)
					PutINIValue("NineTailedFoxMod\options.ini", "options", "current language",Left(SelectedLangString$,Len(SelectedLangString$)-5))
					
					PutINIValue(OptionFile, "options", "framelimit", Framelimit)
					PutINIValue(OptionFile, "options", "vsync", Vsync)
					PutINIValue(OptionFile, "options", "achievement popup enabled", AchvMSGenabled)
					PutINIValue(OptionFile, "options", "show FPS", ShowFPS)
					PutINIValue(OptionFile, "options", "antialias", NTF_Antialias)
					MainMenuTab = 0
				Case 4
					MainMenuTab = 1
					MouseHit1 = False
				Case 5
					MainMenuTab = 3
					MouseHit1 = False
				Case 10
					MainMenuTab = 0
					NTF_AchvMenuScroll# = 0.0
				Case 11
					MainMenuTab = 9
					NTF_AchvMenuScroll# = 0.0
				Default
					MainMenuTab = 0
			End Select
		EndIf
		
		Select MainMenuTab
			Case 10 ;Achievements
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("ACHIEVEMENTS"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 480 * MenuScale
				
				DrawFrame(x, y, width, height)
				SetBuffer ImageBuffer(NTF_AchvIMG%)
				DrawFrame(x, y, width, height)
				
				y = y + 20 * MenuScale
				
				width = 520 * MenuScale
				height = 92 * MenuScale
				x = 190 * MenuScale
				
				For i = 0 To MAXACHIEVEMENTS-1
					If ((y+3*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1)))) > (250*MenuScale) And ((y+3*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1)))) < (900*MenuScale)
						DrawFrame(x, y+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1))), width, height)
						If Achievements(i)=True
							DrawImage AchvIMG(i),x+3*MenuScale,(y+3*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1)))
							Color 255,255,255
							SetFont Font5%
							Text x+100*MenuScale,(y+23*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1))),AchievementStrings(i)
							Color 200,200,200
							SetFont Font1%
							Text x+100*MenuScale,(y+59*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1))),AchievementDescs(i)
						Else
							DrawImage AchvLocked,x+3*MenuScale,(y+3*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1)))
							Color 255,55,55
							SetFont Font5%
							Text x+100*MenuScale,(y+23*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1))),Lang_Replace("LOCKED")
							If AchvProgress(i)>0
								Color 200,200,200
								SetFont Font1%
								Text x+100*MenuScale,(y+59*MenuScale)+(i*97)-(NTF_AchvMenuScroll#*(90*(MAXACHIEVEMENTS-1))),Lang_Replace("Progress: ")+AchvProgress(i)
							EndIf
						EndIf
					EndIf
				Next
				SetBuffer BackBuffer()
				
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 480 * MenuScale
				
				DrawImageRect(NTF_AchvIMG%, x+3*MenuScale, y+3*menuScale, x+3*MenuScale, y+3*MenuScale, width-6*MenuScale, height-6*MenuScale)
				
				x = 720 * MenuScale
				y = 400 * MenuScale
				width = 30*MenuScale
				height = 460*MenuScale
				Local ScrollMenuHeight# = height * ((height / 30) / Max(MAXACHIEVEMENTS, height / 30))
				
				NTF_AchvMenuScroll# = DrawScrollBar(x,y,width,height,x, y + ((height - ScrollMenuHeight) * NTF_AchvMenuScroll#)*MenuScale,20*MenuScale,ScrollMenuHeight,NTF_AchvMenuScroll#,1)
				
				
			Case 6 ;---------------------- graphics ------------------------------------------------
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("OPTIONS"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)
				If DrawButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("GRAPHICS"), False) Then MainMenuTab = 6
				If DrawButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("AUDIO"), False) Then MainMenuTab = 7
				If DrawButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("CONTROLS"), False) Then MainMenuTab = 8
				If DrawButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("ADVANCED"), False) Then MainMenuTab = 9
				Color 0,255,0
				Rect x+20*MenuScale,y+15*MenuScale,width/5,height/2,False
				SetFont Font1
				
				y=y+70*MenuScale
				
				height = 370 * MenuScale
				DrawFrame(x, y, width, height)
				
				y=y+20*MenuScale
				
				Local prevGamma# = ScreenGamma
				ScreenGamma = (SlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, ScreenGamma*50.0)/50.0)
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Screen gamma"))
				Text (x + 20 * MenuScale, y + 15 * MenuScale, Lang_Replace("(fullscreen mode only)"))
				
				If prevGamma<>ScreenGamma Then
					UpdateScreenGamma()
				EndIf
				
				y=y+40*MenuScale
				
				Color 255,255,255				
				Text (x + 20 * MenuScale, y, Lang_Replace("Enable HUD:"))	
				HUDenabled = DrawTick(x + 310 * MenuScale, y + MenuScale, HUDenabled)	
				
				y=y+30*MenuScale
				
				Color 255,255,255				
				Text (x + 20 * MenuScale, y, Lang_Replace("Enable bump mapping:"))	
				BumpEnabled = DrawTick(x + 310 * MenuScale, y + MenuScale, BumpEnabled)	
				
				y=y+30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Disable aim cross:"))
				NTF_DisableAimCross = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_DisableAimCross%)
				
				y=y+30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Texture details:"))
				DrawImage ArrowIMG(1),x + 310 * MenuScale, y-4*MenuScale
				If MouseHit1
					If ImageRectOverlap(ArrowIMG(1),x + 310 * MenuScale, y-4*MenuScale, MouseX(),MouseY(),0,0)
						If NTF_LODMode% < 3
							NTF_LODMode% = NTF_LODMode% + 1
						Else
							NTF_LODMode% = 0
						EndIf
						PlaySound(ButtonSFX)
					EndIf
				EndIf
				Color 255,255,255
				Select NTF_LODMode%
					Case 0
						Text (x + 340 * MenuScale, y + MenuScale, Lang_Replace("LOW"))
						NTF_LODValue# = 1.5
					Case 1
						Text (x + 340 * MenuScale, y + MenuScale, Lang_Replace("MEDIUM"))
						NTF_LODValue# = 0.75
					Case 2
						Text (x + 340 * MenuScale, y + MenuScale, Lang_Replace("HIGH"))
						NTF_LODValue# = 0.0
					Case 3
						Text (x + 340 * MenuScale, y + MenuScale, Lang_Replace("VERY HIGH"))
						NTF_LODValue# = -0.75
				End Select
				
				y=y+30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Separate zone loading:"))
				NTF_ZoneLoading = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_ZoneLoading)
				
				y=y+30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Framelimit:"))
				CurrFrameLimit# = (SlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, CurrFrameLimit#*50.0)/50.0)
				FrameLimit% = CurrFrameLimit#*200.0
				Color 255,255,0
				If FrameLimit% = 0
					Text (x + 20 * MenuScale, y + 15 * MenuScale, Lang_Replace("Disabled"))
				Else
					Text (x + 20 * MenuScale, y + 15 * MenuScale, FrameLimit%+" FPS")
				EndIf
				Color 255,255,255
				Text (x + 20 * MenuScale, y + 30 * MenuScale, Lang_Replace("Disable:"))
				If DrawTick(x + 120 * MenuScale, y + 30 * MenuScale, 0) Then CurrFrameLimit# = 0.0
				
				y=y+60*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("VSync:"))
				Vsync% = DrawTick(x + 310 * MenuScale, y + MenuScale, Vsync%)
				
				y=y+30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Antialias:"))
				NTF_Antialias = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_Antialias%)
				AntiAlias NTF_Antialias
				Text (x + 20 * MenuScale, y + 15 * MenuScale, Lang_Replace("(fullscreen mode only)"))
				
				y=y+40*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Low quality models:"))
				NTF_LQModels% = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_LQModels%)
			Case 7 ;---------------------- sounds ------------------------------------------------
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("OPTIONS"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)
				If DrawButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("GRAPHICS"), False) Then MainMenuTab = 6
				If DrawButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("AUDIO"), False) Then MainMenuTab = 7
				If DrawButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("CONTROLS"), False) Then MainMenuTab = 8
				If DrawButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("ADVANCED"), False) Then MainMenuTab = 9
				Color 0,255,0
				Rect x+160*MenuScale,y+15*MenuScale,width/5,height/2,False
				SetFont Font1
				
				y = y + 70 * MenuScale
				height = 120 * MenuScale
				DrawFrame(x, y, width, height)	
				
				y = y + 20*MenuScale
				
				MusicVolume = (SlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, MusicVolume*100.0)/100.0)
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Music volume:"))
				
				y = y + 30*MenuScale
				
				SFXVolume = (SlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, SFXVolume*100.0)/100.0)
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Sound volume:"))
				If MouseDown1 Then
					If MouseX() >= x And MouseX() <= x + width + 14 And MouseY() >= y And MouseY() <= y + 20 Then
						PlayTestSound(True)
					Else
						PlayTestSound(False)
					EndIf
				Else
					PlayTestSound(False)
				EndIf
				
				y = y + 30*MenuScale
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Shoot SFX pitch shifting:"))
				GunPitchShift% = DrawTick(x + 310 * MenuScale, y + MenuScale, GunPitchShift%)
				
			Case 8 ;---------------------- controls ------------------------------------------------
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("OPTIONS"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)
				If DrawButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("GRAPHICS"), False) Then MainMenuTab = 6
				If DrawButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("AUDIO"), False) Then MainMenuTab = 7
				If DrawButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("CONTROLS"), False) Then MainMenuTab = 8
				If DrawButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("ADVANCED"), False) Then MainMenuTab = 9
				Color 0,255,0
				Rect x+300*MenuScale,y+15*MenuScale,width/5,height/2,False
				SetFont Font1
				
				y = y + 70 * MenuScale
				height = 260 * MenuScale
				DrawFrame(x, y, width, height)	
				
				y = y + 20*MenuScale
				
				MouseSens = (SlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (MouseSens+0.5)*100.0)/100.0)-0.5
				Color(255, 255, 255)
				Text (x + 20 * MenuScale, y, Lang_Replace("Mouse sensitivity:"))
				
				y = y + 30*MenuScale
				
				Color(255, 255, 255)
				Text (x + 20 * MenuScale, y, Lang_Replace("Invert mouse Y-axis:"))
				InvertMouse = DrawTick(x + 310 * MenuScale, y + MenuScale, InvertMouse)
				
				y = y + 30*MenuScale
				Text (x + 20 * MenuScale, y, Lang_Replace("Control configuration:"))
				y = y + 10*MenuScale
				
				Text (x + 20 * MenuScale, y + 20 * MenuScale, Lang_Replace("Up"))
				InputBox(x + 170 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_UP,210)),5)		
				Text (x + 20 * MenuScale, y + 40 * MenuScale, Lang_Replace("Left"))
				InputBox(x + 170 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_LEFT,210)),3)	
				Text (x + 20 * MenuScale, y + 60 * MenuScale, Lang_Replace("Down"))
				InputBox(x + 170 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_DOWN,210)),6)				
				Text (x + 20 * MenuScale, y + 80 * MenuScale, Lang_Replace("Right"))
				InputBox(x + 170 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_RIGHT,210)),4)	
				
				Text (x + 300 * MenuScale, y + 20 * MenuScale, Lang_Replace("Blink"))
				InputBox(x + 450 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_BLINK,210)),7)				
				Text (x + 300 * MenuScale, y + 40 * MenuScale, Lang_Replace("Sprint"))
				InputBox(x + 450 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_SPRINT,210)),8)
				Text (x + 300 * MenuScale, y + 60 * MenuScale, Lang_Replace("Inventory"))
				InputBox(x + 450 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_INV,210)),9)
				Text (x + 300 * MenuScale, y + 80 * MenuScale, Lang_Replace("Crouch"))
				InputBox(x + 450 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_CROUCH,210)),10)
				
				Text (x + 20 * MenuScale, y + 100 * MenuScale, Lang_Replace("Chat"))
				InputBox(x + 170 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_CHAT,210)),11)
				Text (x + 300 * MenuScale, y + 100 * MenuScale, Lang_Replace("Reload"))
				InputBox(x + 450 * MenuScale, y + 100 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_RELOAD,210)),12)
				Text (x + 20 * MenuScale, y + 120 * MenuScale, Lang_Replace("Toggle gun"))
				InputBox(x + 170 * MenuScale, y + 120 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_TOGGLEGUN,210)),13)
				Text (x + 300 * MenuScale, y + 120 * MenuScale, Lang_Replace("Toggle radio"))
				InputBox(x + 450 * MenuScale, y + 120 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_RADIOTOGGLE,210)),14)
				
				For i = 0 To 227
					If KeyHit(i) Then key = i : Exit
				Next
				If key<>0 Then
					Select SelectedInputBox
						Case 3
							KEY_LEFT = key
						Case 4
							KEY_RIGHT = key
						Case 5
							KEY_UP = key
						Case 6
							KEY_DOWN = key
						Case 7
							KEY_BLINK = key
						Case 8
							KEY_SPRINT = key
						Case 9
							KEY_INV = key
						Case 10
							KEY_CROUCH = key
						Case 11
							KEY_CHAT = key
						Case 12
							KEY_RELOAD = key
						Case 13
							KEY_TOGGLEGUN = key
						Case 14
							KEY_RADIOTOGGLE = key
					End Select
					SelectedInputBox = 0
				EndIf
			Case 9 ;---------------------- advanced ------------------------------------------------
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("OPTIONS"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)
				If DrawButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("GRAPHICS"), False) Then MainMenuTab = 6
				If DrawButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("AUDIO"), False) Then MainMenuTab = 7
				If DrawButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("CONTROLS"), False) Then MainMenuTab = 8
				If DrawButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, Lang_Replace("ADVANCED"), False) Then MainMenuTab = 9
				Color 0,255,0
				Rect x+440*MenuScale,y+15*MenuScale,width/5,height/2,False
				SetFont Font1
				
				y = y + 70 * MenuScale
				height = 220 * MenuScale
				DrawFrame(x, y, width, height)	
				
				y = y + 20*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Disable console auto-opening:"))
				NTF_DisableConsoleOpening = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_DisableConsoleOpening)
				
				y = y + 30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Enable achievement popup:"))
				AchvMSGenabled% = DrawTick(x + 310 * MenuScale, y + MenuScale, AchvMSGenabled%)
				
				y = y + 30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Show FPS:"))
				ShowFPS% = DrawTick(x + 310 * MenuScale, y + MenuScale, ShowFPS%)
				
				y = y + 30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, Lang_Replace("Selected language:"))
				Text (x + 340 * MenuScale, y, LANG_Name$(SelectedLang%))
				DrawImage ArrowIMG(2),x + 310 * MenuScale, y-4*MenuScale
				If MouseHit1
					If ImageRectOverlap(ArrowIMG(2),x + 310 * MenuScale, y-4*MenuScale, MouseX(),MouseY(),0,0)
						MainMenuTab = 11
						PlaySound(ButtonSFX)
					EndIf
				EndIf
			Case 11 ;---------------------- language selection ------------------------------------------------
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("LANGUAGES"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 480 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				y = y + 20 * MenuScale
				
				width = 520 * MenuScale
				height = 92 * MenuScale
				x = 315 * MenuScale
				
				For i = 0 To LanguageAmount%
					If LANG$(i) <> SelectedLangString$
						Color 255,255,255
					Else
						Color 0,255,0
					EndIf
					SetFont Font5%
					Text x,y,LANG_Name$(i),1,1
					
					If RectsOverlap(x-125*MenuScale,y-10*MenuScale,250*MenuScale,24*MenuScale,MouseX(),MouseY(),0,0)
						Color 255,255,255
						Rect(x-125*MenuScale,y-10*MenuScale,250*MenuScale,24*MenuScale,False)
					EndIf
					
					If MouseHit1
						If RectsOverlap(x-125*MenuScale,y-10*MenuScale,250*MenuScale,24*MenuScale,MouseX(),MouseY(),0,0)
							If LANG$(i) <> SelectedLangString$
								Lang_Change(i)
							EndIf
							PlaySound(ButtonSFX)
						EndIf
					EndIf
					
					y=y+25*MenuScale
					If y >= 845 Then y = (286 + 110)*MenuScale : x=x+200*MenuScale
				Next
			Case 5
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, "OPTIONS - NTF", True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 130 * MenuScale
				DrawFrame(x, y, width, height)
				SetFont Font1
				
				;---------------------- graphics ------------------------------------------------
				
				y=y+20*MenuScale
				
				;Local prevGamma# = ScreenGamma
				;ScreenGamma = (SlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, ScreenGamma*50.0)/50.0)
				;Color 255,255,255
				;Text (x + 20 * MenuScale, y, "Screen gamma")
				;Text (x + 20 * MenuScale, y + 15 * MenuScale, "(fullscreen mode only)")
				
				;If prevGamma<>ScreenGamma Then
				;	UpdateScreenGamma()
				;EndIf
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, "Disable Aim Cross:")
				NTF_DisableAimCross = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_DisableAimCross%)
				
				y=y+40*MenuScale
				
				;Color 255,255,255				
				;Text (x + 20 * MenuScale, y, "Enable HUD:")	
				;HUDenabled = DrawTick(x + 310 * MenuScale, y + MenuScale, HUDenabled)	
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, "Texture Details:")
				;NTF_LODMode% = (SlideBar(x + 310 * MenuScale, y-4*MenuScale, 150*MenuScale, NTF_LODMode%)/100.0)
				DrawImage ArrowIMG(1),x + 310 * MenuScale, y-4*MenuScale
				If MouseHit1
					If ImageRectOverlap(ArrowIMG(1),x + 310 * MenuScale, y-4*MenuScale, MouseX(),MouseY(),0,0)
						If NTF_LODMode% < 3
							NTF_LODMode% = NTF_LODMode% + 1
						Else
							NTF_LODMode% = 0
						EndIf
						PlaySound(ButtonSFX)
					EndIf
				EndIf
				Color 255,255,255
				Select NTF_LODMode%
					Case 0
						Text (x + 340 * MenuScale, y + MenuScale, "Low")
						NTF_LODValue# = 1.5
					Case 1
						Text (x + 340 * MenuScale, y + MenuScale, "Medium")
						NTF_LODValue# = 0.75
					Case 2
						Text (x + 340 * MenuScale, y + MenuScale, "High")
						NTF_LODValue# = 0.0
					Case 3
						Text (x + 340 * MenuScale, y + MenuScale, "Very High")
						NTF_LODValue# = -0.75
				End Select
				
				y=y+30*MenuScale
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, "Seperate Zone Loading:")
				NTF_ZoneLoading = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_ZoneLoading)
				
				;Color 255,255,255				
				;Text (x + 20 * MenuScale, y, "Enable bump mapping:")	
				;BumpEnabled = DrawTick(x + 310 * MenuScale, y + MenuScale, BumpEnabled)	
				
				;---------------------- console ------------------------------------------------
				
				y = y + 50 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)	
				
				y = y + 20*MenuScale
				
				;MusicVolume = (SlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, MusicVolume*100.0)/100.0)
				;Color 255,255,255
				;Text (x + 20 * MenuScale, y, "Music volume:")	
				
				Color 255,255,255
				Text (x + 20 * MenuScale, y, "Disable Console Auto-Opening:")
				NTF_DisableConsoleOpening = DrawTick(x + 310 * MenuScale, y + MenuScale, NTF_DisableConsoleOpening)
				
				;---------------------- controls ------------------------------------------------
				
				y = y + 50 * MenuScale
				height = 220 * MenuScale
				DrawFrame(x, y, width, height)	
				
				y = y + 20*MenuScale
				
				;y = y + 30*MenuScale
				Text (x + 20 * MenuScale, y, "Control configuration:")	
				y = y + 10*MenuScale
				
				Text (x + 20 * MenuScale, y + 20 * MenuScale, "Reload")
				InputBox(x + 170 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_RELOAD,210)),5)		
				Text (x + 20 * MenuScale, y + 40 * MenuScale, "Use Chat")
				InputBox(x + 170 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_CHAT,210)),3)	
				Text (x + 20 * MenuScale, y + 60 * MenuScale, "Toggle Guns")
				InputBox(x + 170 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_TOGGLEGUN,210)),6)				
				;Text (x + 20 * MenuScale, y + 80 * MenuScale, "Right")
				;InputBox(x + 170 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_RIGHT,210)),4)	
				
				;Text (x + 300 * MenuScale, y + 20 * MenuScale, "Blink")
				;InputBox(x + 450 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_BLINK,210)),7)				
				;Text (x + 300 * MenuScale, y + 40 * MenuScale, "Sprint")
				;InputBox(x + 450 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_SPRINT,210)),8)
				;Text (x + 300 * MenuScale, y + 60 * MenuScale, "Inventory")
				;InputBox(x + 450 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_INV,210)),9)
				;Text (x + 300 * MenuScale, y + 80 * MenuScale, "Crouch")
				;InputBox(x + 450 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_CROUCH,210)),10)
				
				For i = 0 To 227
					If KeyHit(i) Then key = i : Exit
				Next
				If key<>0 Then
					Select SelectedInputBox
						Case 3
							KEY_CHAT = key
				;		Case 4
				;			KEY_RIGHT = key
						Case 5
							KEY_RELOAD = key
						Case 6
							KEY_TOGGLEGUN = key
				;		Case 7
				;			KEY_BLINK = key
				;		Case 8
				;			KEY_SPRINT = key
				;		Case 9
				;			KEY_INV = key
				;		Case 10
				;			KEY_CROUCH = key
					End Select
					SelectedInputBox = 0
				EndIf 
				
				;MouseSens = (SlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (MouseSens+0.5)*100.0)/100.0)-0.5
				;Color(255, 255, 255)
				;Text (x + 20 * MenuScale, y, "Mouse sensitivity:")				
				
				y = y + 30*MenuScale
				
				;Color(255, 255, 255)
				;Text (x + 20 * MenuScale, y, "Invert mouse Y-axis:")
				;InvertMouse = DrawTick(x + 310 * MenuScale, y + MenuScale, InvertMouse)
				
				;y = y + 30*MenuScale
				;Text (x + 20 * MenuScale, y, "Control configuration:")	
				;y = y + 10*MenuScale
				
				;Text (x + 20 * MenuScale, y + 20 * MenuScale, "Up")
				;InputBox(x + 170 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_UP,210)),5)		
				;Text (x + 20 * MenuScale, y + 40 * MenuScale, "Left")
				;InputBox(x + 170 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_LEFT,210)),3)	
				;Text (x + 20 * MenuScale, y + 60 * MenuScale, "Down")
				;InputBox(x + 170 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_DOWN,210)),6)				
				;Text (x + 20 * MenuScale, y + 80 * MenuScale, "Right")
				;InputBox(x + 170 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_RIGHT,210)),4)	
				
				;Text (x + 300 * MenuScale, y + 20 * MenuScale, "Blink")
				;InputBox(x + 450 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_BLINK,210)),7)				
				;Text (x + 300 * MenuScale, y + 40 * MenuScale, "Sprint")
				;InputBox(x + 450 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_SPRINT,210)),8)
				;Text (x + 300 * MenuScale, y + 60 * MenuScale, "Inventory")
				;InputBox(x + 450 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_INV,210)),9)
				;Text (x + 300 * MenuScale, y + 80 * MenuScale, "Crouch")
				;InputBox(x + 450 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_CROUCH,210)),10)
				
				;For i = 0 To 227
				;	If KeyHit(i) Then key = i : Exit
				;Next
				;If key<>0 Then
				;	Select SelectedInputBox
				;		Case 3
				;			KEY_LEFT = key
				;		Case 4
				;			KEY_RIGHT = key
				;		Case 5
				;			KEY_UP = key
				;		Case 6
				;			KEY_DOWN = key
				;		Case 7
				;			KEY_BLINK = key
				;		Case 8
				;			KEY_SPRINT = key
				;		Case 9
				;			KEY_INV = key
				;		Case 10
				;			KEY_CROUCH = key
				;	End Select
				;	SelectedInputBox = 0
				;EndIf 
			Case 1 ; New game
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("NEW GAME"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 330 * MenuScale
				
				DrawFrame(x, y, width, height)				
				
				SetFont Font1
				
				Text (x + 20 * MenuScale, y + 20 * MenuScale, Lang_Replace("Name:"))
				CurrSave = InputBox(x + 150 * MenuScale, y + 15 * MenuScale, 200 * MenuScale, 30 * MenuScale, CurrSave, 1)
				CurrSave = Left(CurrSave, 15)
				
				Color 255,255,255
				If SelectedMap = "" Then
					Text (x + 20 * MenuScale, y + 60 * MenuScale, Lang_Replace("Map seed:"))
					RandomSeed = Left(InputBox(x+150*MenuScale, y+55*MenuScale, 200*MenuScale, 30*MenuScale, RandomSeed, 3),15)	
				Else
					Text (x + 20 * MenuScale, y + 60 * MenuScale, Lang_Replace("Selected map:"))
					Color (255, 255, 255)
					Rect(x+150*MenuScale, y+55*MenuScale, 200*MenuScale, 30*MenuScale)
					Color (0, 0, 0)
					Rect(x+150*MenuScale+2, y+55*MenuScale+2, 200*MenuScale-4, 30*MenuScale-4)
					
					Color (255, 0,0)
					Text(x+150*MenuScale + 100*MenuScale, y+55*MenuScale + 15*MenuScale, SelectedMap, True, True)
					
					If DrawButton(x+370*MenuScale, y+55*MenuScale, 120*MenuScale, 30*MenuScale, Lang_Replace("Deselect"), False) Then
						SelectedMap=""
					EndIf
				EndIf	
				
				Text(x + 20 * MenuScale, y + 110 * MenuScale, Lang_Replace("Enable intro sequence:"))
				IntroEnabled = DrawTick(x + 280 * MenuScale, y + 110 * MenuScale, IntroEnabled)	
				
				;Local modeName$, modeDescription$, selectedDescription$
				Text (x + 20 * MenuScale, y + 150 * MenuScale, Lang_Replace("Difficulty:"))
				For i = SAFE To CUSTOM
					If DrawTick(x + 20 * MenuScale, y + (180+30*i) * MenuScale, (SelectedDifficulty = difficulties(i))) Then SelectedDifficulty = difficulties(i)
					
					Text(x + 60 * MenuScale, y + (180+30*i) * MenuScale, difficulties(i)\name)
				Next
				
				Color(255, 255, 255)
				DrawFrame(x + 150 * MenuScale,y + 155 * MenuScale, 410*MenuScale, 150*MenuScale)
				
				If SelectedDifficulty\customizable Then
					SelectedDifficulty\permaDeath =  DrawTick(x + 160 * MenuScale, y + 165 * MenuScale, (SelectedDifficulty\permaDeath))
					Text(x + 200 * MenuScale, y + 165 * MenuScale, Lang_Replace("Permadeath"))
					
					If DrawTick(x + 160 * MenuScale, y + 195 * MenuScale, SelectedDifficulty\saveType = SAVEANYWHERE And (Not SelectedDifficulty\permaDeath), SelectedDifficulty\permaDeath) Then 
						SelectedDifficulty\saveType = SAVEANYWHERE
					Else
						SelectedDifficulty\saveType = SAVEONSCREENS
					EndIf
					
					Text(x + 200 * MenuScale, y + 195 * MenuScale, Lang_Replace("Save anywhere"))	
					
					SelectedDifficulty\aggressiveNPCs =  DrawTick(x + 160 * MenuScale, y + 225 * MenuScale, SelectedDifficulty\aggressiveNPCs)
					Text(x + 200 * MenuScale, y + 225 * MenuScale, Lang_Replace("Aggressive NPCs"))
					
				Else
					RowText(SelectedDifficulty\description, x+160*MenuScale, y+160*MenuScale, (410-20)*MenuScale, 200)					
				EndIf
				
				If DrawButton(x, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, Lang_Replace("Load map"), False) Then
					MainMenuTab = 4
					LoadSavedMaps()
				EndIf
				
				SetFont Font2
				
				If DrawButton(x + 420 * MenuScale, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, Lang_Replace("START"), False) Then
					If CurrSave <> "" Then
						If RandomSeed = "" Then
							RandomSeed = Abs(MilliSecs())
						EndIf
						Local strtemp$ = ""
						For i = 1 To Len(RandomSeed)
							strtemp = strtemp+Asc(Mid(strtemp,i,1))
						Next
						SeedRnd Abs(Int(strtemp))
						
						Local SameFound% = False
						For  i% = 1 To SaveGameAmount
							If SaveGames(i - 1) = CurrSave Then SameFound=SameFound+1
						Next
						
						If SameFound > 0 Then CurrSave = CurrSave + " (" + (SameFound + 1) + ")"
						
						LoadEntities()
						InitNewGame()
						MainMenuOpen = False
						FlushKeys()
						FlushMouse()
					Else
						
					End If
					
				EndIf
				
			Case 2 ;load game
				
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 300 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("LOAD GAME"), True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				SetFont Font1	
				
				If SaveGameAmount = 0 Then
					Text (x + 20 * MenuScale, y + 20 * MenuScale, Lang_Replace("No saved games"))
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					For i% = 1 To SaveGameAmount
						DrawFrame(x,y,540* MenuScale, 70* MenuScale)
						
						Text(x + 20 * MenuScale, y + 10 * MenuScale, SaveGames(i - 1))
						Text(x + 20 * MenuScale, y + (10+23) * MenuScale, SaveGameTime(i - 1))
						Text(x + 120 * MenuScale, y + (10+23) * MenuScale, SaveGameDate(i - 1))
						
						If DrawButton(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, Lang_Replace("Load"), False) Then
							LoadEntities()
							LoadGame(SavePath + SaveGames(i - 1) + "\")
							CurrSave = SaveGames(i - 1)
							InitLoadGame()
							MainMenuOpen = False
						EndIf
						
						If DrawButton(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, Lang_Replace("Delete"), False) Then
							DeleteFile(CurrentDir()+SavePath + SaveGames(i - 1)+"\save.txt")
							DeleteFile(CurrentDir()+SavePath + SaveGames(i - 1)+"\_z1.SZL")
							DeleteFile(CurrentDir()+SavePath + SaveGames(i - 1)+"\_z2.SZL")
							DeleteFile(CurrentDir()+SavePath + SaveGames(i - 1)+"\_z3.SZL")
							DeleteDir(CurrentDir()+SavePath + SaveGames(i - 1))
							LoadSaveGames()
							Exit
						EndIf
						
						y=y+80 * MenuScale
					Next
				EndIf
				
			Case 3 ;options
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, "OPTIONS", True, True)
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 130 * MenuScale
				DrawFrame(x, y, width, height)
				SetFont Font1
				
				;---------------------- graphics ------------------------------------------------
				
				y=y+20*MenuScale
				
				;Local prevGamma# = ScreenGamma
				prevGamma# = ScreenGamma
				ScreenGamma = (SlideBar(x + 310*MenuScale, y+6*MenuScale, 150*MenuScale, ScreenGamma*50.0)/50.0)
				Color 255,255,255
				Text (x + 20 * MenuScale, y, "Screen gamma")
				Text (x + 20 * MenuScale, y + 15 * MenuScale, "(fullscreen mode only)")
				
				If prevGamma<>ScreenGamma Then
					UpdateScreenGamma()
				EndIf
				
				y=y+40*MenuScale
				
				Color 255,255,255				
				Text (x + 20 * MenuScale, y, "Enable HUD:")	
				HUDenabled = DrawTick(x + 310 * MenuScale, y + MenuScale, HUDenabled)	
				
				y=y+30*MenuScale
				
				Color 255,255,255				
				Text (x + 20 * MenuScale, y, "Enable bump mapping:")	
				BumpEnabled = DrawTick(x + 310 * MenuScale, y + MenuScale, BumpEnabled)	
				
				;---------------------- sounds ------------------------------------------------
				
				y = y + 50 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)	
				
				y = y + 20*MenuScale
				
				MusicVolume = (SlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, MusicVolume*100.0)/100.0)
				Color 255,255,255
				Text (x + 20 * MenuScale, y, "Music volume:")	
				
				;---------------------- controls ------------------------------------------------
				
				y = y + 50 * MenuScale
				height = 220 * MenuScale
				DrawFrame(x, y, width, height)	
				
				y = y + 20*MenuScale
				
				MouseSens = (SlideBar(x + 310*MenuScale, y-4*MenuScale, 150*MenuScale, (MouseSens+0.5)*100.0)/100.0)-0.5
				Color(255, 255, 255)
				Text (x + 20 * MenuScale, y, "Mouse sensitivity:")				
				
				y = y + 30*MenuScale
				
				Color(255, 255, 255)
				Text (x + 20 * MenuScale, y, "Invert mouse Y-axis:")
				InvertMouse = DrawTick(x + 310 * MenuScale, y + MenuScale, InvertMouse)
				
				y = y + 30*MenuScale
				Text (x + 20 * MenuScale, y, "Control configuration:")	
				y = y + 10*MenuScale
				
				Text (x + 20 * MenuScale, y + 20 * MenuScale, "Up")
				InputBox(x + 170 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_UP,210)),5)		
				Text (x + 20 * MenuScale, y + 40 * MenuScale, "Left")
				InputBox(x + 170 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_LEFT,210)),3)	
				Text (x + 20 * MenuScale, y + 60 * MenuScale, "Down")
				InputBox(x + 170 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_DOWN,210)),6)				
				Text (x + 20 * MenuScale, y + 80 * MenuScale, "Right")
				InputBox(x + 170 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_RIGHT,210)),4)	
				
				Text (x + 300 * MenuScale, y + 20 * MenuScale, "Blink")
				InputBox(x + 450 * MenuScale, y + 20 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_BLINK,210)),7)				
				Text (x + 300 * MenuScale, y + 40 * MenuScale, "Sprint")
				InputBox(x + 450 * MenuScale, y + 40 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_SPRINT,210)),8)
				Text (x + 300 * MenuScale, y + 60 * MenuScale, "Inventory")
				InputBox(x + 450 * MenuScale, y + 60 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_INV,210)),9)
				Text (x + 300 * MenuScale, y + 80 * MenuScale, "Crouch")
				InputBox(x + 450 * MenuScale, y + 80 * MenuScale,100*MenuScale,20*MenuScale,KeyName(Min(KEY_CROUCH,210)),10)
				
				For i = 0 To 227
					If KeyHit(i) Then key = i : Exit
				Next
				If key<>0 Then
					Select SelectedInputBox
						Case 3
							KEY_LEFT = key
						Case 4
							KEY_RIGHT = key
						Case 5
							KEY_UP = key
						Case 6
							KEY_DOWN = key
						Case 7
							KEY_BLINK = key
						Case 8
							KEY_SPRINT = key
						Case 9
							KEY_INV = key
						Case 10
							KEY_CROUCH = key
					End Select
					SelectedInputBox = 0
				EndIf
				
				y = y + 140 * MenuScale
				height = 60 * MenuScale
				DrawFrame(x, y, width, height)
				
				y = y + 15 * MenuScale
				
				If DrawButton(x + 20 * MenuScale, y , 200 * MenuScale, 30 * MenuScale, "NTF-Mod Options", False)
					MainMenuTab = 5
				EndIf
			Case 4 ; load map
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 159 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont Font2
				Text(x + width / 2, y + height / 2, Lang_Replace("LOAD MAP"), True, True)
				SetFont Font1
				
				x = 160 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				SetFont Font1
				
				If SavedMaps(0)="" Then 
					Text (x + 20 * MenuScale, y + 20 * MenuScale, Lang_Replace("No saved maps. Use the Map Creator to create new maps."))
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					For i = 0 To MAXSAVEDMAPS-1
						If SavedMaps(i)<>"" Then
							
							If DrawButton(x + 20 * MenuScale, y + 20 * MenuScale, 170, 25, SavedMaps(i), False) Then
								SelectedMap=SavedMaps(i)
								MainMenuTab = 1
							EndIf
							
							y=y+30*MenuScale
							If y > (286+230) * MenuScale Then
								y = 286*MenuScale + 2*MenuScale
								x = x+175*MenuScale
							EndIf
						Else
							Exit
						EndIf
					Next
				EndIf
				
				
		End Select
		
	End If
	
	;DrawTiledImageRect(MenuBack, 985 * MenuScale, 860 * MenuScale, 200 * MenuScale, 20 * MenuScale, 1200 * MenuScale, 866 * MenuScale, 300, 20 * MenuScale)
	
	If Fullscreen Then DrawImage CursorIMG, MouseX(),MouseY()
	
	SetFont Font1
End Function

Function UpdateLauncher()
	
	MenuScale = 1
	
	Graphics3DExt(LauncherWidth, LauncherHeight, 0, 2)
	InitExt
	
	SetBuffer BackBuffer()
	
	Font1 = LoadFont_Strict("GFX\cour.ttf", 18, 0,0,0,0, FT_DEFAULT)
	MenuWhite = LoadImage_Strict("GFX\menu\menuwhite.jpg")
	MenuBlack = LoadImage_Strict("GFX\menu\menublack.jpg")	
	MaskImage MenuBlack, 255,255,0
	LauncherIMG = LoadImage_Strict("NineTailedFoxMod\GFX\menu\launcher.jpg")
	ButtonSFX% = LoadSound_Strict("SFX\Button.ogg")
	Local i%	
	
	For i = 0 To 3
		ArrowIMG(i) = LoadImage_Strict("GFX\menu\arrow.png")
		RotateImage(ArrowIMG(i), 90 * i)
		HandleImage(ArrowIMG(i), 0, 0)
	Next
	
	For i% = 1 To TotalGFXModes
		Local samefound% = False
		For  n% = 0 To TotalGFXModes - 1
			If GfxModeWidths(n) = GfxModeWidth(i) And GfxModeHeights(n) = GfxModeHeight(i) Then samefound = True : Exit
		Next
		If samefound = False Then
			If GraphicWidth = GfxModeWidth(i) And GraphicHeight = GfxModeHeight(i) Then SelectedGFXMode = GFXModes
			GfxModeWidths(GFXModes) = GfxModeWidth(i)
			GfxModeHeights(GFXModes) = GfxModeHeight(i)
			GFXModes=GFXModes+1 
		End If
	Next
	
	Repeat
		
		;Cls
		Color 0,0,0
		Rect 0,0,LauncherWidth,LauncherHeight,True
		
		MouseHit1 = MouseHit(1)
		
		Color 255, 255, 255
		DrawImage(LauncherIMG, 0, 0)
		
		Text(20, 240 - 65, "Resolution: ")
		
		Local x% = 40
		Local y% = 280 - 65
		For i = 0 To (GFXModes - 1)
			Color 0, 0, 0
			If SelectedGFXMode = i Then Rect(x - 1, y - 1, 100, 20, False)
			
			Text(x, y, (GfxModeWidths(i) + "x" + GfxModeHeights(i)))
			If MouseOn(x - 1, y - 1, 100, 20) Then
				Color 100, 100, 100
				Rect(x - 1, y - 1, 100, 20, False)
				If MouseHit1 Then SelectedGFXMode = i
			EndIf
			
			y=y+20
			If y >= 240 - 65 + (LauncherHeight - 80 - 260) Then y = 280 - 65 : x=x+100
		Next
		
		;-----------------------------------------------------------------
		Color 255, 255, 255
		x = 30
		y = 369
		Rect(x - 10, y, 340, 95)
		Text(x - 10, y - 25, "Graphics:")
		
		y=y+10
		For i = 1 To CountGfxDrivers()
			Color 0, 0, 0
			If SelectedGFXDriver = i Then Rect(x - 1, y - 1, 290, 20, False)
			;text(x, y, bbGfxDriverName(i))
			LimitText(GfxDriverName(i), x, y, 290)
			If MouseOn(x - 1, y - 1, 290, 20) Then
				Color 100, 100, 100
				Rect(x - 1, y - 1, 290, 20, False)
				If MouseHit1 Then SelectedGFXDriver = i
			EndIf
			
			y=y+20
		Next
		
		Fullscreen = DrawTick(40 + 430, 262 - 55, Fullscreen)
		LauncherEnabled = DrawTick(40 + 430, 260 - 55 + 40, LauncherEnabled)
		
		Color 255, 255, 255
		Text(40 + 430 + 35, 262 - 55, "Fullscreen")
		Text(40 + 430 + 35, 262 - 55 + 40, "Use launcher")
		
		If DrawButton(LauncherWidth - 30 - 90, LauncherHeight - 50 - 55, 100, 30, "LAUNCH", False) Then
			GraphicWidth = GfxModeWidths(SelectedGFXMode)
			GraphicHeight = GfxModeHeights(SelectedGFXMode)
			Exit
		EndIf
		
		If DrawButton(LauncherWidth - 30 - 90, LauncherHeight - 50, 100, 30, "EXIT", False) Then End
		Flip
	Forever
	
	PutINIValue(OptionFile, "options", "width", GfxModeWidths(SelectedGFXMode))
	PutINIValue(OptionFile, "options", "height", GfxModeHeights(SelectedGFXMode))
	If Fullscreen Then
		PutINIValue(OptionFile, "options", "fullscreen", "true")
	Else
		PutINIValue(OptionFile, "options", "fullscreen", "false")
	EndIf
	If LauncherEnabled Then
		PutINIValue(OptionFile, "launcher", "launcher enabled", "true")
	Else
		PutINIValue(OptionFile, "launcher", "launcher enabled", "false")
	EndIf
	
	PutINIValue(OptionFile, "options", "gfx driver", SelectedGFXDriver)
	
End Function


Function DrawTiledImageRect(img%, srcX%, srcY%, srcwidth#, srcheight#, x%, y%, width%, height%)
	
	Local x2% = x
	While x2 < x+width
		Local y2% = y
		While y2 < y+height
			If x2 + srcwidth > x + width Then srcwidth = srcwidth - Max((x2 + srcwidth) - (x + width), 1)
			If y2 + srcheight > y + height Then srcheight = srcheight - Max((y2 + srcheight) - (y + height), 1)
			DrawImageRect(img, x2, y2, srcX, srcY, srcwidth, srcheight)
			y2 = y2 + srcheight
		Wend
		x2 = x2 + srcwidth
	Wend
	
End Function



Type LoadingScreens
	Field imgpath$
	Field img%
	Field ID%
	Field title$
	Field alignx%, aligny%
	Field disablebackground%
	Field txt$[5], txtamount%
End Type

Function InitLoadingScreens(file$)
	Local TemporaryString$, i%
	Local ls.LoadingScreens
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			ls.LoadingScreens = New LoadingScreens
			LoadingScreenAmount=LoadingScreenAmount+1
			ls\ID = LoadingScreenAmount
			
			ls\title = TemporaryString
			ls\imgpath = GetINIString(file, TemporaryString, "image path")
			
			For i = 0 To 4
				ls\txt[i] = GetINIString(file, TemporaryString, "text"+(i+1))
				If ls\txt[i]<> "" Then ls\txtamount=ls\txtamount+1
			Next
			
			ls\disablebackground = GetINIInt(file, TemporaryString, "disablebackground")
			
			Select Lower(GetINIString(file, TemporaryString, "align x"))
				Case "left"
					ls\alignx = -1
				Case "middle", "center"
					ls\alignx = 0
				Case "right" 
					ls\alignx = 1
			End Select 
			
			Select Lower(GetINIString(file, TemporaryString, "align y"))
				Case "top", "up"
					ls\aligny = -1
				Case "middle", "center"
					ls\aligny = 0
				Case "bottom", "down"
					ls\aligny = 1
			End Select 			
			
		EndIf
	Wend
	
	CloseFile f
End Function



Function DrawLoading(percent%, shortloading=False)
	
	Local x%, y%
	
	If percent = 0 Then
		LoadingScreenText=0
		
		temp = Rand(1,LoadingScreenAmount)
		For ls.loadingscreens = Each LoadingScreens
			If ls\id = temp Then
				If ls\img=0 Then ls\img = LoadImage_Strict("Loadingscreens\"+ls\imgpath)
				SelectedLoadingScreen = ls 
				Exit
			EndIf
		Next
	EndIf	
	
	firstloop = True
	Repeat 
		
		Color 0,0,0
		Rect 0,0,GraphicWidth,GraphicHeight,True
		Color 255, 255, 255
		
		;Cls(True,False)
		
		If percent > 20 Then
			UpdateMusic()
		EndIf
		
		If shortloading = False Then
			If percent > (100.0 / SelectedLoadingScreen\txtamount)*(LoadingScreenText+1) Then
				LoadingScreenText=LoadingScreenText+1
			EndIf
		EndIf
		
		If (Not SelectedLoadingScreen\disablebackground) Then
			DrawImage LoadingBack, GraphicWidth/2 - ImageWidth(LoadingBack)/2, GraphicHeight/2 - ImageHeight(LoadingBack)/2
		EndIf	
		
		If SelectedLoadingScreen\alignx = 0 Then
			x = GraphicWidth/2 - ImageWidth(SelectedLoadingScreen\img)/2 
		ElseIf  SelectedLoadingScreen\alignx = 1
			x = GraphicWidth - ImageWidth(SelectedLoadingScreen\img)
		Else
			x = 0
		EndIf
		
		If SelectedLoadingScreen\aligny = 0 Then
			y = GraphicHeight/2 - ImageHeight(SelectedLoadingScreen\img)/2 
		ElseIf  SelectedLoadingScreen\aligny = 1
			y = GraphicHeight - ImageHeight(SelectedLoadingScreen\img)
		Else
			y = 0
		EndIf	
		
		DrawImage SelectedLoadingScreen\img, x, y
		
		Local width% = 300, height% = 20
		x% = GraphicWidth / 2 - width / 2
		y% = GraphicHeight / 2 + 30 - 100
		
		Rect(x, y, width+4, height, False)
		For  i% = 1 To Int((width - 2) * (percent / 100.0) / 10)
			DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
		Next
		
		If SelectedLoadingScreen\title = "CWM" Then
			
			If Not shortloading Then 
				If firstloop Then 
					If percent = 0 Then
						PlaySound LoadTempSound("SFX\cwm1.cwm")
					ElseIf percent = 100
						PlaySound LoadTempSound("SFX\cwm2.cwm")
					EndIf
				EndIf
			EndIf
			
			SetFont Font2
			strtemp$ = ""
			temp = Rand(2,9)
			For i = 0 To temp
				strtemp$ = STRTEMP + Chr(Rand(48,122))
			Next
			Text(GraphicWidth / 2, GraphicHeight / 2 + 80, strtemp, True, True)
			
			If percent = 0 Then 
				Select Rand(10)
					Case 1
						SelectedLoadingScreen\txt[0] ="A very fine radio might prove to be useful"
					Case 2
						SelectedLoadingScreen\txt[0] ="ThIS PLaCE WiLL BUrN"
					Case 3
						SelectedLoadingScreen\txt[0] ="You can't control it"
					Case 4
						SelectedLoadingScreen\txt[0] ="eof9nsd3jue4iwe1fgj"
					Case 5
						SelectedLoadingScreen\txt[0] = "YOU NEED TO TRUST IT"
					Case 6 
						SelectedLoadingScreen\txt[0] = "Look my friend in the eye when you address him, isn't that the way of the gentleman?"
					Case 7
						SelectedLoadingScreen\txt[0] = "???____??_???__????n?"	
					Case 8
						SelectedLoadingScreen\txt[0] = "???????????"
					Case 9,10
						SelectedLoadingScreen\txt[0] = "It will happen on " +CurrentDate()
					Case 11,12
						SelectedLoadingScreen\txt[0] = CurrentTime()
				End Select
			EndIf
			
			strtemp$ = SelectedLoadingScreen\txt[0]
			temp = Int(Len(SelectedLoadingScreen\txt[0])-Rand(5))
			For i = 0 To Rand(10,15);temp
				strtemp$ = Replace(SelectedLoadingScreen\txt[0],Mid(SelectedLoadingScreen\txt[0],Rand(1,Len(strtemp)-1),1),Chr(Rand(130,250)))
			Next		
			SetFont Font1
			RowText(strtemp, GraphicWidth / 2-200, GraphicHeight / 2 +120,400,300,True)		
		Else
			
			Color 0,0,0
			SetFont Font2
			Text(GraphicWidth / 2 + 1, GraphicHeight / 2 + 80 + 1, SelectedLoadingScreen\title, True, True)
			SetFont Font1
			RowText(SelectedLoadingScreen\txt[LoadingScreenText], GraphicWidth / 2-200+1, GraphicHeight / 2 +120+1,400,300,True)
			
			Color 255,255,255
			SetFont Font2
			Text(GraphicWidth / 2, GraphicHeight / 2 +80, SelectedLoadingScreen\title, True, True)
			SetFont Font1
			RowText(SelectedLoadingScreen\txt[LoadingScreenText], GraphicWidth / 2-200, GraphicHeight / 2 +120,400,300,True)
			
		EndIf
		
		Color 0,0,0
		Text(GraphicWidth / 2 + 1, GraphicHeight / 2 - 100 + 1, Lang_Replace("LOADING")+ " - " + percent + " %", True, True)
		Color 255,255,255
		Text(GraphicWidth / 2, GraphicHeight / 2 - 100, Lang_Replace("LOADING")+ " - " + percent + " %", True, True)
		
		If percent = 100 Then 
			If firstloop And SelectedLoadingScreen\title <> "CWM" Then PlaySound HorrorSFX(8)
			Text(GraphicWidth / 2, GraphicHeight - 50, Lang_Replace("PRESS ANY KEY"), True, True)
		Else
			FlushKeys()
			FlushMouse()
		EndIf
		
		Flip
		
		firstloop = False
		If percent <> 100 Then Exit
		
	Until (GetKey()<>0 Or MouseHit(1))
End Function



Function rInput$(aString$)
	Local value% = GetKey()
	Local length% = Len(aString$)
	
	If value = 8 Then
		value = 0
		If length > 0 Then aString$ = Left(aString, length - 1)
	EndIf
	
	If value = 13 Or value = 0 Then
		Return aString$
	ElseIf value > 0 And value < 7 Or value > 26 And value < 32 Or value = 9
		Return aString$
	Else
		aString$ = aString$ + Chr(value)
		Return aString$
	End If
End Function

Function InputBox$(x%, y%, width%, height%, Txt$, ID% = 0)
	;TextBox(x,y,width,height,Txt$)
	Color (255, 255, 255)
	DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	Color (0, 0, 0)
	
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		Color(50, 50, 50)
		MouseOnBox = True
		If MouseHit1 Then SelectedInputBox = ID : FlushKeys
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	Color (255, 255, 255)	
	
	If (Not MouseOnBox) And MouseHit1 And SelectedInputBox = ID Then SelectedInputBox = 0
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt)
		If (MilliSecs() Mod 800) < 400 Then Rect (x + width / 2 + StringWidth(Txt) / 2 + 2, y + height / 2 - 5, 2, 12)
	EndIf	
	
	Text(x + width / 2, y + height / 2, Txt, True, True)
	
	Return Txt
End Function

Function DrawFrame(x%, y%, width%, height%, xoffset%=0, yoffset%=0)
	Color 255, 255, 255
	DrawTiledImageRect(MenuWhite, xoffset, (y Mod 256), 512, 512, x, y, width, height)
	
	DrawTiledImageRect(MenuBlack, yoffset, (y Mod 256), 512, 512, x+3*MenuScale, y+3*MenuScale, width-6*MenuScale, height-6*MenuScale)	
End Function

Function DrawButton%(x%, y%, width%, height%, txt$, bigfont% = True)
	Local clicked% = False
	
	DrawFrame (x, y, width, height)
	If MouseOn(x, y, width, height) Then
		Color(30, 30, 30)
		If MouseHit1 Then clicked = True : PlaySound(ButtonSFX)
		Rect(x + 4, y + 4, width - 8, height - 8)	
	Else
		Color(0, 0, 0)
	EndIf
	
	Color (255, 255, 255)
	If bigfont Then SetFont Font2 Else SetFont Font1
	Text(x + width / 2, y + height / 2, txt, True, True)
	
	Return clicked
End Function

Function DrawTick%(x%, y%, selected%, locked% = False)
	Local width% = 20 * MenuScale, height% = 20 * MenuScale
	
	Color (255, 255, 255)
	DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	;Rect(x, y, width, height)
	
	Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	
	If Highlight Then
		Color(50, 50, 50)
		If MouseHit1 Then selected = (Not selected) : PlaySound (ButtonSFX)
	Else
		Color(0, 0, 0)		
	End If
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	
	If selected Then
		If Highlight Then
			Color 255,255,255
		Else
			Color 200,200,200
		EndIf
		DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x + 4, y + 4, width - 8, height - 8)
		;Rect(x + 4, y + 4, width - 8, height - 8)
	EndIf
	
	Color 255, 255, 255
	
	Return selected
End Function

Function SlideBar#(x%, y%, width%, value#)
	
	If MouseDown1 Then
		If MouseX() >= x And MouseX() <= x + width + 14 And MouseY() >= y And MouseY() <= y + 20 Then
			value = Min(Max((MouseX() - x) * 100 / width, 0), 100)
		EndIf
	EndIf
	
	Color 255,255,255
	Rect(x, y, width + 14, 20,False)
	
	DrawImage(BlinkMeterIMG, x + width * value / 100.0 +3, y+3)
	
	Color 170,170,170 
	Text (x - 50 * MenuScale, y + 4*MenuScale, Lang_Replace("LOW"))					
	Text (x + width + 38 * MenuScale, y+4*MenuScale, Lang_Replace("HIGH"))	
	
	Return value
	
End Function




Function RowText(A$, X, Y, W, H, align% = 0)
	
	TextRect(X, Y, W, H, A$, align)
	;TextRect(X, Y, W, H, txt$, formatting=FT_LEFT, encoding=FT_UNICODE)
	
End Function

Function LimitText%(txt$, x%, y%, width%)
	If txt = "" Or width = 0 Then Return 0
	Local TextLength% = StringWidth(txt)
	Local UnFitting% = TextLength - width
	If UnFitting <= 0 Then ;mahtuu
		Text(x, y, txt)
	Else ;ei mahdu
		Local LetterWidth% = TextLength / Len(txt)
		
		Text(x, y, Left(txt, Max(Len(txt) - UnFitting / LetterWidth - 4, 1)) + "...")
	End If
End Function













;~IDEal Editor Parameters:
;~F#31#30C#31E#328#3F9#40C#429#443#464#47C#483
;~C#Blitz3D