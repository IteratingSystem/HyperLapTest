package com.test.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.test.component.PlayerComponent;
import games.rednblack.editor.renderer.components.ParentNodeComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.components.sprite.SpriteAnimationComponent;
import games.rednblack.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

/**
 * @author wenlong
 * @date 2024/2/28
 * @name com.test.system
 * @description
 **/
@All(PlayerComponent.class)
public class PlayerAnimationSystem extends IteratingSystem {
    ComponentMapper<PlayerComponent> playerComponentMapper;

    @Override
    protected void process(int entityId) {
        ParentNodeComponent parentNodeComponent = ComponentRetriever.get(entityId,ParentNodeComponent.class,world);
        Body body = ComponentRetriever.get(parentNodeComponent.parentEntity, PhysicsBodyComponent.class,world).body;

//        PlayerComponent playerComponent = ComponentRetriever.get(entityId,PlayerComponent.class,world);
        PlayerComponent playerComponent = playerComponentMapper.get(entityId);


        SpriteAnimationComponent spriteAnimationComponent = ComponentRetriever.get(entityId,SpriteAnimationComponent.class,world);
        SpriteAnimationStateComponent spriteAnimationStateComponent = ComponentRetriever.get(entityId,SpriteAnimationStateComponent.class,world);
        TransformComponent transformComponent = ComponentRetriever.get(entityId,TransformComponent.class,world);


        if (Math.abs(body.getLinearVelocity().x) > 0.1f){
            spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
            spriteAnimationComponent.currentAnimation = "run";
            spriteAnimationComponent.fps = Math.max(6,(int)Math.abs(body.getLinearVelocity().x*3));

            transformComponent.flipX = body.getLinearVelocity().x < 0;
        } else if (playerComponent.touchedPlatforms  > 0) {
            spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
            spriteAnimationComponent.currentAnimation = "idle";
        }

        if (body.getLinearVelocity().y > 0.2f){
            spriteAnimationComponent.currentAnimation = "jumpUp";
            spriteAnimationComponent.playMode = Animation.PlayMode.NORMAL;
        }else if (body.getLinearVelocity().y < 0.2f){
            spriteAnimationComponent.currentAnimation = "jumpUp";
            spriteAnimationComponent.playMode = Animation.PlayMode.REVERSED;
        }

        spriteAnimationStateComponent.set(spriteAnimationComponent);
    }
}
