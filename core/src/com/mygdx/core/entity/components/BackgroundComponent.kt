package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component

class BackgroundComponent: Component {
    val scrollSpeed = 4f
    var currentFrame: Int

    constructor(currentFrame: Int) {
        this.currentFrame = currentFrame
    }
}
