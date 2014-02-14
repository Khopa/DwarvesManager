--[[

Niveau 3

--]]

map          = "level5.tmx"   -- Map
music        = "game"   -- Musique de départ
objective    = 6              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Niveau 5"     -- Nom pour stocker les records en ligne
nextLevel    = ""             -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(8000)  -- Argent de départ
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(17,25,1) -- Position de départ de la caméra
	dwarf.restoreGui()
end

-- Fonction appelée en boucle
function run()

end