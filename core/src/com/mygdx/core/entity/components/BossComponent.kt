package com.mygdx.core.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

enum class BOSS_STATE {
    ENTRANCE, QUAD,
    SPIN_UP, SPIN_DOWN, TRIG_EXPANSION, TRIG,
    BEGIN_DARK, DARK, AIM,
    DEATH
}

class BossComponent: Component {
    var state = BOSS_STATE.ENTRANCE
    var stateTimer = 0f

    var rollSpeed = 0.5f
    var direction = 1
    var rollTimer = 0f
    var rollTimerMin = 0.1f
    var rollTimerMax = 8f

    var maxSpeed = 3f
    var minSpeed = 2f
    var dx = 0f
    var dy = 0f
    var moveTimer = 0f
    var moveTimerMin = 0.5f
    var moveTimerMax = 8f

    var trigNormalSpeed = 1f
    var trigDarkSpeed = 3.5f

    var hit = false

    var currentFrame = 0
    val frames: Array<TextureRegion>

    constructor(frames: Array<TextureRegion>) {
        this.frames = frames
    }
}
