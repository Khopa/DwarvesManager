--[[

TUTORIAL 6

--]]

map          = "tuto6.tmx"    -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 4              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 6"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto7.lua"    		  -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()

	dwarf.addMoney(25000)  -- Argent de départ
	dwarf.addDwarf(12,11,"Miner")   -- Ajout du nain de depart
	dwarf.setThirstNeed(0,1.0)       -- Le nain 0 a sommeil

	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 6 : Besoins vitaux", "Les nains ont comme nous des besoins vitaux.\nEn fait, non les nains ont UN besoin vital : la biere !")
	else
		dwarf.popup("Tutorial 6 : Vital needs", "Like us, dwarves have vitals needs.\nWell, in fact, they have ONE vital need : beer !")
	end
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(22,8,0.5) -- Position de départ de la caméra
	dwarf.restoreGui()
	dwarf.disableRecruitment()
	dwarf.disableRoom()
	dwarf.disableObjects()
	dwarf.configure("Miner", false)
	dwarf.configure("Barman", true)
	dwarf.configure("Craftman", false)
	dwarf.configure("Dorm", false)
	dwarf.configure("Tavern", true)
	dwarf.configure("Workshop", false)
	dwarf.configure("Anvil", false)
	dwarf.configure("Barrel", false)
	dwarf.configure("Bed", false)
	dwarf.configure("Rack", false)
	dwarf.configure("Counter", false)
	dwarf.configure("TableObject", false)
end

-- Fonction appelée en boucle
function run()
	-- Evite d'avoir à gérer le sommeil cette fois ci
	i = 0
	while i < dwarf.dwarfCount() do
		dwarf.setSleepNeed(i,0)
		i = i +1
	end
end

-- Sur event
function onEvent(id, p1, p2)
	if(progression == 0 and id == "popupClosed") then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 6 : Besoins vitaux", "Encore une fois, nous avons un nain qui refuse de travailler !\nPour satisfaire sa soif, nous allons installer une taverne.\n \nUtiliser le menu de construction de piece et placez\nune taverne !\n \n \nAstuce : La jauge bleue indique le niveau de soif d'un nain !")
		else
			dwarf.popup("Tutorial 6 : Vital needs", "Once again, a dwarf is refusing to work !\nTo make him happy, we are going to install a tavern in our mine.\n \nUse the room building button to set up a tavern !\n \n \nHint : The blue gauge displays the dwarf's thirst level");
		end
		dwarf.enableRoom()
		dwarf.blink("Room", true)
		dwarf.blinkRoom("Tavern", true)
		progression = 1
	elseif progression == 1 and id == "roomPlaced" then
		size = tonumber(p1)
		if size < 45 then
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 6 : Besoins vitaux", "Une taverne doit etre tres grande !\nUtilisez tout l'espace dont vous disposez !")
			else
				dwarf.popup("Tutorial 6 : Vital needs", "A tavern must be huge !\nUse all the space you have !")
			end
			dwarf.flushRooms();
		else
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 6 : Besoins vitaux", "C'est parfait !")
			else
				dwarf.popup("Tutorial 6 : Vital needs", "It should be fine like that !")
			end
			dwarf.blink("Room", false)
			dwarf.blinkRoom("Tavern", false)
			progression = 2
		end
	elseif progression == 2 and id == "popupClosed" then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 6 : Besoins vitaux", "Placez maintenant un fut dans la taverne !")
		else
			dwarf.popup("Tutorial 6 : Vital needs", "Now place a barrel in the tavern !")
		end
		dwarf.enableObjects()
		dwarf.blink("Object",true)
		dwarf.configure("Barrel", true)
		dwarf.blinkObject("Barrel",true)
		progression = 3
	elseif progression == 3 and id == "ObjectInRoom" then
		if p1 == "Barrel" and p2 == "Tavern" then
			dwarf.blinkObject("Barrel",false)
			dwarf.blinkObject("TableObject",true)
			dwarf.configure("TableObject", true)
			dwarf.backGameMenu()
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 6 : Besoins vitaux", "Placez maintenant une table dans la taverne.\nAttention c'est gros !")
			else
				dwarf.popup("Tutorial 6 : Vital needs", "Now place a table in the tavern !\nTake care, it's huge")
			end
			progression = 4
		end
		
	elseif progression == 4 and id == "ObjectInRoom" then
		if p1 == "TableObject" and p2 == "Tavern" then
			dwarf.blinkObject("TableObject",false)
			dwarf.blinkObject("Counter",true)
			dwarf.configure("Counter", true)
			dwarf.backGameMenu()
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 6 : Besoins vitaux", "Placez maintenant un comptoir dans la taverne.\n \nEssayez de le placer pres du tonneau")
			else
				dwarf.popup("Tutorial 6 : Vital needs", "Now place a counter.\n \nIt is recommended to put it near the barrel.")
			end
			progression = 5
		end
	elseif progression == 5 and id == "ObjectInRoom" then
		if p1 == "Counter" and p2 == "Tavern" then
			dwarf.blinkObject("Counter",false)
			dwarf.blink("Object", false)
			dwarf.blink("Dwarf", true)
			dwarf.blinkDwarf("Barman", true)
			dwarf.configure("Barman", true)
			dwarf.enableRecruitment()
			if dwarf.getLang() == "fr" then
				dwarf.popup("Tutorial 6 : Besoins vitaux", "Il ne vous reste plus qu'a recruter un tavernier !\nIl s'occupera de la taverne et servira de la boisson aux\nnains qui ont soif.")
			else
				dwarf.popup("Tutorial 6 : Vital needs", "You just have to recruit a taverner to finish !\nHe will run the tavern, and serve\nbeer to thirsty dwarves !")
			end
			progression = 6
		end
	elseif progression == 6 and id == "BarmanRecruited" then
		dwarf.blink("Dwarf", false)
		dwarf.blinkDwarf("Barman", false)
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 6 : Besoins vitaux", "Vous maitrisez maintenant les bases de Dwarves Manager !")
		else
			dwarf.popup("Tutorial 6 : Vital needs", "You now master the basics of 'Dwarves Manager' !\nWell done !")
		end
		progression = 7
	end

end
