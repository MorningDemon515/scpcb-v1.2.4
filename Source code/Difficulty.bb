Type Difficulty
	Field name$
	Field description$
	Field permaDeath%
	Field aggressiveNPCs
	Field saveType%
	
	Field customizable%
End Type

Dim difficulties.Difficulty(4)

Global SelectedDifficulty.Difficulty

Const SAFE=0, EUCLID=1, KETER=2, CUSTOM=3

Const SAVEANYWHERE = 0, SAVEONQUIT=1, SAVEONSCREENS=2

difficulties(SAFE) = New Difficulty
difficulties(SAFE)\name = Lang_Replace$("Safe")
difficulties(SAFE)\description = Lang_Replace$("The game can be saved any time. However, as in the case of SCP Objects, a Safe classification does not mean that handling it does not pose a threat.")
difficulties(SAFE)\permaDeath = False
difficulties(SAFE)\aggressiveNPCs = False
difficulties(SAFE)\saveType = SAVEANYWHERE

difficulties(EUCLID) = New Difficulty
difficulties(EUCLID)\name = Lang_Replace$("Euclid")
difficulties(EUCLID)\description = Lang_Replace$("Euclid-class objects are insufficiently understood or inherently unpredictable,")+" "
difficulties(EUCLID)\description = difficulties(EUCLID)\description +Lang_Replace$("such that reliable containment is not always possible.")+" "
difficulties(EUCLID)\description = difficulties(EUCLID)\description +Lang_Replace$("In Euclid difficulty, saving only allowed at specific locations marked by lit up computer screens.")
difficulties(EUCLID)\permaDeath = False
difficulties(EUCLID)\aggressiveNPCs = False
difficulties(EUCLID)\saveType = SAVEONSCREENS

difficulties(KETER) = New Difficulty
difficulties(KETER)\name = Lang_Replace$("Keter")
difficulties(KETER)\description = Lang_Replace$("Keter-class objects are considered the most dangerous ones in Foundation containment.")+" "
difficulties(KETER)\description = difficulties(KETER)\description +Lang_Replace$("The same can be said for this difficulty level: the SCPs are more aggressive, and you have only one life - when you die, the game is over. ")
difficulties(KETER)\permaDeath = True
difficulties(KETER)\aggressiveNPCs = True
difficulties(KETER)\saveType = SAVEONQUIT

difficulties(CUSTOM) = New Difficulty
difficulties(CUSTOM)\name = Lang_Replace$("Custom")
difficulties(CUSTOM)\permaDeath = False
difficulties(CUSTOM)\aggressiveNPCs = True
difficulties(CUSTOM)\saveType = SAVEANYWHERE
difficulties(CUSTOM)\customizable = True

SelectedDifficulty = difficulties(SAFE)
;~IDEal Editor Parameters:
;~C#Blitz3D