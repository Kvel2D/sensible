package com.mygdx.core

import com.badlogic.ashley.core.Entity

var player: Entity? = null
var boss: Entity? = null
var eyesKilled = 0
var bossHits = 0

enum class GAME_STATE {
    NORMAL, FAIL, WIN
}
var gameState = GAME_STATE.NORMAL
var stateTimer = 0f
