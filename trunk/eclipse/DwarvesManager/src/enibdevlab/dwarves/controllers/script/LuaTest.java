package enibdevlab.dwarves.controllers.script;

import java.io.IOException;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jme.JmePlatform;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

public class LuaTest{
	
	public static void test() {
			
		LuaValue context;
		if(Gdx.app.getType() == ApplicationType.Android){
			context = JmePlatform.standardGlobals();
		}
		else{
			context = JsePlatform.standardGlobals();
		}
		
		Prototype prototype;
		try {
			
			prototype = LuaC.compile(Gdx.files.internal("data/script/test.lua").read(), "script");
			
			LuaClosure closure = new LuaClosure(prototype, context);
			closure.call();
			
			LuaValue f = context.get("MyAdd");
			LuaValue r = f.call(LuaValue.valueOf(7), LuaValue.valueOf(5));
			
			System.out.println(r.tojstring());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	
	}
	
}
