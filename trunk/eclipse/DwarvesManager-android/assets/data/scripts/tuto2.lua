--[[

TUTORIAL 2

--]]

map          = "tuto2.tmx"    -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 2              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 2"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto3.lua"    -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 2 : Une histoire de Pioche", "Aucun nain ne travaille dans votre mine !\nUtilisez le bouton de recrutement pour recruter\nun nain mineur !");
	else
		dwarf.popup("Tutorial 2 : A pickaxe story", "There is no dwarf currently working in your mine\nYou should recruit a miner using the recruitment button");
	end
end

-- Cette fonction est appelée é chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de départ de la caméra
	dwarf.restoreGui()
	dwarf.disableRoom()
	dwarf.disableObjects()
	dwarf.configure("Miner", true)
	dwarf.configure("Barman", false)
	dwarf.configure("Craftman", false)
	dwarf.configure("Dorm", false)
	dwarf.configure("Tavern", false)
	dwarf.configure("Workshop", true)
	dwarf.configure("Anvil", true)
	dwarf.configure("Barrel", false)
	dwarf.configure("Bed", false)
	dwarf.configure("Rack", false)
	dwarf.configure("Counter", false)
	dwarf.configure("TableObject", false)
end

-- Fonction appelée en boucle
function run()

	if progression == 1 then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Recrutement reussi", "Maintenant que votre nain est ici,\nordonnez lui de miner les diamants!");
		else
			dwarf.popup("Recruitment OK", "Now that your dwarf is ready, you should ask\nhim to mine some diamonds");
		end
		progression = 2
	end

	-- Eviter les besoins de sommeil et biere sur les premiers tutos 
	i = 0
	while i < dwarf.dwarfCount() do
		dwarf.setSleepNeed(i,0)
		dwarf.setThirstNeed(i,0)
		i = i +1
	end
	
end

function onEvent(id, p1, p2)

	if(progression == 0 and id == "MinerRecruited") then
		dwarf.blink("Dwarf", false)
		dwarf.blinkDwarf("Miner", false)
		dwarf.disableRecruitment()
		dwarf.backGameMenu()
		progression = 1
	end

	if(progression == 0 and id == "popupClosed") then
		dwarf.blink("Dwarf", true)
		dwarf.blinkDwarf("Miner", true)
	elseif(progression == 2 and id == "popupClosed") then
		dwarf.blink("Mine", true)
	elseif(progression == 2 and id == "pickaxeBreak") then
		dwarf.blink("Mine", false)
		x = tonumber(p1)
		y = tonumber(p2)
		dwarf.moveCamera(x,y,1.5)
		if dwarf.getLang() == "fr" then
			dwarf.popup("Une pioche de moins", "Aie Aie Aie !\nLe nain mineur a casse sa pioche !\nNous allons devoir en fabriquer une autre !");
		else
			dwarf.popup("A broken pickaxe", "Oh no !\nYour miner has broken his pickaxe !\nNow we'll have to craft him a new one !");
		end
		progression = 3
	elseif(progression == 3 and id == "popupClosed") then
		dwarf.enableRoom()
		dwarf.blink("Room",true)
		dwarf.blinkRoom("Workshop", true)
		if dwarf.getLang() == "fr" then
			dwarf.popup("Un atelier !", "Nous allons mettre en place un atelier de\nfabrication de pioches !\n \nCommencez par allez dans le menu\nconstruction de piece et choisisez l'atelier.\n")
		else
			dwarf.popup("A workshop", "We are going to start by putting in place a\nworkshop to craft new pickaxes !\n \nTo do that, use the room building button and\nthen choose the workshop.\n")
		end
		progression = 4
	elseif(progression == 4 and id == "placingRoomWorkshop") then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Creer la piece", "Vous devez maintenant definir l'emplacement\nde la piece en faisant glisser votre\ndoigt sur l'ecran.\n \nEssayez de faire une piece\nd'au moins 3x2 blocs")
		else
			dwarf.popup("Create the room", "You must know define the room area.\nUse your finger to form it.\n \nIt should be at least 3x2 blocks large.")
		end
		dwarf.moveCamera(13,4,0.5) 
		progression = 5
	elseif(progression == 5 and id == "roomPlaced") then
		dwarf.blink("Room", false)
		dwarf.blinkRoom("Workshop", false)
		size = tonumber(p1)
		if size < 6 then
			if dwarf.getLang() == "fr" then
				dwarf.popup("Creer la piece", "C'est un peu trop petit, recommencez !")
			else
				dwarf.popup("Create the room", "It's too small ! Do it again.")
			end
			dwarf.flushRooms();
		else
			if dwarf.getLang() == "fr" then
				dwarf.popup("Creer la piece", "C'est tres bien !\nMais nous n'avons pas fini !")
			else
				dwarf.popup("Create the room", "It looks great !\nBut we're not done yet.")
			end
			progression = 6
		end
	elseif(progression == 6 and id == "popupClosed") then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Creer la piece", "Une piece a besoin d'objets pour fonctionner.\nEn l'occurence, notre atelier a besoin\nd'une enclume et d'un ratelier\n \n \nAstuce : Si il manque des objets a une\npiece, elle apparaitra en rouge.\n")
		else
			dwarf.popup("Create the room", "A room needs furnitures to be used.\nIn our case, a workshop needs\nan anvil and a rack.\n \n \nHint : If it lacks an object\na room is displayed in red.\n")
		end
		progression = 7
	elseif(progression == 7 and id == "popupClosed") then
		dwarf.enableObjects()
		dwarf.backGameMenu()
		dwarf.blink("Object", true)
		dwarf.blinkObject("Anvil", true)
		if dwarf.getLang() == "fr" then
			dwarf.popup("Creer la piece", "Commencer par selectionner l'enclume dans le menu objet\n et placez la dans la piece\n")
		else
			dwarf.popup("Create the room", "Select the anvil in the object menu,\nand put it in the room")
		end
		progression = 8
	elseif(progression == 8 and id == "ObjectInRoom") then
		if p1 == "Anvil" and p2 == "Workshop" then
			if dwarf.getLang() == "fr" then
				dwarf.popup("Creer la piece", "Tres bien ! Grace a cette enclume nous allons\npouvoir fabriquer de nombreuses pioches !\n")
			else
				dwarf.popup("Create the room", "Very good ! Thanks to this anvil we'll be\nable to create many pickaxes !")
			end
			dwarf.blinkObject("Anvil", false)
			progression = 9
		end
	elseif(progression == 9 and id == "popupClosed") then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Creer la piece", "Il faut aussi placer un ratelier pour stocker les\nnouvelles pioches.\n")
		else
			dwarf.popup("Create the room", "You also have to put a rack in the room\nin order to store newly produced pickaxes.")
		end
		dwarf.configure("Rack", true)
		dwarf.blinkObject("Rack", true)
		dwarf.backGameMenu()
		progression = 10
	elseif(progression == 10 and id == "ObjectInRoom") then
		if p1 == "Rack" and p2 == "Workshop" then
			if dwarf.getLang() == "fr" then
				dwarf.popup("Creer la piece", "C'est parfait, si la piece est verte,\nalors elle est prete a l'emploi !")
			else
				dwarf.popup("Create the room", "Very good ! If the room is green,\nthen it's ready to use !!")
			end
			progression = 11
		end
	elseif(progression == 11 and id == "popupClosed") then
		dwarf.configure("Craftman", true)
		dwarf.configure("Miner", false)
		dwarf.blinkDwarf("Craftman", true)
		dwarf.blink("Object", false)
		dwarf.blink("Dwarf", true)
		dwarf.enableRecruitment()
		dwarf.backGameMenu()
		if dwarf.getLang() == "fr" then
			dwarf.popup("Creer la piece", "Il faut maintenant recruter un artisan,\nqui fabriquera les pioches !\nUtilisez le menu de recrutement")
		else
			dwarf.popup("Create the room", "Now you have to recruit a craftman.\nThis dwarf will run the workshop !\nUse the recruitment menu again.")
		end
		progression = 12
	elseif(progression == 12 and id == "CraftmanRecruited") then
		dwarf.disableRecruitment()
		if dwarf.getLang() == "fr" then
			dwarf.popup("Creer la piece", "C'est parfait !\nL'artisan va fabriquer une pioche,\net le mineur viendra la chercher\nquand elle sera prete !")
		else
			dwarf.popup("Create the room", "It's great !\nThe craftman will now make a pickaxe,\nand the miner will come to get it !")
		end
		progression = 13
	end
end







