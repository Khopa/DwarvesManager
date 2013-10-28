--[[

TUTORIAL 2

--]]

map          = "tuto2.tmx"    -- Map
music        = "music1.ogg"   -- Musique de départ
objective    = 2              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 2"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto3.lua"    -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 2 : Une histoire de Pioche", "Au bout d'une certaine distance parcourue\ndans la roche, il peut arriver que la pioche\nd'un mineur se casse. Vous devrez alors recruter\nun nouveau mineur, ou construire un atelier et\nrecruter un nain artisan pour lui fabriquer une\nnouvelle pioche.");
	else
		dwarf.popup("Tutorial 2 : A pickaxe story", "After mining some blocks, your\ndwarf's pickaxe could break.\nTo fix that, you'll need to build a workshop\nand recruit a craftsman to build\nnew pickaxes for your dwarves !");
	end
end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de départ de la caméra
end

-- Fonction appelée en boucle
function run()
end







