package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.mygdx.core.*
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.*

class EyeSystem : IteratingSystem {
    var bossC: BossComponent? = null

    constructor() :
    super(Family.all(EyeComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(EyeComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun update(deltaTime: Float) {
        if (boss != null) {
            bossC = Mappers.boss.get(boss)
        }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val eyeC = Mappers.eye.get(entity)
        val textureC = Mappers.texture.get(entity)

        if (bossC != null && bossC!!.state == BOSS_STATE.BEGIN_DARK) {
            eyeC.stateTimer += deltaTime

            if (eyeC.stateTimer > 0.1f) {
                if (eyeC.currentFrame > 1) {
                    eyeC.stateTimer = 0f
                    eyeC.currentFrame--
                    textureC.region = eyeC.frames[eyeC.currentFrame]
                } else {
                    eyeC.state = EYE_STATE.STAND_BY
                    eyeC.stateTimer = 0f
                    eyeC.currentFrame = 0
                    textureC.region = eyeC.frames[eyeC.currentFrame]
                }
            }

            return
        } else if (bossC != null && bossC!!.state == BOSS_STATE.AIM) {
            if (eyeC.state == EYE_STATE.STAND_BY) {
                eyeC.stateTimer = 0f
                eyeC.state = EYE_STATE.AIM
            } else if (eyeC.state == EYE_STATE.AIM) {
                eyeC.stateTimer += deltaTime

                if (eyeC.stateTimer < 0.1f) {
                    eyeC.currentFrame = 1
                } else if (eyeC.stateTimer < 0.2f) {
                    eyeC.currentFrame = 1
                } else if (eyeC.stateTimer < 0.3f) {
                    eyeC.currentFrame = 2
                } else if (eyeC.stateTimer < 0.4f) {
                    eyeC.currentFrame = 3
                } else if (eyeC.stateTimer < 0.5f) {
                    eyeC.currentFrame = 4
                } else if (eyeC.stateTimer > 1.5f && eyeC.stateTimer < 1.6f) {
                    eyeC.currentFrame = 3
                } else if (eyeC.stateTimer > 1.6f && eyeC.stateTimer < 1.7f) {
                    eyeC.currentFrame = 2
                } else if (eyeC.stateTimer > 1.7f && eyeC.stateTimer < 1.8f) {
                    eyeC.currentFrame = 1
                }
                textureC.region = eyeC.frames[eyeC.currentFrame]
            }
            return
        }

        when (eyeC.state) {
            EYE_STATE.SLEEP -> {
                eyeC.stateTimer += deltaTime

                if (eyeC.stateTimer > 0.1f) {
                    if (eyeC.currentFrame < eyeC.frames.size - 1) {
                        eyeC.stateTimer = 0f
                        eyeC.currentFrame++
                        textureC.region = eyeC.frames[eyeC.currentFrame]
                    } else {
                        eyeC.state = EYE_STATE.AWAKE
                        eyeC.stateTimer = 0f
                    }
                }
            }
            EYE_STATE.AWAKE -> {
                val k = MathUtils.random(120)
                if (k == 0) {
                    eyeC.state = EYE_STATE.BLINK_IN
                    eyeC.stateTimer = 0f
                }
            }
            EYE_STATE.BLINK_IN -> {
                eyeC.stateTimer += deltaTime

                if (eyeC.stateTimer > 0.1f) {
                    if (eyeC.currentFrame > 1) {
                        eyeC.stateTimer = 0f
                        eyeC.currentFrame--
                        textureC.region = eyeC.frames[eyeC.currentFrame]
                    } else {
                        eyeC.state = EYE_STATE.BLINK_OUT
                        eyeC.stateTimer = 0f
                    }
                }
            }
            EYE_STATE.BLINK_OUT -> {
                eyeC.stateTimer += deltaTime

                if (eyeC.stateTimer > 0.1f) {
                    if (eyeC.currentFrame < eyeC.frames.size - 1) {
                        eyeC.stateTimer = 0f
                        eyeC.currentFrame++
                        textureC.region = eyeC.frames[eyeC.currentFrame]
                    } else {
                        eyeC.state = EYE_STATE.AWAKE
                        eyeC.stateTimer = 0f
                    }
                }
            }
            EYE_STATE.AIM -> {
                eyeC.state = EYE_STATE.STAND_BY
                eyeC.currentFrame = 0
                textureC.region = eyeC.frames[eyeC.currentFrame]
            }
        }

    }
}
