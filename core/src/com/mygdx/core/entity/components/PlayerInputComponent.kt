package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component

class PlayerInputComponent : Component {
    val rotationSpeed = 3f
    val movementSpeed = 10f
    val bulletCharge = 0.5f
    var bulletTimer = 0f
}
