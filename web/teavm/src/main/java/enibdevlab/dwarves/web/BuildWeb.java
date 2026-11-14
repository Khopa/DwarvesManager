package enibdevlab.dwarves.web;

import java.io.File;
import java.io.IOException;

import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;

/**
 * Compiles the game to JavaScript into teavm/build/dist/webapp.
 * Run with: gradle :teavm:buildWeb
 */
public class BuildWeb {

	public static void main(String[] args) throws IOException {
		boolean debug = (args.length > 0 && args[0].equals("debug")) || System.getenv("WEB_DEBUG") != null;

		TeaBuildConfiguration config = new TeaBuildConfiguration();
		// The game assets live in the Android project and are shared by every platform
		config.assetsPath.add(new AssetFileHandle("../../trunk/eclipse/DwarvesManager-android/assets"));
		// Files in that folder the game never loads: keep them out of the published build
		config.assetFilter = (file, isDirectory, op) ->
				!(file.endsWith(".pdn") || file.endsWith(".py") || file.endsWith("sftpConf.xml"));
		config.webappPath = new File("build/dist").getCanonicalPath();
		config.targetType = TeaVMTargetType.JAVASCRIPT;
		config.htmlTitle = "Dwarves Manager";
		config.htmlWidth = 0;
		config.htmlHeight = 0;

		TeaBuilder.config(config);
		TeaVMTool tool = new TeaVMTool();
		tool.setMainClass(WebLauncher.class.getName());
		tool.setOptimizationLevel(debug ? TeaVMOptimizationLevel.SIMPLE : TeaVMOptimizationLevel.ADVANCED);
		tool.setObfuscated(!debug);
		TeaBuilder.build(tool);
	}
}
