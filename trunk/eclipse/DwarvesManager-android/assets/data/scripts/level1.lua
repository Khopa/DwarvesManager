--[[

Niveau 1

--]]

map          = "level1.tmx"   -- Map
music        = "game"   -- Musique de départ
objective    = 7              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Niveau 1"     -- Nom pour stocker les records en ligne
nextLevel    = "level2.lua"   -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(22,8,0.5) -- Position de départ de la caméra
	dwarf.restoreGui()
end

-- Fonction appelée en boucle
function run()

end
