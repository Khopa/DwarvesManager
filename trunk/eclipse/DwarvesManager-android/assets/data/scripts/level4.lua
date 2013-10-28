--[[

Niveau 3

--]]

map          = "level4.tmx"   -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 2              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Niveau 4"     -- Nom pour stocker les records en ligne
nextLevel    = ""   -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(8000)  -- Argent de départ
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(16,8,1) -- Position de départ de la caméra
end

-- Fonction appelée en boucle
function run()

end