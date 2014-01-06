--[[

Niveau 2

--]]

map          = "level2.tmx"   -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 2              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Niveau 2"     -- Nom pour stocker les records en ligne
nextLevel    = ""   -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(35000)  -- Argent de départ
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(71,10,0.75) -- Position de départ de la caméra
	dwarf.restoreGui()
end

-- Fonction appelée en boucle
function run()

end