package com.test.system;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.test.component.PlayerComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ViewPortComponent;

/**
 * @author wenlong
 * @date 2024/3/1
 * @name com.test.system
 * @description
 **/
@All(ViewPortComponent.class)
public class CameraSystem extends IteratingSystem {
    ComponentMapper<ViewPortComponent> viewPortComponentMapper;

    private Entity focus;
    private final float xMin;
    private final float xMax;
    private final float yMin;
    private final float yMax;

    public CameraSystem(float xMin,float xMax,float yMin,float yMax){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public void setFocus(Entity focus) {
        this.focus = focus;
    }

    @Override
    protected void process(int entityId) {
        ViewPortComponent viewPortComponent = viewPortComponentMapper.get(entityId);
        Camera camera = viewPortComponent.viewPort.getCamera();

        if (focus != null){
            TransformComponent transformComponent = focus.getComponent(TransformComponent.class);
            if (transformComponent != null){
                float x = Math.max(xMin,Math.min(xMax,transformComponent.x));
                float y = Math.max(yMin,Math.min(yMax,transformComponent.y+2));
                camera.position.set(x,y,0);
            }
        }
    }
}
