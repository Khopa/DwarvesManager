package enibdevlab.dwarves.views;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Compatibility replacement for libGDX 0.9.x BitmapFont.TextBounds
 * (removed in libGDX 1.5 in favour of GlyphLayout).
 */
public class TextBounds {

	private static final GlyphLayout layout = new GlyphLayout();

	public float width;
	public float height;

	public static TextBounds of(BitmapFont font, CharSequence text){
		layout.setText(font, text == null ? "" : text);
		TextBounds b = new TextBounds();
		b.width = layout.width;
		b.height = layout.height;
		return b;
	}
}
