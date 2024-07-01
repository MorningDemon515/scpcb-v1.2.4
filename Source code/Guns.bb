;This BB file is actually defining the weapons and all their functions. You can easily add your own weapon by first putting it as a new type variable into
;the "InitGuns" function. After this, you need to update your gun using the "UpdateGuns" function. If you still not know how to add a new gun into the game,
;then write me a PM at the UnderTowGames Forum (my username on this forum: PXLSHN). But if you really wanna add a new weapon into that game, then you should
;try it out first by yourself (just using the code inside the "InitGuns" and "UpdateGuns" functions as a template.
;BTW: You need to let the worldmodel load by creating a item that is the gun, so it can be held in the inventory. The viewmodel of the gun must be loaded inside
;the main.bb file (also changing the scale values and other things there).

Global USPViewModel,P90Viewmodel,CrowbarViewmodel,M9ViewModel,GunPivot,GunParticle,P90Scope
Global AimCrossIMG
Global ExtraKevlarIMG
Global HoldingGun = 0
Global BulletIcon%
Dim USPSounds(6)
Dim P90Sounds(3)
Dim P90ShootSounds_In(7)
Dim P90ShootSounds_Out(7)
Dim USPShootSounds_In(7)
Dim USPShootSounds_Out(7)
Dim M9Sounds(2)
Global ShootEmptySFX
Global P90BulletMeter
Global Crowbar_HitPivot
Dim CrowbarSounds(6)
Global MuzzleFlash
Global KevlarIcon
Global Kevlar_Health% = 100
Global Kevlar_ExtraHealth% = 0
Global P90Ammo% = 3
Global USPAmmo% = 3
Global M9Ammo% = 0
Global KevlarSFX
Dim GunPickSFX(2)

Global KEY_RELOAD = GetINIInt("NineTailedFoxMod\options.ini", "options", "reload key")
Global KEY_TOGGLEGUN = GetINIInt("NineTailedFoxMod\options.ini", "options", "togglegun key")

Global Weapon_InSlot1$
Global Weapon_InSlot2$
Global Weapon_InSlot3$
Global Weapon_CurrSlot% = 1

Global BulletHole1,BulletHole2,DustParticle

Global GunPickPivot

Global GunPivot_Y#
Global GunPivot_YSide% = 0
Global GunPivot_X#
Global GunPivot_XSide% = 0

Global GunAnimFLAG = False

Global GunChangeFLAG = False

Global GunUpdateFLAG = 0

Global UsingScope%
Global ScopeTexture
Global ScopeCam

Global GunSFX,GunSFX2,GunCHN,GunCHN2

Global CanPlayerUseGuns% = True

;Type defination for the weapon + info
Type Guns
	Field ID					;<--- The ID of the gun. WARNING: Overwriting existing IDs could cause to the game would be glitched or at the worst case a MAV
	Field IMG					;<--- The HUD Image of the gun (not the item IMG!)
	Field CurrAmmo				;<--- Current ammo in a magazine
	Field MaxCurrAmmo			;<--- Max ammo in a magazine
	;Field CurrReloadAmmo		;<--- How much reload ammo the gun has (when picked up, etc...)
	;Field MaxReloadAmmo		;<--- How much magazines can be carried by the player
	Field DamageOnEntity		;<--- How much damage the gun makes, when the bullet you shot hit an hurtable entity
	Field FlySpeed = 100		;<--- How fast the bullet will fly (optional, default fly speed is 100)
	Field Accuracy = 100		;<--- How good the accuracy of the guns is (optional, but would be better to change it, max amount is 100 (best accuracy), and lowest is 1 (the lowest accuracy))
	Field CanHaveSilencer = 0	;<--- Does the gun has the ability to have a silencer atached to it?
	Field ShootState# = 0.0		;<--- Dont change this variables in the "CreateGun" or "InitGuns" functions!
	Field ReloadState# = 0.0	;<--- 								"-"
	Field DeployState# = 0.0	;<---								"-"
	Field GunState = 0			;<---								"-"
	Field Deployed% = 0			;<---								"-"
	Field Holster% = 0			;<---								"-"
	Field ShootAnim = 0			;<---								"-"
	Field HasSilencer% = 0		;<---								"-"
	Field SilenState# = 0.0		;<---								"-"
End Type

;Type defination for the bullets. You dont need to work with those
Type Bullet
	Field Numb
	Field DamageOnEntity
	Field FlySpeed
	Field Accuracy
End Type

Type BulletHole
	Field obj%
	Field obj2%
	Field obj3%
	Field obj4%
	Field obj5%
	Field obj6%
	Field KillTimer#,KillTimer2#
End Type

;Dont put anything inside this function, unless you want to make a special weapon like a rocket launcher or a nuke
Function CreateGun.Guns(id, img$, ammo1, maxammo1, ammo2, maxammo2, dmgentity, accuracy = 100, flyspeed = 100, canhavesilencer = 0)
	Local g.Guns = New Guns
	
	g\ID = id
	g\IMG = LoadImage("NineTailedFoxMod\GFX\items\"+img$)
	g\CurrAmmo = ammo1
	g\MaxCurrAmmo = maxammo1
	;g\CurrReloadAmmo = ammo2
	;g\MaxReloadAmmo = maxammo2
	g\DamageOnEntity = dmgentity
	g\FlySpeed = flyspeed
	g\Accuracy = accuracy
	g\CanHaveSilencer = canhavesilencer
	
	Return g
End Function

;Here you need to define your weapon (look for the other guns as a sample)
Function InitGuns()
	Local g.Guns
	
	;If GunPivot<>0 Then FreeEntity GunPivot:GunPivot=0
	
	;If GunPickPivot<>0 Then FreeEntity GunPickPivot:GunPickPivot=0
	
	NTF_InfiniteStamina% = False
	
	;For BS.BloodSpit = Each BloodSpit
	;	If BS\obj%<>0 Then FreeEntity BS\obj%:BS\obj%=0
	;	Delete BS
	;Next
	
	;For BH.BulletHole = Each BulletHole
	;	If BH\obj%<>0 Then FreeEntity BH\obj%:BH\obj%=0
	;	If BH\obj2%<>0 Then FreeEntity BH\obj2%:BH\obj2%=0
	;	If BH\obj3%<>0 Then FreeEntity BH\obj3%:BH\obj3%=0
	;	If BH\obj4%<>0 Then FreeEntity BH\obj4%:BH\obj4%=0
	;	If BH\obj5%<>0 Then FreeEntity BH\obj5%:BH\obj5%=0
	;	If BH\obj6%<>0 Then FreeEntity BH\obj6%:BH\obj6%=0
	;	Delete BH
	;Next
	
	GunAnimFLAG = False
	
	GunChangeFLAG = False
	
	GunUpdateFLAG = 0
	
	UsingScope% = False
	
	CanPlayerUseGuns% = True
	
	;If BulletHole1<>0
	;	FreeEntity BulletHole1:BulletHole1=0
	;	FreeEntity BulletHole2:BulletHole2=0
	;	FreeEntity DustParticle:DustParticle=0
	;EndIf
	
	;BulletHole1 = LoadSprite("GFX\bullethole1.jpg",1+2)
	;BulletHole2 = LoadSprite("GFX\bullethole2.jpg",1+2)
	;DustParticle = LoadSprite("GFx\dustparticle.jpg",1+2)
	;ScaleSprite BulletHole1,0.2,0.2
	;ScaleSprite BulletHole2,0.2,0.2
	;ScaleSprite DustParticle,0.1,0.1
	;SpriteViewMode BulletHole1,2
	;SpriteViewMode BulletHole2,2
	;SpriteViewMode DustParticle,1
	
	HoldingGun = 2
	Kevlar_Health% = 100
	Kevlar_ExtraHealth% = 0
	
	P90Ammo% = 3
	USPAmmo% = 3
	M9Ammo% = 0
	
	Weapon_InSlot1$ = "p90"
	Weapon_InSlot2$ = "usp"
	Weapon_InSlot3$ = "crowbar"
	If (Not IntroEnabled%)
		Weapon_CurrSlot% = 1
	Else
		Weapon_CurrSlot% = 4
		HoldingGun = 0
		CanPlayerUseGuns% = False
	EndIf
	
	AimCrossIMG = LoadImage_Strict("NineTailedFoxMod\GFX\AimCross.png")
	BulletIcon% = LoadImage_Strict("NineTailedFoxMod\GFX\bulleticon.png")
	P90BulletMeter = LoadImage_Strict("NineTailedFoxMod\GFX\P90_BulletMeter.jpg")
	KevlarIcon = LoadImage_Strict("NineTailedFoxMod\GFX\kevlarIcon.png")
	ExtraKevlarIMG = LoadImage_Strict("NineTailedFoxMod\GFX\ExtraKevlarMeter.jpg")
	
	KevlarSFX = LoadSound_Strict("NineTailedFoxMod\SFX\kevlarsound.ogg")
	
	ShootEmptySFX = LoadSound_Strict("NineTailedFoxMod\SFX\shoot_empty.ogg")
	;P90ShootSounds_Out(0) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_out_01.ogg")
	;P90ShootSounds_Out(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_out_02.ogg")
	;P90ShootSounds_Out(2) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_out_03.ogg")
	;P90ShootSounds_Out(3) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_out_04.ogg")
	;P90ShootSounds_Out(4) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_out_05.ogg")
	;P90ShootSounds_Out(5) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_out_06.ogg")
	;P90ShootSounds_Out(6) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_out_07.ogg")
	;P90ShootSounds_In(0) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_in_01.ogg")
	;P90ShootSounds_In(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_in_02.ogg")
	;P90ShootSounds_In(2) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_in_03.ogg")
	;P90ShootSounds_In(3) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_in_04.ogg")
	;P90ShootSounds_In(4) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_in_05.ogg")
	;P90ShootSounds_In(5) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_in_06.ogg")
	;P90ShootSounds_In(6) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\shoot_in_07.ogg")
	;USPShootSounds_Out(0) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_out_01.ogg")
	;USPShootSounds_Out(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_out_02.ogg")
	;USPShootSounds_Out(2) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_out_03.ogg")
	;USPShootSounds_Out(3) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_out_04.ogg")
	;USPShootSounds_Out(4) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_out_05.ogg")
	;USPShootSounds_Out(5) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_out_06.ogg")
	;USPShootSounds_Out(6) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_out_07.ogg")
	;USPShootSounds_In(0) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_in_01.ogg")
	;USPShootSounds_In(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_in_02.ogg")
	;USPShootSounds_In(2) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_in_03.ogg")
	;USPShootSounds_In(3) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_in_04.ogg")
	;USPShootSounds_In(4) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_in_05.ogg")
	;USPShootSounds_In(5) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_in_06.ogg")
	;USPShootSounds_In(6) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\shoot_in_07.ogg")
	;P90Sounds(0) = LoadSound_Strict("NineTailedFoxMod\SFX\p90_shoot.ogg")
	;P90Sounds(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\reload.ogg")
	;P90Sounds(2) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\deploy.ogg")
	;USPSounds(0) = LoadSound_Strict("NineTailedFoxMod\SFX\usp_shoot.ogg")
	;USPSounds(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\reload1.ogg")
	;USPSounds(2) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\deploy.ogg")
	;USPSounds(3) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\reload1.ogg")
	;USPSounds(4) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\reload2.ogg")
	;USPSounds(5) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\reload3.ogg")
	CrowbarSounds(0) = LoadSound_Strict("NineTailedFoxMod\SFX\cbar_hit1.wav")
	CrowbarSounds(1) = LoadSound_Strict("NineTailedFoxMod\SFX\cbar_hit2.wav")
	CrowbarSounds(2) = LoadSound_Strict("NineTailedFoxMod\SFX\cbar_miss1.wav")
	CrowbarSounds(3) = LoadSound_Strict("NineTailedFoxMod\SFX\cbar_hitbod1.wav")
	CrowbarSounds(4) = LoadSound_Strict("NineTailedFoxMod\SFX\cbar_hitbod2.wav")
	CrowbarSounds(5) = LoadSound_Strict("NineTailedFoxMod\SFX\cbar_hitbod3.wav")
	
	GunPickSFX(0) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\P90\pickup.ogg")
	GunPickSFX(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\USP\pickup.ogg")
	
	M9Sounds(0) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\M9BERETTA\deploy.ogg")
	M9Sounds(1) = LoadSound_Strict("NineTailedFoxMod\SFX\guns\M9BERETTA\reload_noammo.ogg")
	
	GunPivot = CreatePivot()
	
	GunPickPivot = CreatePivot()
	EntityParent GunPickPivot,GunPivot
	
	MuzzleFlash = LoadTexture("GFX\flash.jpg",1+2)
	
	ScopeTexture = CreateTexture(128,128,1+256+FE_RENDER+FE_ZRENDER)
	ScopeCam = CreateCamera(GunPivot)
	MoveEntity ScopeCam,0,0,0.15
	CameraZoom ScopeCam,7.5
	CameraViewport ScopeCam,0,0,128,128
	CameraRange ScopeCam,0.005,16
	CameraFogMode ScopeCam,1
	CameraFogRange (ScopeCam, CameraFogNear, CameraFogFar)
	CameraFogColor (ScopeCam, GetINIInt("options.ini", "options", "fog r"), GetINIInt("options.ini", "options", "fog g"), GetINIInt("options.ini", "options", "fog b"))
	CameraProjMode ScopeCam,0
	
	USPViewModel = LoadAnimMesh_Strict("NineTailedFoxMod\GFX\items\USP_Tactical_Viewmodel.b3d")
	ScaleEntity USPViewModel,0.005,0.005,0.005
	EntityParent USPViewModel,GunPivot
	MoveEntity USPViewModel,0.01,0.0,0.02
	HideEntity USPViewModel
	
	P90ViewModel = LoadAnimMesh_Strict("NineTailedFoxMod\GFX\items\P90_Viewmodel.b3d")
	ScaleEntity P90ViewModel,0.005,0.005,0.005
	EntityParent P90ViewModel,GunPivot
	MoveEntity P90ViewModel,0.01,0.0,0.0
	HideEntity P90ViewModel
	P90Scope = LoadAnimMesh_Strict("NineTailedFoxMod\GFX\items\P90_Viewmodel_Scope.b3d")
	ScaleEntity P90Scope,0.005,0.005,0.005
	EntityParent P90Scope,GunPivot
	MoveEntity P90Scope,0.01,0.0,0.0
	EntityFX P90Scope,17
	EntityTexture P90Scope,ScopeTexture
	HideEntity P90Scope
	
	CrowbarViewmodel = LoadAnimMesh_Strict("NineTailedFoxMod\GFX\items\Crowbar_Viewmodel.b3d")
	ScaleEntity CrowbarViewmodel,0.01,0.01,0.01
	EntityParent CrowbarViewmodel,GunPivot
	MoveEntity CrowbarViewmodel,0.11,-0.06,0.25
	RotateEntity CrowbarViewmodel,0,180,0
	HideEntity CrowbarViewModel
	Crowbar_HitPivot = CreatePivot(GunPivot)
	MoveEntity Crowbar_HitPivot,0,0,2 ;<--- This defines the range of the crowbar weapon!!!
	EntityRadius Crowbar_HitPivot,0.85
	EntityPickMode Crowbar_HitPivot,1
	HideEntity Crowbar_HitPivot
	
	M9ViewModel = LoadAnimMesh_Strict("NineTailedFoxMod\GFX\items\M9_Beretta_Viewmodel.b3d")
	ScaleEntity M9ViewModel,0.01,0.01,0.01
	EntityParent M9ViewModel,GunPivot
	MoveEntity M9ViewModel,-0.02,0.0,0.02
	RotateEntity M9ViewModel,0,270,0
	HideEntity M9ViewModel
	
	GunParticle = CreateSprite()
	EntityParent GunParticle,GunPivot
	MoveEntity GunParticle,0.06,-0.03,0.17
	EntityTexture(GunParticle, MuzzleFlash)
	EntityFX(GunParticle, 1 + 8)
	SpriteViewMode (GunParticle, 3)
	EntityBlend(GunParticle, BLEND_ADD)
	ScaleSprite(GunParticle, 0.08, 0.08)
	HideEntity GunParticle
	
	g.Guns = CreateGun(1, "INVusp.jpg", 12, 12, 3, 5, 39, 90, 100, 1)		;<---- USP Tactical
	g.Guns = CreateGun(2, "INVp90.jpg", 50, 50, 3, 5, 31, 85, 100, 0)		;<---- FN P90
	g.Guns = CreateGun(3, "INVcrowbar.jpg", 0, 0, 0, 0, 42, 100, 0, 0)		;<---- Crowbar
	g.Guns = CreateGun(4, "INVfreezer.jpg", 15, 15, 0, 5, 45, 87, 100, 0)	;<---- M9 Beretta
	
End Function

Function DeleteGuns()
	
	For g.Guns = Each Guns
		If g\IMG<>0 Then FreeImage g\IMG:g\IMG=0
		Delete g
	Next
	
	FreeImage AimCrossIMG : AimCrossIMG = 0
	FreeImage BulletIcon% : BulletIcon% = 0
	FreeImage P90BulletMeter : P90BulletMeter = 0
	FreeImage KevlarIcon : KevlarIcon = 0
	FreeImage ExtraKevlarIMG : ExtraKevlarIMG = 0
	
	FreeSound KevlarSFX : KevlarSFX = 0
	
	FreeSound ShootEmptySFX : ShootEmptySFX = 0
	For i = 0 To 6
		FreeSound P90ShootSounds_Out(i) : P90ShootSounds_Out(i) = 0
		FreeSound P90ShootSounds_In(i) : P90ShootSounds_In(i) = 0
		FreeSound USPShootSounds_Out(i) : USPShootSounds_Out(i) = 0
		FreeSound USPShootSounds_In(i) : USPShootSounds_In(i) = 0
	Next
	For i = 1 To 2
		FreeSound P90Sounds(i) : P90Sounds(i) = 0
	Next
	;For i = 2 To 5
	;	FreeSound USPSounds(i) : USPSounds(i) = 0
	;Next
	For i = 0 To 5
		FreeSound CrowbarSounds(i) : CrowbarSounds(i) = 0
	Next
	For i = 0 To 1
		FreeSound GunPickSFX(i) : GunPickSFX(i) = 0
	Next
	For i = 0 To 1
		FreeSound M9Sounds(i) : M9Sounds(i) = 0
	Next
	
End Function

;Here you need to make your gun work
Function UpdateGuns()
	Local g.Guns, n.NPCs
	Local de.Decals
	Local USPprevframe# = AnimTime(USPViewModel)
	Local P90prevframe# = AnimTime(P90ViewModel)
	Local Crowbarprevframe# = AnimTime(CrowbarViewmodel)
	Local M9prevframe# = AnimTime(M9ViewModel)
	;PositionEntity gunpivot,EntityX(Camera), EntityY(Camera), EntityZ(Camera)
	RotateEntity gunpivot,EntityPitch(Camera), EntityYaw(Camera), 0
	;For n.NPCs = Each NPCs
	;	EntityPickMode n\Collider,1
	;	EntityPickMode n\obj,2
	;Next
	GunAnimFLAG = True
	UsingScope% = False
	For g.Guns = Each Guns
		Select g\ID
			Case 1 ;<--- USP
				If HoldingGun = g\ID
					;ShowEntity USPViewModel
					
					If g\ReloadState# = 0.0 And g\ShootState# = 0.0
						If g\Deployed = 1
							If g\CurrAmmo < 12 And USPAmmo% > 0
								If AnimTime(USPViewModel) = 41.0 Or AnimTime(USPViewModel) = 225.0
									If KeyHit(KEY_RELOAD)
										g\CurrAmmo = 0
										;PlaySound USPSounds(1)
										;PlaySound USPSounds(Rand(3,5))
										PlayGunSound("USP\reload",1,3,1,False)
										USPprevframe# = 105.0
										Animate2(USPViewModel,AnimTime(USPViewModel),105,105,0.5,False)
										g\ReloadState# = 1.0
									EndIf
								Else
									If KeyHit(KEY_RELOAD)
										FlushKeys()
									EndIf
								EndIf
							Else
								If KeyHit(KEY_RELOAD)
									FlushKeys()
								EndIf
							EndIf
						Else
							If KeyHit(KEY_RELOAD)
								FlushKeys()
							EndIf
						EndIf
					Else
						Animate2(USPViewModel,AnimTime(USPViewModel),105,168,0.5,False)
						;AnimateGun(USPViewModel,105,168,0.5,False)
						If USPprevframe# < 167.5 And AnimTime(USPViewModel)=> 167.5
							g\CurrAmmo = 12
							USPAmmo% = USPAmmo% - 1
							g\ReloadState# = 0.0
						EndIf
					EndIf
					
					If g\CurrAmmo > 0
						If g\ReloadState# = 0.0
							If g\Deployed = 0
								Animate2(USPViewModel,AnimTime(USPViewModel),1,37,0.5,False)
								;AnimateGun(USPViewModel,1,37,0.5,False)
								If USPprevframe# < 12.0 And AnimTime(USPViewModel)=>12.0
									;PlaySound USPSounds(2)
									PlayGunSound("USP\deploy",0,0,1,False)
								ElseIf USPprevframe# < 36.5 And AnimTime(USPViewModel)=>36.5
									USPprevframe# = 0.0
									g\Deployed = 1
								EndIf
							Else
								If g\ShootAnim = 1
									Animate2(USPViewModel,AnimTime(USPViewModel),171,197,0.5,False)
									;AnimateGun(USPViewModel,171,197,0.5,False)
									If USPprevframe# < 196.5 And AnimTime(USPViewModel)=>196.5
										Animate2(USPViewModel,AnimTime(USPViewModel),41,41,0.5,False)
										;SetGunFrame(USPViewModel,41)
										USPprevframe# = 40.0
										g\ShootAnim = 0
									EndIf
								EndIf
								
								If g\ShootState# = 0.0 And g\SilenState# = 0.0
									If MouseHit1
										If ClosestButton=0 And ClosestItem=Null And GrabbedEntity%=0
											g\ShootAnim = 0
											Animate2(USPViewModel,AnimTime(USPViewModel),41,41,0.5,False)
											;SetGunFrame(USPViewModel,41)
											USPprevframe# = 170.0
											g\ShootState# = 1.0
										Else
											GunAnimFLAG = False
										EndIf
									ElseIf MouseHit2
										g\ShootAnim = 0
										g\SilenState# = 1.0
									Else
										If g\ShootAnim = 0
											Animate2(USPViewModel,AnimTime(USPViewModel),41,41,0.5,False)
											;SetGunFrame(USPViewModel,41)
											GunAnimFLAG = False
										EndIf
									EndIf
								ElseIf g\ShootState# = 1.0 And g\SilenState# = 0.0
									;PlaySound USPSounds(0)
									If g\HasSilencer% = 0
										If PlayerRoom\RoomTemplate\Name="gatea" Or PlayerRoom\RoomTemplate\Name="exit1" Or PlayerRoom\RoomTemplate\Name = "pocketdimension" Or PlayerRoom\RoomTemplate\Name = "dimension1499"
											;PlaySound USPShootSounds_Out(Rand(0,6))
											PlayGunSound("USP\shoot_out_0",1,7,0,True)
										Else
											;PlaySound USPShootSounds_In(Rand(0,6))
											PlayGunSound("USP\shoot_in_0",1,7,0,True)
										EndIf
									Else
										PlayGunSound("shoot_slienced",0,0,0,True)
									EndIf
									ShowEntity GunParticle
									RotateEntity GunParticle,0,0,Rnd(360)
									RotateEntity GunPickPivot,Rnd(-0.6,0.6),Rnd(-0.6,0.6),0
									EntityPick GunPickPivot,10000.0
									temp = 0
									For n.NPCs = Each NPCs
										If PickedEntity()<>0
											temp = 1
											If n\NPCtype = NPCtypeD2
												If PickedEntity() = n\HitBox
													DebugLog "D found"
													n\CurrHP% = n\CurrHP% - 14
													temp = 2
													Exit
												ElseIf PickedEntity() = n\HitBoxHead
													DebugLog "D found + Head"
													n\CurrHP% = 0
													temp = 2
													Exit
												EndIf
											EndIf
										EndIf
									Next
									If temp = 2
										p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
										p\speed = 0.001
										p\SizeChange = 0.003
										p\A = 0.8
										p\Achange = -0.02
										DebugLog "shot"
									ElseIf temp = 1
										p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
										p\speed = 0.001
										p\SizeChange = 0.003
										p\A = 0.8
										p\Achange = -0.01
										RotateEntity p\pvt, EntityPitch(GunPivot)-180, EntityYaw(GunPivot),0
										
										PlaySound2(Gunshot3SFX, Camera, p\pvt, 0.4, Rnd(0.8,1.0))
										
										For i = 0 To Rand(2,3)
											p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
											p\speed = 0.02
											p\A = 0.8
											p\Achange = -0.01
											RotateEntity p\pvt, EntityPitch(GunPivot)+Rnd(170,190), EntityYaw(GunPivot)+Rnd(-10,10),0	
										Next
										
										;bullet hole decal
										de.Decals = CreateDecal(Rand(13,14), PickedX(),PickedY(),PickedZ(), 0,0,0)
										AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
										MoveEntity de\obj, 0,0,-0.001
										EntityFX de\obj, 1
										de\lifetime = 70*20
										EntityBlend de\obj, 2
										de\Size = Rnd(0.028,0.034)
										ScaleSprite de\obj, de\Size, de\Size
									EndIf
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 1.0 And g\ShootState# < 3.0 And g\SilenState# = 0.0
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 2.99 And g\SilenState# = 0.0
									HideEntity GunParticle
									g\CurrAmmo = g\CurrAmmo - 1
									g\ShootAnim = 1
									g\ShootState# = 0.0
								ElseIf g\ShootState# = 0.0 And g\SilenState# = 1.0
									
								EndIf
							EndIf
						EndIf
					ElseIf g\CurrAmmo = 0
						If g\ReloadState# = 0.0
							If g\Deployed = 0
								Animate2(USPViewModel,AnimTime(USPViewModel),1,37,0.5,False)
								;AnimateGun(USPViewModel,1,37,0.5,False)
								If USPprevframe# < 12.0 And AnimTime(USPViewModel)=>12.0
									;PlaySound USPSounds(2)
									PlayGunSound("USP\deploy",0,0,1,False)
								ElseIf USPprevframe# < 36.5 And AnimTime(USPViewModel)=>36.5
									USPprevframe# = 0.0
									g\Deployed = 1
								EndIf
							Else
								If g\ShootAnim = 1
									Animate2(USPViewModel,AnimTime(USPViewModel),199,225,0.5,False)
									;AnimateGun(USPViewModel,199,225,0.5,False)
									If USPprevframe# < 224.5 And AnimTime(USPViewModel)=>224.5
										Animate2(USPViewModel,AnimTime(USPViewModel),225,225,0.5,False)
										;SetGunFrame(USPViewModel,225)
										USPprevframe# = 225.0
										g\ShootAnim = 0
									EndIf
								EndIf
								
								If g\ShootState# = 0.0
									If MouseHit1%
										If ClosestButton=0 And ClosestItem=Null And GrabbedEntity%=0
											g\ShootAnim = 0
											Animate2(USPViewModel,AnimTime(USPViewModel),225,225,0.5,False)
											;SetGunFrame(USPViewModel,255)
											USPprevframe# = 199.0
											g\ShootState# = 1.0
										Else
											GunAnimFLAG = False
										EndIf
									Else
										If g\ShootAnim = 0
											Animate2(USPViewModel,AnimTime(USPViewModel),225,225,0.5,False)
											;SetGunFrame(USPViewModel,225)
											GunAnimFLAG = False
										EndIf
									EndIf
								ElseIf g\ShootState# = 1.0
									PlaySound ShootEmptySFX
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 1.0 And g\ShootState# < 3.0
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 2.99
									HideEntity GunParticle
									g\ShootAnim = 1
									g\ShootState# = 0.0
								EndIf
							EndIf
						EndIf
					EndIf
				Else
					;HideEntity USPViewModel
					g\Deployed = 0
				EndIf
			Case 2 ;<--- P90
				;ShowEntity P90ViewModel
				UsingScope% = True
				
				If HoldingGun = g\ID
					If g\ReloadState# = 0.0 And g\ShootState# = 0.0
						If g\Deployed = 1
							If g\CurrAmmo < 50 And P90Ammo% > 0
								If AnimTime(P90ViewModel) = 32.0
									If KeyHit(KEY_RELOAD)
										g\CurrAmmo = 0
										;PlaySound P90Sounds(1)
										PlayGunSound("P90\reload",0,0,1,False)
										P90prevframe# = 50.0
										Animate2(P90ViewModel,AnimTime(P90ViewModel),50,50,0.5,False)
										;SetGunFrame(P90ViewModel,50)
										g\ReloadState# = 1.0
									EndIf
								Else
									If KeyHit(KEY_RELOAD)
										FlushKeys()
									EndIf
								EndIf
							Else
								If KeyHit(KEY_RELOAD)
									FlushKeys()
								EndIf
							EndIf
						Else
							If KeyHit(KEY_RELOAD)
								FlushKeys()
							EndIf
						EndIf
					ElseIf g\ReloadState# > 0.0 And g\ShootState# = 0.0
						Animate2(P90ViewModel,AnimTime(P90ViewModel),50,199,0.5,False)
						;AnimateGun(P90ViewModel,50,199,0.5,False)
						If P90prevframe# < 198.5 And AnimTime(P90ViewModel)=> 198.5
							g\CurrAmmo = 50
							P90Ammo% = P90Ammo% - 1
							g\ReloadState# = 0.0
						EndIf
					EndIf
					
					If g\CurrAmmo > 0
						If g\ReloadState# = 0.0
							If g\Deployed = 0
								Animate2(P90ViewModel,AnimTime(P90ViewModel),1,30,0.5,False)
								;AnimateGun(P90ViewModel,1,30,0.5,False)
								If P90prevframe# < 2.0 And AnimTime(P90ViewModel)=>2.0
									;PlaySound P90Sounds(2)
									PlayGunSound("P90\deploy",0,0,1,False)
								ElseIf P90prevframe# < 29.5 And AnimTime(P90ViewModel)=>29.5
									P90prevframe# = 0.0
									g\Deployed = 1
								EndIf
							Else
								If g\ShootAnim = 1
									Animate2(P90ViewModel,AnimTime(P90ViewModel),200,228,0.5,False)
									;AnimateGun(P90ViewModel,200,228,0.5,False)
									If USPprevframe# < 227.5 And AnimTime(P90ViewModel)=>227.5
										Animate2(P90ViewModel,AnimTime(P90ViewModel),32,32,0.5,False)
										;SetGunFrame(P90ViewModel,32)
										P90prevframe# = 32.0
										g\ShootAnim = 0
									EndIf
								EndIf
								
								If g\ShootState# = 0.0
									If MouseDown1%
										If ClosestButton=0 And ClosestItem=Null And GrabbedEntity%=0
											g\ShootAnim = 0
											Animate2(P90ViewModel,AnimTime(P90ViewModel),200,200,0.5,False)
											;SetGunFrame(P90ViewModel,200)
											P90prevframe# = 200.0
											g\ShootState# = 1.0
										Else
											GunAnimFLAG = False
										EndIf
									Else
										If g\ShootAnim = 0
											Animate2(P90ViewModel,AnimTime(P90ViewModel),32,32,0.5,False)
											;SetGunFrame(P90ViewModel,32)
											GunAnimFLAG = False
										EndIf
									EndIf
								ElseIf g\ShootState# = 1.0
									;PlaySound P90Sounds(0)
									If PlayerRoom\RoomTemplate\Name="gatea" Or PlayerRoom\RoomTemplate\Name="exit1" Or PlayerRoom\RoomTemplate\Name = "pocketdimension" Or PlayerRoom\RoomTemplate\Name = "dimension1499"
										;PlaySound P90ShootSounds_Out(Rand(0,6))
										PlayGunSound("P90\shoot_out_0",1,7,0,True)
									Else
										PlayGunSound("P90\shoot_in_0",1,7,0,True)
									EndIf
									ShowEntity GunParticle
									RotateEntity GunParticle,0,0,Rnd(360)
									RotateEntity GunPickPivot,Rnd(-1,1),Rnd(-1,1),0
									EntityPick GunPickPivot,10000.0
									temp = 0
									For n.NPCs = Each NPCs
										If PickedEntity()<>0
											temp = 1
											If n\NPCtype = NPCtypeD2
												If PickedEntity() = n\HitBox
													DebugLog "D found"
													n\CurrHP% = n\CurrHP% - 8
													temp = 2
													Exit
												ElseIf PickedEntity() = n\HitBoxHead%
													DebugLog "D found + Head"
													n\CurrHP% = 0
													temp = 2
													Exit
												EndIf
											EndIf
										EndIf
									Next
									If temp = 2
										p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
										p\speed = 0.001
										p\SizeChange = 0.003
										p\A = 0.8
										p\Achange = -0.02
										DebugLog "shot"
									ElseIf temp = 1
										p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
										p\speed = 0.001
										p\SizeChange = 0.003
										p\A = 0.8
										p\Achange = -0.01
										RotateEntity p\pvt, EntityPitch(GunPivot)-180, EntityYaw(GunPivot),0
										
										PlaySound2(Gunshot3SFX, Camera, p\pvt, 0.4, Rnd(0.8,1.0))
										
										For i = 0 To Rand(2,3)
											p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
											p\speed = 0.02
											p\A = 0.8
											p\Achange = -0.01
											RotateEntity p\pvt, EntityPitch(GunPivot)+Rnd(170,190), EntityYaw(GunPivot)+Rnd(-10,10),0	
										Next
										
										;bullet hole decal
										de.Decals = CreateDecal(Rand(13,14), PickedX(),PickedY(),PickedZ(), 0,0,0)
										AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
										MoveEntity de\obj, 0,0,-0.001
										EntityFX de\obj, 1
										de\lifetime = 70*20
										EntityBlend de\obj, 2
										de\Size = Rnd(0.028,0.034)
										ScaleSprite de\obj, de\Size, de\Size
									EndIf
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 1.0 And g\ShootState# < 2.5
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 2.49
									HideEntity GunParticle
									g\CurrAmmo = g\CurrAmmo - 1
									g\ShootAnim = 1
									g\ShootState# = 0.0
								EndIf
							EndIf
						EndIf
					ElseIf g\CurrAmmo = 0
						If g\ReloadState# = 0.0
							If g\Deployed = 0
								Animate2(P90ViewModel,AnimTime(P90ViewModel),1,30,0.5,False)
								;AnimateGun(P90ViewModel,1,30,0.5,False)
								If P90prevframe# < 29.5 And AnimTime(P90ViewModel)=>29.5
									P90prevframe# = 0.0
									g\Deployed = 1
								EndIf
							Else
								If g\ShootAnim = 1
									Animate2(P90ViewModel,AnimTime(P90ViewModel),200,228,0.5,False)
									;AnimateGun(P90ViewModel,200,228,0.5,False)
									If P90prevframe# < 227.5 And AnimTime(P90ViewModel)=>227.5
										Animate2(P90ViewModel,AnimTime(P90ViewModel),32,32,0.5,False)
										;SetGunFrame(P90ViewModel,32)
										P90prevframe# = 32.0
										g\ShootAnim = 0
									EndIf
								EndIf
								
								If g\ShootState# = 0.0
									;If MouseDown1%
									If MouseHit1%
										If ClosestButton=0 And ClosestItem=Null And GrabbedEntity%=0
											g\ShootAnim = 0
											Animate2(P90ViewModel,AnimTime(P90ViewModel),32,32,0.5,False)
											;SetGunFrame(P90ViewModel,32)
											P90prevframe# = 200.0
											g\ShootState# = 1.0
										Else
											GunAnimFLAG = False
										EndIf
									Else
										If g\ShootAnim = 0
											Animate2(P90ViewModel,AnimTime(P90ViewModel),32,32,0.5,False)
											;SetGunFrame(P90ViewModel,32)
											GunAnimFLAG = False
										EndIf
									EndIf
								ElseIf g\ShootState# = 1.0
									PlaySound ShootEmptySFX
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 1.0 And g\ShootState# < 2.5
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 2.49
									HideEntity GunParticle
									g\ShootAnim = 1
									g\ShootState# = 0.0
								EndIf
							EndIf
						EndIf
					EndIf
					
					SetAnimTime(P90Scope,AnimTime(P90ViewModel))
				Else
					;HideEntity P90ViewModel
					g\Deployed = 0
				EndIf
				
			Case 3 ;<--- Crowbar
				;ShowEntity CrowbarViewmodel
				
				If HoldingGun = g\ID
					If g\Deployed = 0
						Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),1,46,0.5,False)
						;AnimateGun(CrowbarViewmodel,1,46,0.5,False)
						If Crowbarprevframe# < 45.5 And AnimTime(CrowbarViewmodel) > 45.5
							g\Deployed = 1
						EndIf
					Else
						If MouseHit1
							If ClosestButton=0 And ClosestItem=Null And GrabbedEntity%=0
								If g\CurrAmmo = 0
									;campick = CameraPick(Camera,GraphicWidth/2,GraphicHeight/2)
									;If campick <> 0
										;If campick = Crowbar_HitPivot
											;g\CurrAmmo = 1
										;Else
											;g\CurrAmmo = 2
										;EndIf
									;Else
										;g\CurrAmmo = 1
									;EndIf
									pick = EntityPick(GunPivot,0.8)
									If PickedEntity() <> 0
										g\CurrAmmo = 2
									Else
										g\CurrAmmo = 1
									EndIf
								EndIf
							EndIf
						EndIf
						If g\CurrAmmo = 0
							Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),46,46,0.5,False)
							;SetGunFrame(CrowbarViewmodel,46)
							GunAnimFLAG = False
						ElseIf g\CurrAmmo = 1
							Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),197,258,0.5,False)
							;AnimateGun(CrowbarViewmodel,197,258,0.5,False)
							If g\ShootState# = 0.0
								PlaySound CrowbarSounds(2)
								g\ShootState# = 1.0
							EndIf
							If Crowbarprevframe# < 257.0 And AnimTime(CrowbarViewmodel) > 257.0
								g\ShootState# = 0.0
								g\CurrAmmo = 0
							EndIf
						ElseIf g\CurrAmmo = 2
							Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),258,324,0.5,False)
							;AnimateGun(CrowbarViewmodel,258,324,0.5,False)
							If g\ShootState# = 0.0
								PlaySound CrowbarSounds(Rand(0,1))
								g\ShootState# = 1.0
							EndIf
							If Crowbarprevframe# < 323.0 And AnimTime(CrowbarViewmodel) > 323.0
								g\ShootState# = 0.0
								g\CurrAmmo = 0
							EndIf
						EndIf
					EndIf
				Else
					;HideEntity CrowbarViewmodel
					g\Deployed = 0
				EndIf
			Case 4 ;<--- M9
				If HoldingGun = g\ID
					
					If g\ReloadState# = 0.0 And g\ShootState# = 0.0
						If g\Deployed = 1
							If g\CurrAmmo < 15 And M9Ammo% > 0
								If AnimTime(M9ViewModel) = 50.0 Or AnimTime(M9ViewModel) = 193.0
									If KeyHit(KEY_RELOAD)
										g\CurrAmmo = 0
										PlaySound M9Sounds(1)
										M9prevframe# = 79.0
										Animate2(M9ViewModel,AnimTime(M9ViewModel),79,79,0.5,False)
										g\ReloadState# = 1.0
									EndIf
								Else
									If KeyHit(KEY_RELOAD)
										FlushKeys()
									EndIf
								EndIf
							Else
								If KeyHit(KEY_RELOAD)
									FlushKeys()
								EndIf
							EndIf
						Else
							If KeyHit(KEY_RELOAD)
								FlushKeys()
							EndIf
						EndIf
					Else
						Animate2(M9ViewModel,AnimTime(M9ViewModel),79,192,0.75,False)
						;AnimateGun(USPViewModel,105,168,0.5,False)
						If M9prevframe# < 190.5 And AnimTime(M9ViewModel)=> 190.5
							g\CurrAmmo = 15
							M9Ammo% = M9Ammo% - 1
							g\ReloadState# = 0.0
						EndIf
					EndIf
					
					If g\CurrAmmo > 0
						If g\ReloadState# = 0.0
							If g\Deployed = 0
								Animate2(M9ViewModel,AnimTime(M9ViewModel),1,32,0.5,False)
								;AnimateGun(USPViewModel,1,37,0.5,False)
								If M9prevframe# < 12.0 And AnimTime(M9ViewModel)=>12.0
									PlaySound M9Sounds(0)
								ElseIf M9prevframe# < 31.5 And AnimTime(M9ViewModel)=>31.5
									M9prevframe# = 0.0
									g\Deployed = 1
								EndIf
							Else
								If g\ShootAnim = 1
									Animate2(M9ViewModel,AnimTime(M9ViewModel),192,219,0.5,False)
									;AnimateGun(USPViewModel,171,197,0.5,False)
									If M9prevframe# < 217.5 And AnimTime(M9ViewModel)=>217.5
										Animate2(M9ViewModel,AnimTime(M9ViewModel),50,50,0.5,False)
										;SetGunFrame(USPViewModel,41)
										M9prevframe# = 50.0
										g\ShootAnim = 0
									EndIf
								EndIf
								
								If g\ShootState# = 0.0
									If MouseHit1
										If ClosestButton=0 And ClosestItem=Null And GrabbedEntity%=0
											g\ShootAnim = 0
											Animate2(M9ViewModel,AnimTime(M9ViewModel),50,50,0.5,False)
											;SetGunFrame(USPViewModel,41)
											M9prevframe# = 192.0
											g\ShootState# = 1.0
										Else
											GunAnimFLAG = False
										EndIf
									Else
										If g\ShootAnim = 0
											Animate2(M9ViewModel,AnimTime(M9ViewModel),50,50,0.5,False)
											;SetGunFrame(USPViewModel,41)
											GunAnimFLAG = False
										EndIf
									EndIf
								ElseIf g\ShootState# = 1.0
									;PlaySound USPSounds(0)
									If PlayerRoom\RoomTemplate\Name="gatea" Or PlayerRoom\RoomTemplate\Name="exit1" Or PlayerRoom\RoomTemplate\Name = "pocketdimension" Or PlayerRoom\RoomTemplate\Name = "dimension1499"
										PlaySound USPShootSounds_Out(Rand(0,6))
									Else
										PlaySound USPShootSounds_In(Rand(0,6))
									EndIf
									ShowEntity GunParticle
									RotateEntity GunParticle,0,0,Rnd(360)
									RotateEntity GunPickPivot,Rnd(-0.6,0.6),Rnd(-0.6,0.6),0
									EntityPick GunPickPivot,10000.0
									temp = 0
									For n.NPCs = Each NPCs
										If PickedEntity()<>0
											temp = 1
											If n\NPCtype = NPCtypeD2
												If PickedEntity() = n\HitBox
													DebugLog "D found"
													n\CurrHP% = n\CurrHP% - 14
													temp = 2
													Exit
												ElseIf PickedEntity() = n\HitBoxHead
													DebugLog "D found + Head"
													n\CurrHP% = 0
													temp = 2
													Exit
												EndIf
											EndIf
										EndIf
									Next
									If temp = 2
										p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
										p\speed = 0.001
										p\SizeChange = 0.003
										p\A = 0.8
										p\Achange = -0.02
										DebugLog "shot"
									ElseIf temp = 1
										p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
										p\speed = 0.001
										p\SizeChange = 0.003
										p\A = 0.8
										p\Achange = -0.01
										RotateEntity p\pvt, EntityPitch(GunPivot)-180, EntityYaw(GunPivot),0
										
										PlaySound2(Gunshot3SFX, Camera, p\pvt, 0.4, Rnd(0.8,1.0))
										
										For i = 0 To Rand(2,3)
											p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
											p\speed = 0.02
											p\A = 0.8
											p\Achange = -0.01
											RotateEntity p\pvt, EntityPitch(GunPivot)+Rnd(170,190), EntityYaw(GunPivot)+Rnd(-10,10),0	
										Next
										
										;bullet hole decal
										de.Decals = CreateDecal(Rand(13,14), PickedX(),PickedY(),PickedZ(), 0,0,0)
										AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
										MoveEntity de\obj, 0,0,-0.001
										EntityFX de\obj, 1
										de\lifetime = 70*20
										EntityBlend de\obj, 2
										de\Size = Rnd(0.028,0.034)
										ScaleSprite de\obj, de\Size, de\Size
									EndIf
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 1.0 And g\ShootState# < 3.0
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 2.99
									HideEntity GunParticle
									g\CurrAmmo = g\CurrAmmo - 1
									g\ShootAnim = 1
									g\ShootState# = 0.0
								EndIf
							EndIf
						EndIf
					ElseIf g\CurrAmmo = 0
						If g\ReloadState# = 0.0
							If g\Deployed = 0
								Animate2(M9ViewModel,AnimTime(M9ViewModel),1,32,0.5,False)
								;AnimateGun(USPViewModel,1,37,0.5,False)
								If M9prevframe# < 12.0 And AnimTime(M9ViewModel)=>12.0
									PlaySound M9Sounds(0)
								ElseIf M9prevframe# < 30.5 And AnimTime(M9ViewModel)=>30.5
									M9prevframe# = 0.0
									g\Deployed = 1
								EndIf
							Else
								If g\ShootAnim = 1
									Animate2(M9ViewModel,AnimTime(M9ViewModel),192,193,0.5,False)
									;AnimateGun(USPViewModel,199,225,0.5,False)
									If M9prevframe# < 192.5 And AnimTime(M9ViewModel)=>192.5
										Animate2(M9ViewModel,AnimTime(M9ViewModel),193,193,0.5,False)
										;SetGunFrame(USPViewModel,225)
										M9prevframe# = 193.0
										g\ShootAnim = 0
									EndIf
								EndIf
								
								If g\ShootState# = 0.0
									If MouseHit1%
										If ClosestButton=0 And ClosestItem=Null And GrabbedEntity%=0
											g\ShootAnim = 0
											Animate2(M9ViewModel,AnimTime(M9ViewModel),193,193,0.5,False)
											;SetGunFrame(USPViewModel,255)
											M9prevframe# = 192.0
											g\ShootState# = 1.0
										Else
											GunAnimFLAG = False
										EndIf
									Else
										If g\ShootAnim = 0
											Animate2(M9ViewModel,AnimTime(M9ViewModel),193,193,0.5,False)
											;SetGunFrame(USPViewModel,225)
											GunAnimFLAG = False
										EndIf
									EndIf
								ElseIf g\ShootState# = 1.0
									PlaySound ShootEmptySFX
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 1.0 And g\ShootState# < 3.0
									g\ShootState# = g\ShootState# + FPSfactor#
								ElseIf g\ShootState# > 2.99
									HideEntity GunParticle
									g\ShootAnim = 1
									g\ShootState# = 0.0
								EndIf
							EndIf
						EndIf
					EndIf
				Else
					g\Deployed = 0
				EndIf
				
		End Select
		
		If GunChangeFLAG = False Then DebugLog "0"
		
		If HoldingGun = 0
			If GunChangeFLAG = False
				HideEntity USPViewModel
				HideEntity P90ViewModel
				HideEntity P90Scope
				HideEntity CrowbarViewmodel
				HideEntity M9ViewModel
				g\Deployed=0
				g\ShootState# = 0.0
				g\ReloadState# = 0.0
				g\ShootAnim = 0
				Animate2(USPViewModel,AnimTime(USPViewModel),0,0,False)
				Animate2(P90ViewModel,AnimTime(P90ViewModel),0,0,False)
				Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),0,0,False)
				Animate2(M9ViewModel,AnimTime(M9ViewModel),0,0,False)
				GunChangeFLAG = True
			EndIf
		ElseIf HoldingGun = 1
			If g\ID%<>1
				If GunChangeFLAG = False
					ShowEntity USPViewModel
					HideEntity P90ViewModel
					HideEntity P90Scope
					HideEntity CrowbarViewmodel
					HideEntity M9ViewModel
					g\Deployed=0
					g\ShootState# = 0.0
					g\ReloadState# = 0.0
					g\ShootAnim = 0
					;Animate2(USPViewModel,AnimTime(USPViewModel),0,0,False)
					Animate2(P90ViewModel,AnimTime(P90ViewModel),0,0,False)
					Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),0,0,False)
					Animate2(M9ViewModel,AnimTime(M9ViewModel),0,0,False)
					GunChangeFLAG = True
				EndIf
			EndIf
		ElseIf HoldingGun = 2
			If g\ID%<>2
				If GunChangeFLAG = False
					HideEntity USPViewModel
					ShowEntity P90ViewModel
					ShowEntity P90Scope
					HideEntity CrowbarViewmodel
					HideEntity M9ViewModel
					g\Deployed=0
					g\ShootState# = 0.0
					g\ReloadState# = 0.0
					g\ShootAnim = 0
					Animate2(USPViewModel,AnimTime(USPViewModel),0,0,False)
					;Animate2(P90ViewModel,AnimTime(P90ViewModel),0,0,False)
					Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),0,0,False)
					Animate2(M9ViewModel,AnimTime(M9ViewModel),0,0,False)
					GunChangeFLAG = True
				EndIf
			EndIf
		ElseIf HoldingGun = 3
			If g\ID%<>3
				If GunChangeFLAG = False
					HideEntity USPViewModel
					HideEntity P90ViewModel
					HideEntity P90Scope
					ShowEntity CrowbarViewmodel
					HideEntity M9ViewModel
					g\Deployed=0
					g\ShootState# = 0.0
					g\ReloadState# = 0.0
					g\ShootAnim = 0
					Animate2(USPViewModel,AnimTime(USPViewModel),0,0,False)
					Animate2(P90ViewModel,AnimTime(P90ViewModel),0,0,False)
					;Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),0,0,False)
					Animate2(M9ViewModel,AnimTime(M9ViewModel),0,0,False)
					GunChangeFLAG = True
				EndIf
			EndIf
		ElseIf HoldingGun = 4
			If GunChangeFLAG = False
				HideEntity USPViewModel
				HideEntity P90ViewModel
				HideEntity P90Scope
				HideEntity CrowbarViewmodel
				ShowEntity M9ViewModel
				g\Deployed=0
				g\ShootState# = 0.0
				g\ReloadState# = 0.0
				g\ShootAnim = 0
				Animate2(USPViewModel,AnimTime(USPViewModel),0,0,False)
				Animate2(P90ViewModel,AnimTime(P90ViewModel),0,0,False)
				Animate2(CrowbarViewmodel,AnimTime(CrowbarViewmodel),0,0,False)
				;Animate2(M9ViewModel,AnimTime(M9ViewModel),0,0,False)
				GunChangeFLAG = True
			EndIf
		EndIf
	Next
	;For n.NPCs = Each NPCs
	;	EntityPickMode n\Collider,0
	;	EntityPickMode n\obj,0
	;Next
	
End Function

Function DrawGunsInHUD()
	
	Local g.Guns
	Local width% = 204, height% = 20
		
		If HoldingGun = 1
			
			x% = GraphicWidth - 300
			y% = GraphicHeight - 95
			
			Color 155,155,155
			Rect(x + 123, y, width - 123, height, True)
			
			Color 255,255,255
			Rect(x, y, width, height, False)
			
			Color 0,0,0
			Rect(x - 50, y, 30, 30)
			
			Color 255, 255, 255
			Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
			DrawImage BulletIcon, x - 50, y
			For g.Guns = Each Guns
				If g\ID = 1
					For i = 1 To g\CurrAmmo
						DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
					Next
					SetFont Font3%
					Text x+220,y,"/"+USPAmmo%
				EndIf
			Next
			
			If (Not NTF_DisableAimCross%)
				MidHandle AimCrossIMG
				DrawImage AimCrossIMG,GraphicWidth/2,GraphicHeight/2
			EndIf
			
			;Text x+200,y,
		ElseIf HoldingGun = 2
			
			x% = GraphicWidth - 300
			y% = GraphicHeight - 95
			
			;Color 155,155,155
			;Rect(x + 123, y, width - 123, height, True)
			
			Color 255,255,255
			Rect(x, y, width, height, False)
			
			Color 0,0,0
			Rect(x - 50, y, 30, 30)
			
			Color 255, 255, 255
			Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
			DrawImage BulletIcon, x - 50, y
			For g.Guns = Each Guns
				If g\ID = 2
					For i = 1 To g\CurrAmmo
						DrawImage(P90BulletMeter, x + 3 + 3.98 * (i - 1), y + 3) ;x + 3 + 10 * (i - 1)
					Next
					SetFont Font3%
					Text x+220,y,"/"+P90Ammo%
				EndIf
			Next
			
			If (Not NTF_DisableAimCross%)
				MidHandle AimCrossIMG
				DrawImage AimCrossIMG,GraphicWidth/2,GraphicHeight/2
			EndIf
		
		ElseIf HoldingGun = 3
			If (Not NTF_DisableAimCross%)
				MidHandle AimCrossIMG
				DrawImage AimCrossIMG,GraphicWidth/2,GraphicHeight/2
			EndIf
		ElseIf HoldingGun = 4
			
			x% = GraphicWidth - 300
			y% = GraphicHeight - 95
			
			Color 155,155,155
			Rect(x + 123, y, width - 123, height, True)
			
			Color 255,255,255
			Rect(x, y, width, height, False)
			
			Color 0,0,0
			Rect(x - 50, y, 30, 30)
			
			Color 255, 255, 255
			Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
			DrawImage BulletIcon, x - 50, y
			For g.Guns = Each Guns
				If g\ID = 4
					For i = 1 To g\CurrAmmo
						DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
					Next
					SetFont Font3%
					Text x+220,y,"/"+M9Ammo%
				EndIf
			Next
			
			If (Not NTF_DisableAimCross%)
				MidHandle AimCrossIMG
				DrawImage AimCrossIMG,GraphicWidth/2,GraphicHeight/2
			EndIf
		EndIf
		
		Color 200,20,20
		For g.Guns = Each Guns
			Select Weapon_InSlot1$
				Case "p90"
					x% = GraphicWidth - 150
					y% = (GraphicHeight/2) - 150
					width% = 64
					height% = 64
					
					If g\ID% = 2
						Color 0,0,0
						Rect x%,y%,ImageWidth(g\IMG),ImageHeight(g\IMG)
						DrawImage g\IMG,x%,y%
						Color 200,20,20
					EndIf
					
					If Weapon_CurrSlot%=1
						Rect(x, y, width, height,False)
					EndIf
			End Select
			Select Weapon_InSlot2$
				Case "usp"
					x% = GraphicWidth - 150
					y% = (GraphicHeight/2) - 50
					width% = 64
					height% = 64
					
					If g\ID% = 1
						Color 0,0,0
						Rect x%,y%,ImageWidth(g\IMG),ImageHeight(g\IMG)
						DrawImage g\IMG,x%,y%
						Color 200,20,20
					EndIf
					
					If Weapon_CurrSlot%=2
						Rect(x, y, width, height,False)
					EndIf
				Case "m9"
					x% = GraphicWidth - 150
					y% = (GraphicHeight/2) - 50
					width% = 64
					height% = 64
					
					If g\ID% = 4
						Color 0,0,0
						Rect x%,y%,ImageWidth(g\IMG),ImageHeight(g\IMG)
						DrawImage g\IMG,x%,y%
						Color 200,20,20
					EndIf
					
					If Weapon_CurrSlot%=2
						Rect(x, y, width, height,False)
					EndIf
			End Select
			Select Weapon_InSlot3$
				Case "crowbar"
					x% = GraphicWidth - 150
					y% = (GraphicHeight/2) + 50
					width% = 64
					height% = 64
					
					If g\ID% = 3
						Color 0,0,0
						Rect x%,y%,ImageWidth(g\IMG),ImageHeight(g\IMG)
						DrawImage g\IMG,x%,y%
						Color 200,20,20
					EndIf
					
					If Weapon_CurrSlot%=3
						Rect(x, y, width, height,False)
					EndIf
			End Select
			x% = GraphicWidth - 150
			y% = (GraphicHeight/2) + 150
			width% = 64
			height% = 64
			Color 0,0,0
			Rect x%,y%,width%,height%
			Color 200,20,20
			If Weapon_CurrSlot%=4
				Rect(x, y, width, height, False)
			EndIf
		Next
		
		If KillTimer >= 0 And (CanPlayerUseGuns%=True) And FPSFactor#>0.0
			If KeyHit(KEY_TOGGLEGUN)
				GunChangeFLAG = False
				Weapon_CurrSlot% = Weapon_CurrSlot% + 1
				If Weapon_CurrSlot%>4 Then Weapon_CurrSlot%=1
				Select Weapon_CurrSlot%
					Case 1
						Select Weapon_InSlot1$
							Case "p90"
								HoldingGun=2
						End Select
					Case 2
						Select Weapon_InSlot2$
							Case "usp"
								HoldingGun=1
							Case "m9"
								HoldingGun=4
						End Select
					Case 3
						Select Weapon_InSlot3$
							Case "crowbar"
								HoldingGun=3
						End Select
					Case 4
						HoldingGun = 0
				End Select
			EndIf
		EndIf
		
		x% = GraphicWidth - 300
		y% = GraphicHeight - 55
		width% = 204
		height% = 20
		
		;Color 155,155,155
		;Rect(x + 123, y, width - 123, height, True)
		
		If Kevlar_Health%>0
			Color 255,255,255
		Else
			Color 255,0,0
		EndIf
		Rect(x, y, width, height, False)
		
		Color 0,0,0
		Rect(x - 50, y, 30, 30)
		
		If Kevlar_Health%>20
			Color 255, 255, 255
		Else
			Color 255,0,0
		EndIf
		Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
		DrawImage KevlarIcon, x - 50, y
		For i = 1 To Int(Kevlar_Health%/5)
			DrawImage(BlinkMeterIMG, x + 3 + 10 * (i - 1), y + 3)
		Next
		
		If Kevlar_ExtraHealth%>0
			Color 255,255,0
			Rect(x, y+height+5, (width/2)+2, height, False)
			
			For i = 1 To Int(Kevlar_ExtraHealth%/5)
				DrawImage(ExtraKevlarIMG, x + 3 + 10 * (i - 1), (y+height+5) + 3)
			Next
		EndIf
		
		Color 255,255,255
		
End Function

Function ToggleGuns_NoHUD()
	
	If KillTimer >= 0 And (CanPlayerUseGuns%=True) And FPSFactor#>0.0
		If KeyHit(KEY_TOGGLEGUN)
			GunChangeFLAG = False
			Weapon_CurrSlot% = Weapon_CurrSlot% + 1
			If Weapon_CurrSlot%>4 Then Weapon_CurrSlot%=1
			Select Weapon_CurrSlot%
				Case 1
					Select Weapon_InSlot1$
						Case "p90"
							HoldingGun=2
					End Select
				Case 2
					Select Weapon_InSlot2$
						Case "usp"
							HoldingGun=1
						Case "m9"
							HoldingGun=4
					End Select
				Case 3
					Select Weapon_InSlot3$
						Case "crowbar"
							HoldingGun=3
					End Select
				Case 4
					HoldingGun = 0
			End Select
		EndIf
	EndIf
	
End Function

Function AnimateGuns()
	
	If (Not GunAnimFLAG) And CurrSpeed=0.0
		If GunPivot_YSide%=0
			If GunPivot_Y# > -0.005
				GunPivot_Y# = GunPivot_Y# - (0.00005*FPSFactor)
			Else
				GunPivot_Y# = -0.005
				GunPivot_YSide% = 1
			EndIf
		Else
			If GunPivot_Y# < 0.0
				GunPivot_Y# = GunPivot_Y# + (0.00005*FPSFactor)
			Else
				GunPivot_Y# = 0.0
				GunPivot_YSide% = 0
			EndIf
		EndIf
		
		If GunPivot_X# < -0.001
			GunPivot_X# = GunPivot_X# + (0.0001*FPSFactor)
		ElseIf GunPivot_X# > 0.001
			GunPivot_X# = GunPivot_X# - (0.0001*FPSFactor)
		Else
			GunPivot_X# = 0.0
		EndIf
	ElseIf (Not GunAnimFLAG) And CurrSpeed<>0.0
		If GunPivot_YSide%=0
			If GunPivot_Y# > -0.005
				GunPivot_Y# = GunPivot_Y# - (0.0001*FPSFactor)
			Else
				GunPivot_Y# = -0.005
				GunPivot_YSide% = 1
			EndIf
		Else
			If GunPivot_Y# < 0.0
				GunPivot_Y# = GunPivot_Y# + (0.0001*FPSFactor)
			Else
				GunPivot_Y# = 0.0
				GunPivot_YSide% = 0
			EndIf
		EndIf
		
		If GunPivot_XSide%=0
			If GunPivot_X# > -0.0025
				GunPivot_X# = GunPivot_X# - (0.000075*FPSFactor)
			Else
				GunPivot_X# = -0.0025
				GunPivot_XSide% = 1
			EndIf
		Else
			If GunPivot_X# < 0.0025
				GunPivot_X# = GunPivot_X# + (0.000075*FPSFactor)
			Else
				GunPivot_X# = 0.0025
				GunPivot_XSide% = 0
			EndIf
		EndIf
	Else
		If GunPivot_Y# < 0.0
			GunPivot_Y# = GunPivot_Y# + (0.0001*FPSFactor)
		Else
			GunPivot_Y# = 0.0
		EndIf
		
		If GunPivot_X# < -0.001
			GunPivot_X# = GunPivot_X# + (0.0001*FPSFactor)
		ElseIf GunPivot_X# > 0.001
			GunPivot_X# = GunPivot_X# - (0.0001*FPSFactor)
		Else
			GunPivot_X# = 0.0
		EndIf
	EndIf
	
	PositionEntity gunpivot,EntityX(Camera), EntityY(Camera)+GunPivot_Y#, EntityZ(Camera)
	MoveEntity gunpivot,GunPivot_X#,0,0
	
End Function

Function Shoot2(x#,y#,z#,hitProb#=1.0,particles%=True,damage%=10)
	
	;muzzle flash
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	LightVolume = TempLightVolume*1.2
	
	If (Not GodMode) Then 
		
		If Rnd(1.0)=<hitProb Then
			TurnEntity Camera, Rnd(-3,3), Rnd(-3,3), 0
			
			;If WearingVest>0 Then
			If Kevlar_Health%>0
				;If WearingVest = 1 Then
					Select Rand(7)
						Case 1,2,3,4,5
							;BlurTimer = 500
							;Stamina = 0
							Msg = "Air escapes from your lungs as something hits your vest" : MsgTimer = 70*6		
							;Injuries = Injuries + Rnd(0.1,0.5)
							DamageKevlar(damage%)
							PlaySound NTF_PainSFX(Rand(0,7))
						Case 6
							BlurTimer = 500
							Msg = "You feel a burning pain in your left leg" : MsgTimer = 70*6
							Injuries = Injuries + Rnd(0.4,0.8)
							PlaySound NTF_PainWeakSFX(Rand(0,1))
						Case 7
							BlurTimer = 500
							Msg = "You feel a burning pain in your right leg" : MsgTimer = 70*6		
							Injuries = Injuries + Rnd(0.4,0.8)
							PlaySound NTF_PainWeakSFX(Rand(0,1))
						;Case 8
							;Kill()
					End Select	
				;Else
				;	If Rand(10)=1 Then
				;		Kill()
				;	Else
				;		Msg = "You feel something hitting your vest" : MsgTimer = 70*6	
				;		Injuries = Injuries + Rnd(0.1,0.5)
				;	EndIf
				;EndIf
			Else
				Select Rand(6)
					Case 1
						Kill()
					Case 2
						BlurTimer = 500
						Msg = "You feel a burning pain in your left leg" : MsgTimer = 70*6
						Injuries = Injuries + Rnd(0.4,0.8)
						PlaySound NTF_PainWeakSFX(Rand(0,1))
					Case 3
						BlurTimer = 500
						Msg = "You feel a burning pain in your right leg" : MsgTimer = 70*6	
						Injuries = Injuries + Rnd(0.4,0.8)
						PlaySound NTF_PainWeakSFX(Rand(0,1))
					Case 4
						BlurTimer = 500
						Msg = "You feel a burning pain in your right shoulder" : MsgTimer = 70*6			
						Injuries = Injuries + Rnd(0.6,1.0)
						PlaySound NTF_PainWeakSFX(Rand(0,1))	
					Case 5
						BlurTimer = 500
						Msg = "You feel a burning pain in your left shoulder" : MsgTimer = 70*6			
						Injuries = Injuries + Rnd(0.6,1.0)
						PlaySound NTF_PainWeakSFX(Rand(0,1))	
					Case 6
						BlurTimer = 500
						Msg = "You feel a burning pain in your abdomen" : MsgTimer = 70*6
						Injuries = Injuries + Rnd(2.0,4.0)
						PlaySound NTF_PainWeakSFX(Rand(0,1))
				End Select
			EndIf
			
			Injuries = Min(Injuries, 4.0)
			
			;Kill()
			PlaySound BullethitSFX
		ElseIf particles
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
			PointEntity pvt, p\obj
			TurnEntity pvt, 0, 180, 0
			
			EntityPick(pvt, 2.5)
			
			FreeEntity pvt
			
			If PickedEntity() <> 0 Then 
				PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
				
				If particles Then 
					;dust/smoke particles
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
					p\speed = 0.001
					p\SizeChange = 0.003
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
					
					For i = 0 To Rand(2,3)
						p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
						p\speed = 0.02
						p\A = 0.8
						p\Achange = -0.01
						RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
					Next
					
					;bullet hole decal
					Local de.Decals = CreateDecal(Rand(13,14), PickedX(),PickedY(),PickedZ(), 0,0,0)
					AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
					MoveEntity de\obj, 0,0,-0.001
					EntityFX de\obj, 1
					de\lifetime = 70*20
					EntityBlend de\obj, 2
					de\Size = Rnd(0.028,0.034)
					ScaleSprite de\obj, de\Size, de\Size
				EndIf				
			EndIf
			
		EndIf
		
	EndIf
	
End Function

Function ShootTarget(x#,y#,z#,n.NPCs,hitProb#=1.0,particles%=True,damage%=10)
	
	;muzzle flash
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	;LightVolume = TempLightVolume*1.2
	
	;If (Not GodMode) Then 
		
		If Rnd(1.0)=<hitProb Then
			p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
			p\speed = 0.001
			p\SizeChange = 0.003
			p\A = 0.8
			p\Achange = -0.02
			
			n\Target\CurrHP% = n\Target\CurrHP% - damage%
			
			PlaySound BullethitSFX
		ElseIf particles
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
			PointEntity pvt, p\obj
			TurnEntity pvt, 0, 180, 0
			
			EntityPick(pvt, 2.5)
			
			FreeEntity pvt
			
			If PickedEntity() <> 0 Then 
				PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
				
				If particles Then 
					;dust/smoke particles
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
					p\speed = 0.001
					p\SizeChange = 0.003
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
					
					For i = 0 To Rand(2,3)
						p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
						p\speed = 0.02
						p\A = 0.8
						p\Achange = -0.01
						RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
					Next
					
					;bullet hole decal
					Local de.Decals = CreateDecal(Rand(13,14), PickedX(),PickedY(),PickedZ(), 0,0,0)
					AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
					MoveEntity de\obj, 0,0,-0.001
					EntityFX de\obj, 1
					de\lifetime = 70*20
					EntityBlend de\obj, 2
					de\Size = Rnd(0.028,0.034)
					ScaleSprite de\obj, de\Size, de\Size
				EndIf				
			EndIf
			
		EndIf
		
	;EndIf
	
End Function


Function UpdateScope()
	
	If PlayerRoom\RoomTemplate\Name$ = "gatea"
		CameraFogRange ScopeCam, 5,30
		CameraFogColor (ScopeCam,200,200,200)
		CameraClsColor (ScopeCam,200,200,200)
		CameraRange(ScopeCam, 0.005, 30)
	ElseIf (PlayerRoom\RoomTemplate\Name = "gateaintro") Then
		CameraFogRange ScopeCam, 5,30
		CameraFogColor (ScopeCam,200,200,200)
		CameraClsColor (ScopeCam,200,200,200)					
		CameraRange(ScopeCam, 0.005, 100)
	ElseIf (PlayerRoom\RoomTemplate\Name = "exit1") And (EntityY(Collider)>1040.0*RoomScale)
		CameraFogRange ScopeCam, 5,45
		CameraFogColor (ScopeCam,200,200,200)
		CameraClsColor (ScopeCam,200,200,200)					
		CameraRange(ScopeCam, 0.005, 60)
	Else
		CameraFogRange(ScopeCam, CameraFogNear*LightVolume,CameraFogFar*LightVolume)
		CameraFogColor(ScopeCam, 0,0,0)
		CameraFogMode ScopeCam,1
		CameraRange(ScopeCam, 0.005, Min(CameraFogFar*LightVolume*1.5,28))
	EndIf
	
	CameraProjMode Camera,0
	CameraProjMode ScopeCam,1
	SetBuffer TextureBuffer(ScopeTexture)
	RenderWorld
	CopyRect 0,0,TextureWidth(ScopeTexture),TextureHeight(ScopeTexture),BackBuffer(),TextureBuffer(ScopeTexture)
	SetBuffer BackBuffer()
	CameraProjMode Camera,1
	CameraProjMode ScopeCam,0
	
End Function

Function PlayGunSound(file$,min_amount%=0,max_amount%=0,sfx%=0,pitchshift%=False)
	
	If sfx%=0
		If GunSFX <> 0 Then FreeSound GunSFX:GunSFX=0
		If ChannelPlaying(GunCHN) Then StopChannel(GunCHN) : GunCHN = 0
		If min_amount% = max_amount%
			GunSFX = LoadSound_Strict("NineTailedFoxMod\SFX\guns\"+file$+".ogg")
		Else
			GunSFX = LoadSound_Strict("NineTailedFoxMod\SFX\guns\"+file$+Rand(min_amount%,max_amount%)+".ogg")
		EndIf
		GunCHN = PlaySound(GunSFX)
		If GunPitchShift% = 1
			If pitchshift%
				ChannelPitch GunCHN,Rand(35000,43000)
			EndIf
		EndIf
	Else
		If GunSFX2 <> 0 Then FreeSound GunSFX2:GunSFX2=0
		If ChannelPlaying(GunCHN2) Then StopChannel(GunCHN2) : GunCHN2 = 0
		If min_amount% = max_amount%
			GunSFX2 = LoadSound_Strict("NineTailedFoxMod\SFX\guns\"+file$+".ogg")
		Else
			GunSFX2 = LoadSound_Strict("NineTailedFoxMod\SFX\guns\"+file$+Rand(min_amount%,max_amount%)+".ogg")
		EndIf
		GunCHN2 = PlaySound(GunSFX2)
	EndIf
	
End Function