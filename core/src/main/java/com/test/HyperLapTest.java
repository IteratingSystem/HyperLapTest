package com.test;

import com.artemis.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.test.script.PlayerScript;
import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;
import games.rednblack.editor.renderer.utils.ItemWrapper;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HyperLapTest extends ApplicationAdapter {
    private OrthographicCamera camera;
    private Viewport viewport;

    private AssetManager assetManager;

    private SceneLoader sceneLoader;

    World engine;

    @Override
    public void create() {
        camera  = new OrthographicCamera();
//        viewport  = new ExtendViewport(640,480,0,0,camera);
        viewport  = new ExtendViewport(16,0,camera);

        assetManager = new AssetManager();
        assetManager.setLoader(AsyncResourceManager.class, new ResourceManagerLoader(assetManager.getFileHandleResolver()));
        assetManager.load("project.dt", AsyncResourceManager.class);
        assetManager.finishLoading();

        AsyncResourceManager asyncResourceManager = assetManager.get("project.dt", AsyncResourceManager.class);

        SceneConfiguration configuration = new SceneConfiguration();
        configuration.setResourceRetriever(asyncResourceManager);

        sceneLoader = new SceneLoader(configuration);
        sceneLoader.loadScene("MainScene",viewport);

        engine = sceneLoader.getEngine();

        ItemWrapper root = new ItemWrapper(sceneLoader.getRoot(),engine);
        ItemWrapper player = root.getChild("player");
        player.addScript(new PlayerScript(engine));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        engine.process();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);

        if (width != 0 &&  height != 0){
            sceneLoader.resize(width,height );
        }
    }

    @Override
    public void dispose() {
        sceneLoader.dispose();
    }
}
