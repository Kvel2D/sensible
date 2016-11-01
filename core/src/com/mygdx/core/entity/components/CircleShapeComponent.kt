package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component

class CircleShapeComponent : Component {
    var radius: Float

    constructor(radius: Float) {
        this.radius = radius
    }
}
