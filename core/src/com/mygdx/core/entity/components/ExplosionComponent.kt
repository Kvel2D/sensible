package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

class ExplosionComponent: Component {
    val frames: Array<Array<TextureRegion>>
    var currentFrame = 0
    val frameNum: Int
    val frameTimerMax = 0.06f
    var frameTimer = 0f

    constructor(frames: Array<Array<TextureRegion>>) {
        this.frames = frames
        this.frameNum = frames[0].size - 1
    }
}
