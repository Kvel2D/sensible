package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool

class TetherComponent : Component {
    val parent: Entity
    var xOffset: Float
    var yOffset: Float
    var angleOffset: Float
    var rotated: Boolean

    constructor(parent: Entity, xOffset: Float, yOffset: Float, angleOffset: Float, rotated: Boolean) {
        this.parent = parent
        this.xOffset = xOffset
        this.yOffset = yOffset
        this.angleOffset = angleOffset
        this.rotated = rotated
    }
}