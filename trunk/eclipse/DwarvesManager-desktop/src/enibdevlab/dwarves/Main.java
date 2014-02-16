package enibdevlab.dwarves;

/*

Copyright (c) 2013, 
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in the
  documentation and/or other materials provided with the distribution.
* The names of its contributors may not be used to endorse or promote 
  products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

                                     II77                                       
                                     7I.                                        
                                     7=,                                        
                                     7+,                                        
                                     ,++                                        
                                     ,+?                                        
                                    ,,,,,7                                      
                                  ,?=IIII,,                                     
                               7,+++???????I?7                                  
                               ?++???????????I7                                 
                              ~+++????????????:                                 
                              ,+++????????????I7                                
                              ?+++????????????I7                                
                              ?+++,,??????????,I,7 7                            
                              ?++I?IIIIIIII77     777                           
                              ?+,???,7777,,,7777,,,,77                          
                              ?+,???7777777,77777,,, 7                         
                              ?+,???7777777,77777,,,77                          
                              ?++???,77777,77777,,,,,                           
                              ?++++,???????????????,7                           
                              ?+++??????????????,                               
                              +++?,???~,,,,,,,7                                 
                              ,+I7,?7777777777                                  
                              ,+?7,77777777777                                  
                              ,+,7,7I,.,,,,,?7I                                 
                             7,++??,,7777777,,I7                                
                           ,I???+??????????????7=                               
                         ,I????IIIIII?,,,?IIIIIII77,                            
                        ,??????IIIIIIIIIIIIIIIIIIII7,7                          
                        ++,???IIIIIIIIIIIIIIIIIIII7:I7                          
                       .?,+++++,+IIIIIIIIIIIII,,????I7,                         
                      ~++??++???????????????????????I??                         
                    7+===??,+??????????????????????????+7                       
                  7,===++,?,+??:=?????????????,,??????:?,7                      
                  +==+++=,?,+??????????????????????I??,+?+7                     
                 +==+,+=,++++??????????????????????I??=++?+                     
                +==+++~7,?~++??????????????????????I, ,=+=?,                    
               ,==++++   I++++????????????????????,I,  +++?+                    
              7+==+~+     ++++????????????????????,I,  7=++?,                   
              ,==+++      ?+++?,??????????????????,I,   ,,,?+                   
              +==+++      ?+++?,????????????????,?,I7   ,+++?                   
              +=,,+,      ,+++?:???????????????????I7   7+++?,                  
              +==++,      ,+++??????????????????????7   7+++?,                  
              +==+++       +++??????????????????????    7+++?,                  
              ,==:,:       ?+++???????????????????I?7   7+++?,                  
               ,==+++      ?+++???????????????????I?    7+++?,                  
               ?===++,     ,+++?,????????????????,I,    7+++?,                  
                ,==,++~    ,+++?????,,,,,,,,,,????I,    7+++?:                  
                7,===+++    ,?++??????????????????I    7????II~                 
                  ,===?????7  7,:???????????????,,7    ,????II?                 
                   7,+++?????7 7+==+++7    7+++++?+7   ,???III?                 
                    7+++++?,??7 ====++.      +=+++,+  ,?I7?I7?I,                
                     ,++,+,??+,  ,:,,+?7      +=,++?,7,? ,I? ,I,                
                      ,+++,+??7  7===+++       +++++?7   7 7                    
                      ,+++,,7     +==++?7       ++++++7                         
                                  ,==++?,       ,,,++?                          
                                  ,==+++,        ++++?,                         
                                  ,==+++,        +++++,                         
                                  ===+++,        +++++,                         
                                 7==,,+?7        ++++?,                         
                                7+==+++:        ,=+++?~                         
                                .==++++         +=++++7                         
                               ,==++~I7        7=+++?,   7 I7                   
                              ,==+++?,         ,=+++?7 ,   777 7                
                             ,==+++?,         ,=++++,  ,? 7 =7 ,                
                           7+==++=,,       7,?,+++++I,                          
                         ,+~==+++?7      7~+++????????I,                        
                       ,++++??,,?I?7     ?+++??????????I?                       
                     ,?++++?????????I?  ?+++????????????I,                      
                    7?++++???????????I, ??+?????????????,                       
                    ,?++++????????????I       7   7                             
                       =,???????I??..7                                

*/

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {
	public static void main(String[] args){
		
		// On recupère la résolution de l'écran par défaut
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "DwarvesManager";
		cfg.useGL20 = false;
		cfg.width = (int)800;
		cfg.height = (int)600;
		cfg.fullscreen = false;
		new LwjglApplication(new DwarvesManager(), cfg);
		
	}
} 

