--[[

TUTORIAL 5

--]]

map          = "tuto5.tmx"    -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 4              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 5"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto6.lua"    		  -- Niveau Suivant

delay = 0;

-- Cette fonction est appelée au début de la partie --
function init()

	dwarf.addMoney(15000)           -- Argent de départ
	dwarf.addDwarf(12,11,"Miner")   -- Ajout du nain de depart
	dwarf.setSleepNeed(0,1.0)       -- Le nain 0 a sommeil

	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 5 : Au lit", "Les diamants sont tout proches !\nVite dite a ce mineur de les ramasser !")
	else
		dwarf.popup("Tutorial 5 : Sleep", "The diamonds are so close already !\nTell this miner to get them !")
	end
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(22,8,0.5) -- Position de départ de la caméra
	dwarf.restoreGui()
	dwarf.disableRecruitment()
	dwarf.disableRoom()
	dwarf.disableObjects()
	dwarf.configure("Dorm", true)
	dwarf.configure("Tavern", false)
	dwarf.configure("Workshop", false)
	dwarf.configure("Anvil", false)
	dwarf.configure("Barrel", false)
	dwarf.configure("Bed", true)
	dwarf.configure("Rack", false)
	dwarf.configure("Counter", false)
	dwarf.configure("TableObject", false)
end

-- Fonction appelée en boucle
function run()

	-- Evite d'avoir à gérer la bière pour le moment
	i = 0
	while i < dwarf.dwarfCount() do
		dwarf.setThirstNeed(i,0)
		i = i +1
	end

	---------------------------------

	if progression == 1 then
		delay = delay+1
		if delay > 2 then
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 5 : Au lit", "Mais que se passe t'il ?\nUn nain refuse de travailler ?")
			else
				dwarf.popup("Tutorial 5 : Sleep", "What the ... ?\nA dwarf refuses to work ?")
			end
			progression = 2
		end
	end

end

function onEvent(id, p1, p2)
	if(progression == 0 and id == "newTile") then
		progression = 1
	elseif progression == 2 and id=="popupClosed" then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 5 : Au lit", "Ah mais oui bien sur !\nCe nain manque de sommeil ! Il lui faut dormir !\n\nPlacez tout de suite un dortoir grace au menu\nd'achat de piece.\n \n \nAstuce : La jauge jaune au dessus d'un nain\nindique son besoin de sommeil. Quand elle\n est trop remplie, le nain cesse tout travail !")
		else
			dwarf.popup("Tutorial 5 : Sleep", "Yes of course !\nThis dwarf is so tired he can't work !\nCreate immediatly a dorm using the room building button\n \n \nHint : The yellow gauge over each dwarf displays\nhis sleep needs. When it's almost full, the dwarf\nwill stop working !")
		end
		dwarf.enableRoom()
		dwarf.blink("Room", true)
		dwarf.blinkRoom("Dorm", true)
		progression = 3
	elseif progression == 3 and id == "roomPlaced" then
		size = tonumber(p1)
		if size < 6 then
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 5 : Au lit", "C'est un peu trop petit, recommencez !")
			else
				dwarf.popup("Tutorial 5 : Sleep", "It's too small ! Do it again.")
			end
			dwarf.flushRooms();
		else
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 5 : Au lit", "Parfait !")
			else
				dwarf.popup("Tutorial 5 : Sleep", "Perfect !")
			end
			dwarf.blink("Room", false)
			dwarf.blinkRoom("Dorm", false)
			progression = 4
		end
	elseif progression == 4 and id == "popupClosed" then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 5 : Au lit", "Vous devez maintenant placer un lit dans le dortoir.\nUtilisez le menu d'achat d'objets.\n \n \nAstuce : Vous pouvez placer autant de lits que\nvous le souhaitez dans un dortoir tant\nqu'il reste de la place !")
		else
			dwarf.popup("Tutorial 5 : Sleep", "You now have to put a bed in the dorm.\nUse the furniture shop menu.\n \n \nHint : You can put as many bed as you want in a dorm\nas long as you have enough space !")
		end
		dwarf.enableObjects()
		dwarf.blink("Object",true)
		dwarf.blinkObject("Bed",true)
		progression = 5
	elseif progression == 5 and id == "ObjectInRoom" then
		if p1 == "Bed" and p2 == "Dorm" then
			dwarf.blink("Object",false)
			dwarf.blinkObject("Bed",false)
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 5 : Au lit", "Excellent !\nLe nain va maintenant aller dormir et\nretournera au travail ensuite !")
			else
				dwarf.popup("Tutorial 5 : Sleep", "Very good !\nThe dwarf will now go resting.\nHe will immediatly get back to work after that !")
			end
		end
		progression = 6
	end
end
