package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

enum class EYE_STATE {
    SLEEP, AWAKE, BLINK_IN, BLINK_OUT, STAND_BY, AIM
}

class EyeComponent: Component {
    var state = EYE_STATE.SLEEP
    var stateTimer = 0f

    var currentFrame = 0

    val frames: Array<TextureRegion>

    constructor(frames: Array<TextureRegion>) {
        this.frames = frames
    }
}
