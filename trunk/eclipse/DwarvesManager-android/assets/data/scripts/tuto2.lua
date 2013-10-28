--[[

TUTORIAL 2

--]]

map          = "tuto2.tmx"    -- Map
music        = "music1.ogg"   -- Musique de d�part
objective    = 2              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 2"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto3.lua"    -- Niveau Suivant

-- Cette fonction est appel�e au d�but de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de d�part
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 2 : Une histoire de Pioche", "Au bout d'une certaine distance parcourue\ndans la roche, il peut arriver que la pioche\nd'un mineur se casse. Vous devrez alors recruter\nun nouveau mineur, ou construire un atelier et\nrecruter un nain artisan pour lui fabriquer une\nnouvelle pioche.");
	else
		dwarf.popup("Tutorial 2 : A pickaxe story", "After mining some blocks, your\ndwarf's pickaxe could break.\nTo fix that, you'll need to build a workshop\nand recruit a craftsman to build\nnew pickaxes for your dwarves !");
	end
end

-- Cette fonction est appel�e � chaque fois que la map est charg�e --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de d�part de la cam�ra
end

-- Fonction appel�e en boucle
function run()
end







