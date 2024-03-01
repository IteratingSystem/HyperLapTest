package com.test;

import com.artemis.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.test.component.DiamondComponent;
import com.test.component.PlayerComponent;
import com.test.script.PlayerScript;
import com.test.stage.HUD;
import com.test.system.CameraSystem;
import com.test.system.PlayerAnimationSystem;
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

    private World engine;

    private HUD hud;
    private Viewport hudViewPort;

    @Override
    public void create() {
        camera  = new OrthographicCamera();
//        viewport  = new ExtendViewport(640,480,0,0,camera);
        viewport  = new ExtendViewport(16,0,camera);

        assetManager = new AssetManager();
        assetManager.setLoader(AsyncResourceManager.class, new ResourceManagerLoader(assetManager.getFileHandleResolver()));
        assetManager.load("project.dt", AsyncResourceManager.class);
        assetManager.load("skin/skin.json", Skin.class);
        assetManager.load("skin/skin.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        Skin skin = assetManager.get("skin/skin.json");
        TextureAtlas textureAtlas = assetManager.get("skin/skin.atlas");

        AsyncResourceManager asyncResourceManager = assetManager.get("project.dt", AsyncResourceManager.class);
//        asyncResourceManager.addAtlasPack("skin",textureAtlas);

        CameraSystem cameraSystem = new CameraSystem(5, 40, 5, 6);


        SceneConfiguration configuration = new SceneConfiguration();
        configuration.setResourceRetriever(asyncResourceManager);
        configuration.addSystem(new PlayerAnimationSystem());
        configuration.addSystem(cameraSystem);

        sceneLoader = new SceneLoader(configuration);
        sceneLoader.loadScene("MainScene",viewport);
        sceneLoader.addComponentByTagName("diamond", DiamondComponent.class);

        engine = sceneLoader.getEngine();


        ItemWrapper root = new ItemWrapper(sceneLoader.getRoot(),engine);
        ItemWrapper player = root.getChild("player");
        int animEntity = player.getChild("player-anim").getEntity();
        engine.getMapper(PlayerComponent.class).create(animEntity);
        PlayerScript playerScript = new PlayerScript(engine);
        player.addScript(playerScript);
        cameraSystem.setFocus(engine.getEntity(player.getEntity()));


        hudViewPort = new ExtendViewport(768,576);


        hud = new HUD(skin,textureAtlas,hudViewPort,sceneLoader.getBatch());
        hud.setPlayerScript(playerScript);
        Gdx.input.setInputProcessor(hud);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        engine.process();

//        hud.act(Gdx.graphics.getDeltaTime());
//        hudViewPort.apply();
//        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);

        hudViewPort.update(width,height,true);

        if (width != 0 &&  height != 0){
            sceneLoader.resize(width,height );
        }
    }

    @Override
    public void dispose() {
        sceneLoader.dispose();
    }
}
