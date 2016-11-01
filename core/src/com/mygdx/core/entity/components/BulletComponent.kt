package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component

class BulletComponent: Component {
    var dx: Float
    var dy: Float
    var lifeTimer = 0f
    val lifeMax = 2f

    constructor(dx: Float, dy: Float) {
        this.dx = dx
        this.dy = dy
    }
}

