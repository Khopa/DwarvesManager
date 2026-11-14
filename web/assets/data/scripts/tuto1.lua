--[[

TUTORIAL 1

--]]

map          = "tuto1.tmx"    -- Map
music        = "tuto"   -- Musique de départ   (music1.ogg, music2.ogg et music3.ogg dispo uniquement)
objective    = 2              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 1"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto2.lua"    -- Niveau Suivant (si on ne met rien, le jeu ne proposera pas de passer au niveau suivant à la fin de la partie)

said = false
said2 = false

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(5000)  -- Argent de départ
	dwarf.addDwarf(12,5, "Miner")
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de départ de la caméra
	dwarf.disableRecruitment()
	dwarf.disableRoom()
	dwarf.disableObjects()
end

-- Fonction appelée en boucle toutes les secondes
function run()
	if progression==0 then
		initial()
	elseif progression==1 then
		state1()
	elseif progression==2 then
		state2()
	end

	-- Eviter les besoins de sommeil et biere sur les premiers tutos 
	i = 0
	while i < dwarf.dwarfCount() do
		dwarf.setSleepNeed(i,0)
		dwarf.setThirstNeed(i,0)
		i = i +1
	end
end

function initial()
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 1", "Bienvenue dans Dwarves Manager. \nLe but de ce jeu est de miner tous les diamants\nd'un niveau.\nUn nain est deja pret a executer vos ordres.\nUtilisez le bouton pioche pour definir une zone a miner")
	else 
		dwarf.popup("Tutorial 1", "Welcome in Dwarves Manager. \nThe goal of this game is to mine all the diamonds\nin each level.\nA dwarf is already ready to execute your orders.\nUse the pickaxe button to define an area to dig")
	end		
	progression = 1;
end

function onEvent(id, p1, p2)
	if(progression == 1 and id == "popupClosed") then
		dwarf.blink("Mine", true)
	elseif((progression == 1) and id == "hitMineButton" and said == false) then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 1", "Maintenant, touchez les blocs a miner !\nAttention les blocs sombres ne peuvent\netre detruits")
		else 
			dwarf.popup("Tutorial 1", "Now touch the blocs you want to mine !\nPlease note that the dark blocs can't be mined")
		end
		said = true
		dwarf.blink("Mine", false)
	elseif(progression == 1 and id == "newTile" and said2 == false) then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 1", "Excellent ! Tracez un chemin vers les diamants !\n \n \nAstuce : Vous pouvez faire glisser votre doigt sur\nl'ecran pour definir de plus grandes zones")
		else 
			dwarf.popup("Tutorial 1", "Really good ! Now define a path to the diamonds !\n \n \nHint : You can slide your finger across the screen\nto define larger areas")
		end
		said2 = true
	end
end

function state1()
	if dwarf.getDiamonds() == 1 then
		if dwarf.getLang() == "fr" then
			dwarf.popup("Tutorial 1 : Premiere Galerie", "Bravo ! \nVous avez pris votre premier diamant !\nMaintenant il faut aller chercher le second ! \n")
		else
			dwarf.popup("Tutorial 1", "Well done ! \nYou just picked up your very first diamond!\nNow, continue and get the second one ! \n")
		end		
		progression = 2
	end
end

function state2()
	-- On fait plus rien
end






