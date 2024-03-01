package com.test.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.test.script.PlayerScript;

/**
 * @author wenlong
 * @date 2024/3/1
 * @name com.test.stage
 * @description
 **/
public class HUD extends Stage {
    private Label diamondLabel;
    private PlayerScript playerScript;

    private boolean leftClick;
    private boolean rightClick;

    private int diamonds = -1;

    public HUD(Skin skin, TextureAtlas atlas, Viewport viewport, Batch batch){
        super(viewport,batch);

        Table root = new Table();
        root.pad(10,20,10,20);
        root.setFillParent(true);

        Table gemCounter = new Table();

        Image diamond = new Image(atlas.findRegion("gemCounter"));
        gemCounter.add(diamond);

        diamondLabel = new Label("Diamonds",skin);
        gemCounter.add(diamondLabel);

        root.add(gemCounter).expand().left().top().colspan(3);
        root.row();

        ImageButton leftButton = new ImageButton(skin,"left");
        leftButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftClick = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                leftClick = false;
            }
        });
        root.add(leftButton).left().bottom();

        ImageButton rightButton = new ImageButton(skin,"right");
        rightButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightClick = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                rightClick = false;
            }
        });
        root.add(rightButton).left().bottom().padLeft(20);

        ImageButton upButton = new ImageButton(skin,"up");
        upButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                playerScript.movePlayer(PlayerScript.JUMP);
            }
        });
        root.add(upButton).expand().right().bottom();

        addActor(root);
    }

    public void setPlayerScript(PlayerScript playerScript){
        this.playerScript = playerScript;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (leftClick){
            playerScript.movePlayer(PlayerScript.LEFT);
        }
        if (rightClick){
            playerScript.movePlayer(PlayerScript.RIGHT);
        }

        if (diamonds != playerScript.getPlayerComponent().diamondsCollected){
            diamonds = playerScript.getPlayerComponent().diamondsCollected;
            diamondLabel.setText("x"+diamonds);
        }
    }

}
