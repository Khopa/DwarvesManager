--[[

TUTORIAL 1

--]]

map          = "tuto1.tmx"    -- Map
music        = "music1.ogg"   -- Musique de d�part   (music1.ogg, music2.ogg et music3.ogg dispo uniquement)
objective    = 2              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 1"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto2.lua"    -- Niveau Suivant (si on ne met rien, le jeu ne proposera pas de passer au niveau suivant � la fin de la partie)

-- Cette fonction est appel�e au d�but de la partie --
function init()
	dwarf.addMoney(5000)  -- Argent de d�part
end

-- Cette fonction est appel�e � chaque fois que la map est charg�e --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de d�part de la cam�ra
end

-- Fonction appel�e en boucle toutes les secondes
function run()
	if progression==0 then
		initial()
	elseif progression==1 then
		state1()
	elseif progression==2 then
		state2()
	end
end

function initial()
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 1", "Bienvenue dans Dwarves Manager. \nLe but de ce jeu est de miner tous les diamants\nd'un niveau.\nPour cela vous devez recruter un nain et lui donner\nl'ordre de miner")
	else 
		dwarf.popup("Tutorial 1", "Welcome in Dwarves Manager. \nThe goal of this game is to mine all the diamonds\nin each level.\nTo do that, you must recruit a dwarf\nand define an area to mine")
	end		
	progression = 1;
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






