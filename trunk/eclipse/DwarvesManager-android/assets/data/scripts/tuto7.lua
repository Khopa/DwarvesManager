--[[

TUTORIAL 7

--]]

map          = "tuto7.tmx"    -- Map
music        = "tuto"   -- Musique de départ
objective    = 3              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 7"   -- Nom pour stocker les records en ligne
nextLevel    = ""    		  -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 7 : Une vraie mine", "C'est votre premiere vraie mine !\nRecrutez des nains et mettez en place des\ninfrastructures pour gagner la partie !\n \nSi vous aimez le jeu, n'oubliez pas de poster un commentaire\net d'en parler autour de vous !\nChaque commentaire et telechargement m'encourage a\ncontinuer les mises a jour de cette application !\nMerci !")
	else
		dwarf.popup("Tutorial 7 : Your first real mine", "It's your first real mine !\nRecruit dwarves and fulfill their basics needs to achieve victory !\n \nIf you like the game, considerer writing a comment !\nEach comment and download encourages me\nto continue updating this application ^^\nThank you !")
	end
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(22,8,0.5) -- Position de départ de la caméra
	dwarf.restoreGui()
end

-- Fonction appelée en boucle
function run()

end
