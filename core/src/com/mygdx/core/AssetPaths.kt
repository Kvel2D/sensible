package com.mygdx.core


object AssetPaths {
    const val SHIP = "ship.png"
    const val BULLET = "bullet.png"
    const val ENEMY = "enemy.png"
    const val EXPLOSION = "explosion.png"
    val BACKGROUND1 = "back1.png"
    val BACKGROUND2 = "back2.png"
    val WIN = "win.png"
    val FAIL = "fail.png"
    val EYE_BIG = "eye_big.png"
    val EYE_SMALL = "eye_small.png"
    val textures: List<String> = listOf(SHIP, BULLET, ENEMY, EXPLOSION,
            BACKGROUND1, BACKGROUND2, WIN, FAIL, EYE_BIG, EYE_SMALL
    )

    const val EYE_HURT = "sounds/eye.wav"
    const val HURT1 = "sounds/hurt1.wav"
    const val HURT2 = "sounds/hurt2.wav"
    const val HURT3 = "sounds/hurt3.wav"
    const val HURT4 = "sounds/hurt4.wav"
    const val MOB_HURT = "sounds/mob.wav"
    const val PLAYER_HURT = "sounds/player_hurt.wav"
    val sounds = listOf(EYE_HURT, HURT1, HURT2, HURT3, HURT4, MOB_HURT, PLAYER_HURT)
}