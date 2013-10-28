--[[

Niveau 3

--]]

map          = "level6.tmx"   -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 1              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Niveau 6"     -- Nom pour stocker les records en ligne
nextLevel    = ""             -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(8000)  -- Argent de départ
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(93,80,1) -- Position de départ de la caméra
end

-- Fonction appelée en boucle
function run()

end