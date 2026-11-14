--[[

Niveau 3

--]]

map          = "level7.tmx"   -- Map
music        = "game"   -- Musique de départ
objective    = 9              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Niveau 7"     -- Nom pour stocker les records en ligne
nextLevel    = ""             -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(12000)  -- Argent de départ
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(42,5,1) -- Position de départ de la caméra
	dwarf.restoreGui()
end

-- Fonction appelée en boucle
function run()

end