--[[

TUTORIAL 3

--]]

map          = "tuto3.tmx"    -- Map
music        = "tuto"   -- Musique de départ
objective    = 1              -- Objectif en diamants
progression  = 0              -- Variable de progression
levelName    = "Tutorial 3"   -- Nom pour stocker les records en ligne
nextLevel    = "tuto4.lua"    -- Niveau Suivant

-- Cette fonction est appelée au début de la partie --
function init()
	dwarf.addMoney(15000)  -- Argent de départ
	if dwarf.getLang() == "fr" then
		dwarf.popup("Tutorial 3 : Les blocs", "Certains blocs sont plus difficiles a miner.\nNotez leurs couleurs et choissisez le meilleur chemin !")
	else
		dwarf.popup("Tutorial 3 : Hard Blocks", "Some blocks are harder to mine than other.\nNote their colors and choose the best path !")
	end


end

-- Cette fonction est appelée à chaque fois que la map est chargée --
function setup()
	dwarf.moveCamera(13,13,0.5) -- Position de départ de la caméra
	dwarf.restoreGui()
	dwarf.configure("Miner", true)
	dwarf.configure("Barman", false)
	dwarf.configure("Craftman", true)
	dwarf.configure("Enchanter", false)
	dwarf.configure("Dorm", false)
	dwarf.configure("Tavern", false)
	dwarf.configure("Workshop", true)
	dwarf.configure("Anvil", true)
	dwarf.configure("Barrel", false)
	dwarf.configure("Bed", false)
	dwarf.configure("Rack", true)
	dwarf.configure("Counter", false)
	dwarf.configure("TableObject", false)
	dwarf.configure("Hearth", false)
end

-- Fonction appelée en boucle
function run()
	-- Eviter les besoins de sommeil et biere sur les premiers tutos 
	i = 0
	while i < dwarf.dwarfCount() do
		dwarf.setSleepNeed(i,0)
		dwarf.setThirstNeed(i,0)
		i = i +1
	end
end
