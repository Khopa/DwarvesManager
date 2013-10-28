--[[

TUTORIAL 4

--]]

map          = "tuto4.tmx"    -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 1              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 4"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto5.lua"    -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 4 : En route vers la fortune", "Certains blocs contiennent des gemmes\nqui vous rapporteront de l'argent !")
	else
		dwarf.popup("Tutorial 4 : How to become rich ?", "Some blocks contains gems\nwhich will yield you extra money !")
	end
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de départ de la caméra
end

-- Fonction appelée en boucle
function run()
end
