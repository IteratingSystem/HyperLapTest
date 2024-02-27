package com.test.script;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.scripts.BasicScript;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;

/**
 * @author wenlong
 * @date 2024/2/27
 * @name com.test.script
 * @description
 **/
public class PlayerScript extends BasicScript {
    private static final int LEFT = 1;
    private static final int RIGHT = -1;
    private static final int JUMP = 0;

    private Vector2 impulse = new Vector2(0,0);
    private Vector2 speed = new Vector2(0,0);

    private World engine;

    private PhysicsBodyComponent physicsBodyComponent;
    private Entity animEntity;

    public PlayerScript(World engine){
        this.engine  = engine;
    }

    public void movePlayer(int direction){
        Body body = physicsBodyComponent.body;

        speed.set(body.getLinearVelocity());

        switch (direction){
            case LEFT:
                impulse.set(-5,speed.y);
                break;
            case RIGHT:
                impulse.set(5,speed.y);
                break;
            case JUMP:
                impulse.set(speed.x,5);
                break;
        }

        body.applyLinearImpulse(impulse.sub(speed),body.getWorldCenter(),true);
    }

    @Override
    public void init(int item) {
        super.init(item);
        ItemWrapper itemWrapper = new ItemWrapper(item,engine);
        animEntity = engine.getEntity(itemWrapper.getChild("player-anim").getEntity());

        physicsBodyComponent = ComponentRetriever.get(item,PhysicsBodyComponent.class,engine);
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            movePlayer(LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            movePlayer(RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PAUSE)){
            TransformComponent transformComponent = ComponentRetriever.get(entity,TransformComponent.class,engine);

            if (transformComponent.y < 6){
                movePlayer(JUMP);
            }
        }
    }

    @Override
    public void dispose() {

    }
}
