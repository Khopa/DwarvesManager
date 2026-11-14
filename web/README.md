# Dwarves Manager — web port

The complete game running in the browser: all tutorials and levels, saves, options
and best times. The original Java code is compiled to JavaScript with
[TeaVM](https://teavm.org) through the [gdx-teavm](https://github.com/xpenatan/gdx-teavm) libGDX backend.

## Build

Needs JDK 17+. Gradle is downloaded by the wrapper.

```sh
cd web
./gradlew :teavm:buildWeb          # optimized build
WEB_DEBUG=1 ./gradlew :teavm:buildWeb   # readable JS + stack traces
```

The game is written to `teavm/build/dist/webapp/`. It's a static site: serve that
folder over HTTP with any web server (opening `index.html` from disk won't work):

```sh
python3 -m http.server 8080 --directory teavm/build/dist/webapp
# then open http://localhost:8080
```

## Layout

The game exists once in this repository. This folder only holds what the browser
build adds:

| Path | Contents |
|---|---|
| `../trunk/eclipse/DwarvesManager/src` | The game's Java sources (libGDX 1.14), compiled by the `core` module |
| `../trunk/eclipse/DwarvesManager-android/assets` | The game assets, packaged by the build |
| `core/` | Gradle module that compiles the sources above (no code of its own) |
| `teavm/` | Browser launcher (`WebLauncher`) and the TeaVM build entry point (`BuildWeb`) |

The older Eclipse projects under `trunk/eclipse` (desktop, Android, GWT) still ship the
libGDX 0.9.8 jars. The sources now target libGDX 1.14, so those projects need their
libraries and launchers updated before they build again.

## What changed from the original

Behaviour is kept as close to the original as possible. The changes needed for
modern libGDX and the browser:

- **libGDX 0.9.8 → 1.14 API migration:** `Batch` in `Actor.draw`, `GL20`,
  `GlyphLayout` (via a small `TextBounds` shim), viewports, new input-listener
  methods, the List/SelectBox/skin JSON schema, and `scrolled(float, float)`.
- **`Group.clear()` semantics:** since libGDX 1.0 it also removes listeners and
  actions, which broke the side panel and character animations. The game now calls
  `clearChildren()`.
- **No reflection:** objects, rooms and dwarves were created through
  `Class.forName`/`getConstructor`. `Ctor` provides the same lookup with explicit
  factories.
- **Lua:** LuaJ globals are built from the pure-Java libraries only, and the game API
  is installed directly instead of through `require "<java class>"`.
- **Shapes and sprites:** `BatchAwareShapeRenderer` pauses the active `SpriteBatch`
  around shape drawing, because modern libGDX binds the batch shader only in
  `begin()`.
- **Explicit bounds checks:** TeaVM doesn't throw `ArrayIndexOutOfBoundsException`,
  so tile lookups check bounds themselves.
- **Storage:** `Gdx.files.external` (config, best times, saves) is now
  `Gdx.files.local`, which gdx-teavm persists in IndexedDB.
- **Resolution:** a fixed 720px-tall virtual resolution with a `FitViewport`, and
  width following the window's aspect ratio.
- **Assets:** particle files re-saved in the current format, skin JSON updated, and
  Latin-1 text files converted to UTF-8. The build leaves out the files the game never
  loads (`sftpConf.xml`, `.pdn`, `.py`).
- **Original bugs fixed along the way:**
  - The save/load folder name differed in case (`Saves` vs `saves`).
  - The main menu buttons were off centre at some aspect ratios.
  - **Zoom drifted the camera.** Each wheel step rescaled the map position and added a guessed
    offset, so the view slid away. `MapLayer.zoomAt()` now zooms around the cursor (around the
    screen centre for touch pinch), keeping the map point under it fixed. Zooming in and back out
    returns to the same view.
  - The vertical scroll limit used the screen width instead of the height.
  - Middle-drag scrolling and screen-edge scrolling now work in virtual-screen units, so the map
    follows the cursor at any window size.

## Known limitations

- Audio is Ogg Vorbis, as in the original. Chrome, Edge and Firefox play it; older
  Safari versions may stay silent.
- The online score / account features in the original were already disabled
  ("Offline mode") and stay that way.
