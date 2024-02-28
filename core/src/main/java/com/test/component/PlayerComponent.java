package com.test.component;

import com.artemis.Component;

/**
 * @author wenlong
 * @date 2024/2/28
 * @name com.test.component
 * @description
 **/
public class PlayerComponent extends Component {
    public int touchedPlatforms = 0;
    public int diamondsCollected = 0;

    public void reset(){
        touchedPlatforms = 0;
        diamondsCollected = 0;
    }
}
