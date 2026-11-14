package enibdevlab.dwarves.web;

import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;

import enibdevlab.dwarves.DwarvesManager;

/**
 * Browser entry point (compiled to JavaScript by TeaVM).
 */
public class WebLauncher {

	public static void main(String[] args) {
		TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
		// 0 = fill the browser window
		config.width = 0;
		config.height = 0;
		config.storagePrefix = "dwarvesmanager";
		new TeaApplication(new DwarvesManager(), config);
	}
}
