package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component

class EnemyComponent: Component {
    val speed = 5f
    val xMin = -200f
    val xMax = 1480f
    var direction = -1
}
