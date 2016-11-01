package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color

class ColorComponent : Component {
    val color: Color

    constructor(color: Color) {
        this.color = color.cpy()
    }
}