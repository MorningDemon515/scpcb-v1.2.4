Graphics3D 1280,720,32,2
SetBuffer BackBuffer()

Global Font% = LoadFont("Arial",20)

Global CurrTime#, ElapsedTime#, PrevTime#, FPSFactor#
Global CheckFPS#, FPS, ElapsedLoops
LoopDelay = MilliSecs()

Global CurrAnimID% = 1
Global AnimID% = 0
Global MaxAnim% = 1
Global AnimPaused% = False

Global MaxVar% = 0
Global CurrVarID% = 1
Global VarID% = 1

Global CamView% = 0

AppTitle CommandLine$()

Type Animation
	Field ID%
	Field name$
End Type

Type Variant
	Field ID%
	Field name$
End Type

;Global file$ = Lower$(CommandLine$())
Global file$ = "gib_humanribs.b3d"

;index$ = Instr(file$,".")
;If index$>0 Then ext$=Mid$(file$,index+1)

;Select ext$
	;Case "b3d"
		ShowB3D(file$)
	;Case "x"
		ShowB3D(file$)
	;Case "3ds"
		;Show3DS(file$)
	;Default
		;RuntimeError "File '"+file$+"' is not a 'b3d','x' or '3ds' mesh!"
;End Select

Function ShowB3D(modelfile$)
	Local model$ = LoadAnimMesh(modelfile$)
	Local HasCFG%
	Local modelconffile% = ReadFile(modelfile$+".RTCFG")
	Local a.Animation, v.Variant
	
	If modelconffile% = 0
		HasCFG% = False
		ScaleEntity model$,0.1,0.1,0.1
	Else
		HasCFG% = True
		scaleX# = GetRTFileValue(modelfile$+".RTCFG","Scale","x","1.0")
		scaleY# = GetRTFileValue(modelfile$+".RTCFG","Scale","y","1.0")
		scaleZ# = GetRTFileValue(modelfile$+".RTCFG","Scale","z","1.0")
		ScaleEntity model$,scaleX#,scaleY#,scaleZ#
		RotateEntity model$,0,180,0
		DebugLog "Scale Values: "+scaleX#+"/"+scaleY#+"/"+scaleZ#
		If GetRTFileValue(modelfile$+".RTCFG","Animation","is_animated")=True
			MaxAnim% = GetRTFileValue(modelfile$+".RTCFG","Animation","anim_amount")
			For i = 1 To GetRTFileValue(modelfile$+".RTCFG","Animation","anim_amount")
				AnimID% = AnimID% + 1
				a.Animation = New Animation
				a\ID% = AnimID%
				a\name$ = GetRTFileValue(modelfile$+".RTCFG","Animation","anim"+i)
				DebugLog "Animation found: "+a\name$
				DebugLog a\name$+"-ID: "+a\ID%
			Next
		EndIf
		If GetRTFileValue(modelfile$+".RTCFG","Model","var_amount")<>""
			MaxVar% = GetRTFileValue(modelfile$+".RTCFG","Model","var_amount")
			For i = 1 To GetRTFileValue(modelfile$+".RTCFG","Model","var_amount")
				VarID% = VarID% + 1
				v.Variant = New Variant
				v\ID% = VarID%
				v\name$ = LoadAnimMesh(GetRTFileValue(modelfile$+".RTCFG","Model","var"+i)+".b3d")
				ScaleEntity v\name$,scaleX#,scaleY#,scaleZ#
				RotateEntity v\name$,0,180,0
				HideEntity v\name$
				DebugLog "Model Variant found: "+v\name$
				DebugLog v\name$+"-ID: "+v\ID%
			Next
		EndIf
	EndIf
	
	Camera = CreateCamera()
	CameraRange Camera,0.1,10000
	PositionEntity Camera,0,1,-4
	PositionEntity Camera,0,1,-4
	CameraClsColor Camera,0,0,255
	light = CreateFlashLite(Camera)
	
	Repeat
	Cls
	
	CurrTime = MilliSecs()
	ElapsedTime = (CurrTime - PrevTime) / 1000.0
	PrevTime = CurrTime
	FPSFactor = Max(Min(ElapsedTime * 70, 5.0), 0.00001)
	
	If CheckFPS < MilliSecs()
		FPS = ElapsedLoops
		ElapsedLoops = 0
		CheckFPS = MilliSecs()+1000
	EndIf
	ElapsedLoops = ElapsedLoops + 1
	
	If HasCFG%
		For a.Animation = Each Animation
			If a\ID% = CurrAnimID%
				If AnimPaused% = False
					Animate_Runtime(modelfile$+".RTCFG",model$,AnimTime(model$),a\name$,True)
					For v.Variant = Each Variant
						If v\name$ <> 0
							Animate_Runtime(modelfile$+".RTCFG",v\name$,AnimTime(v\name$),a\name$,True)
						EndIf
					Next
				Else
					Animate2(model$,AnimTime(model$),AnimTime(model$),AnimTime(model$),0.0,True)
					For v.Variant = Each Variant
						If v\name$ <> 0
							Animate2(v\name$,AnimTime(v\name$),AnimTime(v\name$),AnimTime(v\name$),0.0,True)
						EndIf
					Next
				EndIf
			EndIf	
		Next
		For v.Variant = Each Variant
			If v\ID% = CurrVarID%
				ShowEntity v\name$
				HideEntity model$
			ElseIf CurrVarID% = 1
				HideEntity v\name$
				ShowEntity model$
			Else
				HideEntity v\name$
			EndIf
		Next
	Else
		If AnimPaused% = False
			;Animate2(model$,AnimTime(model$),1,AnimLength(model$),0.5,True)
		Else
			;Animate2(model$,AnimTime(model$),AnimTime(model$),AnimTime(model$),0.0,True)
		EndIf
	EndIf
	
	UpdateWorld
	RenderWorld
	
	If KeyHit(2) Then CurrAnimID% = CurrAnimID% - 1
	If KeyHit(3) Then CurrAnimID% = CurrAnimID% + 1
	
	If KeyHit(4) Then AnimPaused% = (Not AnimPaused%)
	
	If CamView% = 0
		If KeyDown(203) Then TurnEntity model$,0,-0.5,0
		If KeyDown(205) Then TurnEntity model$,0,0.5,0
	EndIf
	
	For v.Variant = Each Variant
		If v\name$ <> 0
			If KeyDown(203) Then TurnEntity v\name$,0,-0.5,0
			If KeyDown(205) Then TurnEntity v\name$,0,0.5,0
		EndIf
	Next
	
	If MaxVar% > 0
		If KeyHit(5) Then CurrVarID% = CurrVarID% - 1
		If KeyHit(6) Then CurrVarID% = CurrVarID% + 1
	EndIf
	
	If KeyHit(7)
		CamView% = (Not CamView%)
		If CamView% = 1
			RotateEntity Camera,0,180,0
			PositionEntity Camera,0,0,0
			RotateEntity model$,0,180,0
			For v.Variant = Each Variant
				If v\name$ <> 0
					RotateEntity v\name$,0,180,0
				EndIf
			Next
		Else
			RotateEntity Camera,0,0,0
			PositionEntity Camera,0,1,-4
		EndIf
	EndIf
	
	If CurrAnimID% < 1
		CurrAnimID% = 1
	ElseIf CurrAnimID > MaxAnim%
		CurrAnimID% = MaxAnim%
	EndIf
	
	If MaxVar% > 0
		If CurrVarID% < 1
			CurrVarID% = 1
		ElseIf CurrVarID% > MaxVar%+1
			CurrVarID% = MaxVar%+1
		EndIf
	EndIf
	
	If (Not HasCFG%)
		SetFont Font%
		Color 255,0,0
		Text 0,0,"This model hasn't a RTCFG file."
	Else
		SetFont Font%
		Color 255,255,255
		Text 0,0,"Max Animations: "+MaxAnim%
		For a.Animation = Each Animation
			If a\ID% = CurrAnimID%
				Text 0,20,"Current Animation: "+a\name$
			EndIf
		Next
		If AnimPaused% = True
			Color 255,0,255
			Text 0,40,"Animation Paused: True"
		Else
			Color 255,255,255
			Text 0,40,"Animation Paused: False"
		EndIf
		Color 255,255,255
		Text 0,60,"Current Animation Frame: "+AnimTime(model$)
		If CurrVarID% = 1
			Text 0,80,"Current Model Variant: Standard Model"
		Else
			For v.Variant = Each Variant
				If v\ID% = CurrVarID%
					Text 0,80,"Current Model Variant: "+v\name$
				EndIf
			Next
		EndIf
		If CamView% = 0
			Text 0,100,"Current View Mode: 3DView"
		Else
			Text 0,100,"Current View Mode: FPSView"
		EndIf
	EndIf
	SetFont Font%
	Color 255,255,255
	Text 350,0,"Press '1' and '2' to toggle animation"
	Text 350,20,"Press '3' to pause/unpause animation"
	Text 350,40,"Press the left/right arrow keys to rotate model"
	Text 350,60,"Press '4' and '5' to toggle model variants"
	Text 350,80,"Press '6' to toggle 3DView/FPSView"
	
	Flip 0
	Until KeyHit(1)
	End
	
End Function

Function ShowX(model$)

End Function

Function Show3DS(model$)

End Function

Function Animate_Runtime(file$, entity%, curr#, animation$, loop=True)
	Local start = GetRTFileValue(file$,"Animation",animation$+"_start")
	Local quit = GetRTFileValue(file$,"Animation",animation$+"_quit")
	Local speed# = GetRTFileValue(file$,"Animation",animation$+"_speed")
	
	If speed > 0.0 Then 
		If loop Then
			SetAnimTime entity, Max(Min(curr + speed * FPSFactor,quit),start)
			If AnimTime(entity) => quit Then SetAnimTime entity, start
		Else
			SetAnimTime entity, Max(Min(curr + speed * FPSFactor,quit),start)
		EndIf
	Else
		If start < quit Then
			a% = start
			start = quit
			quit = a
		EndIf
		
		If loop Then 
			SetAnimTime entity, curr + speed * FPSFactor
			If AnimTime(entity) < quit Then SetAnimTime entity, start
			If AnimTime(entity) > start Then SetAnimTime entity, quit
		Else
			SetAnimTime entity, Max(Min(curr + speed * FPSFactor,start),quit)
		EndIf
	EndIf
	
End Function

Function GetRTFileValue$(file$, section$, parameter$, defaultvalue$="")
	Local TemporaryString$ = ""
	Local f% = ReadFile(file)
	If f% = 0
		RuntimeError "Stream does not exist: "+file$
	EndIf
	
	section = Lower(section)
	
	While Not Eof(f)
		Local strtemp$ = ReadLine(f)
		If Left(strtemp,2) <> ";["
			If Left(strtemp,1) = "[" Then
				strtemp$ = Lower(strtemp)
				If Mid(strtemp, 2, Len(strtemp)-2)=section Then
					Repeat
						TemporaryString = ReadLine(f)
						If Lower(Trim(Left(TemporaryString, Max(Instr(TemporaryString, "=") - 1, 0)))) = Lower(parameter) Then
							CloseFile f
							Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString,"=")) )
						EndIf
					Until Left(TemporaryString, 1) = "[" Or Eof(f)
					
					CloseFile f
					Return defaultvalue
				EndIf
			EndIf
		EndIf
	Wend
	
	CloseFile f
	
	Return defaultvalue
End Function

Function Min#(a#, b#)
	If a < b Then
		Return a
	Else
		Return b
	EndIf
End Function

Function Max#(a#, b#)
	If a > b Then
		Return a
	Else
		Return b
	EndIf
End Function

Function Animate2(ent%, curr#, start%, quit%, speed#, loop=True)
	If speed > 0.0 Then 
		If loop Then
			SetAnimTime ent, Max(Min(curr + speed/FPSFactor,quit),start)
			If AnimTime(ent) => quit Then SetAnimTime ent, start
		Else
			SetAnimTime (ent, Max(Min(curr + speed/FPSFactor,quit),start))
		EndIf
	Else
		If start < quit Then
			a% = start
			start = quit
			quit = a
		EndIf
		
		If loop Then 
			SetAnimTime (ent, curr + speed/FPSFactor)
			If AnimTime(ent) < quit Then SetAnimTime ent, start
			If AnimTime(ent) > start Then SetAnimTime ent, quit
		Else
			SetAnimTime (ent, Max(Min(curr + speed/FPSFactor,start),quit))
		EndIf
	EndIf
	
End Function

Function CreateFlashLite(parent=0)
 Local n#,tex,piv,i,sz#
 tex=CreateFlashLiteTex()
 piv=CreatePivot()
 n=40
 m#=0.5
 m2#=0.2
 For i=0 To n
  iflo#=Float(i)
  s=CreateSprite(piv)
  EntityBlend s,3
  EntityTexture s,tex
  TranslateEntity s,0,0,m
  m=m*1.08
  EntityAlpha s, 0.05;13
  sz#=3.0
  ScaleSprite s,m2,m2
  m2=m2*1.07 ;085
 Next
 If parent<>0 Then
  TranslateEntity piv,0,3,0
  EntityParent piv,parent
 EndIf

 li=CreateLight(3,piv)
 LightConeAngles li,0,180
 LightRange li,1.0
 LightColor li,150,150,150
 PositionEntity li,EntityX(parent),EntityY(parent),EntityZ(parent),1


 li2=CreateLight(3,piv)
 PositionEntity li2,EntityX(parent),EntityY(parent),EntityZ(parent),1
 LightConeAngles li2,0,180
 LightRange li2,1.0
 MoveEntity li2,0,1,0
 LightColor li2,150,150,150



 Return piv
End Function

Function CreateFlashLiteTex()
 Local tex,gr,i,j,blue
 tex=CreateTexture(128,128,(2 Or 48))
 SetBuffer BackBuffer()
 For i=64 To 0 Step -1
  gr=(65-i)*8
  If gr>255 Then gr=255
  If gr<0 Then gr=0
  Color gr,gr,gr
  Oval 64-i,64-i,Abs(i+i),Abs(i+i),1
  Oval 63-i,64-i,Abs(i+i),Abs(i+i),1
  Oval 64-i,63-i,Abs(i+i),Abs(i+i),1
  Oval 63-i,63-i,Abs(i+i),Abs(i+i),1
 Next
 LockBuffer(BackBuffer())
 For j=0 To TextureHeight(tex)-1
  For i=0 To TextureWidth(tex)-1
   blue=(ReadPixelFast(i,j) And $ff)/3.0
   WritePixelFast i,j,(blue Shl 24) Or $ffffff
  Next
 Next
 UnlockBuffer(BackBuffer())
 SetBuffer BackBuffer()
 CopyRect 0,0,128,128,0,0,BackBuffer(),TextureBuffer(tex)
 Return tex
End Function