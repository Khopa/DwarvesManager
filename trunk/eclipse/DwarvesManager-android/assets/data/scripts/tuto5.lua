--[[

TUTORIAL 5

--]]

map          = "tuto5.tmx"    -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 3              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 5"   -- Nom pour stocker les records en ligne
nextLevel    = ""    		  -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 5 : Une vraie mine", "Les nains ont des besoins.\nIl vous faudra construire une taverne et un dortoir\npour terminer cette mission.\nUn nain fatigué ou endormi ne pourra pas travailler")
	else
		dwarf.popup("Tutorial 5 : Your first real mine", "Dwarves have essentials needs.\nYou will need a tavern and a bartender to give them beer\nand a dorm to make them sleep.\nA thirsty dwarf, or a sleepy one, can't work.")
	end
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(22,8,0.5) -- Position de départ de la caméra
end

-- Fonction appelée en boucle
function run()

end
