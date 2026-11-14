--[[

TUTORIAL 4

--]]

map          = "default.tmx"  -- Map
music        = "game"   -- Musique de départ
objective    = 1              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Random Level" -- Nom pour stocker les records en ligne
nextLevel    = "tuto5.lua"    -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de départ de la caméra
end

-- Fonction appelée en boucle
function run()
	
end


