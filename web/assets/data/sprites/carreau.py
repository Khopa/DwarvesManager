# -*- coding: utf-8 -*- 

import pygame
from pygame.locals import *
 
pygame.init()

width = input("Largeur de l'image?:")
height = input("Hauteur de l'image?:")

Xtile = input("Largeur d'un tile'?:")
Ytile = input("Hauteur d'un tile'?:")
nom = raw_input("Nom de l'image:")

blue  = (0,0,255)
green =(0,255,0)

pygame.display.set_caption(nom+".png")
 
#Crée la surface de l'écran
screen = pygame.display.set_mode((width, height))



blueSurface  = pygame.Surface((Xtile, Ytile))
blueSurface = blueSurface.convert()
blueSurface.fill(blue)
greenSurface = pygame.Surface((Xtile, Ytile))
greenSurface = greenSurface.convert()
greenSurface.fill(green)




map2=[]


i=0

for i in range(width/Xtile):
	for j in range(height/Ytile):
		if(i%2):
			map2.append(blueSurface)
		else:
			map2.append(greenSurface)
		i+=1


k=0
for i in range(width/Xtile):
	for j in range(height/Ytile):
		screen.blit(map2[k],(i*Xtile,j*Ytile))
		pygame.display.flip()
		k+=1
 
continuer = True

while(continuer):
    #Regarde s'il y a une event
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            #On quitte la fenêtre
            pygame.image.save(screen, nom+".png")
            continuer = False