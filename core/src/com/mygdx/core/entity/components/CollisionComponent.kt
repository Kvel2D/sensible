package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component

class CollisionComponent: Component {
    var radius: Float
    var radius2: Float

    constructor(width: Int, height: Int) {
        val maxDimension = Math.max(width, height).toFloat()
        this.radius = maxDimension / 2f
        this.radius2 = radius * radius
    }

    fun set(width: Int, height: Int) {
        val maxDimension = Math.max(width, height).toFloat()
        this.radius = maxDimension / 2f
        this.radius2 = radius * radius
    }
}

