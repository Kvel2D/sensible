package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mygdx.core.Constants
import com.mygdx.core.GAME_STATE
import com.mygdx.core.boss
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.BOSS_STATE
import com.mygdx.core.entity.components.CircleShapeComponent
import com.mygdx.core.entity.components.TransformComponent
import com.mygdx.core.gameState

class DarknessSystem : IteratingSystem {
    constructor() :
    super(Family.all(CircleShapeComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(CircleShapeComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val circleC = Mappers.circleShape.get(entity)

        var deltaScale = deltaTime / Constants.STANDARD_FRAMETIME
        if (deltaScale > 2.5f) {
            deltaScale = 1f
        }

        if (boss != null) {
            val bossTransformC = Mappers.transform.get(boss)
            val bossC = Mappers.boss.get(boss)

            val transformC = Mappers.transform.get(entity)
            transformC.x = bossTransformC.x
            transformC.y = bossTransformC.y

            if (bossC.state == BOSS_STATE.DEATH || gameState == GAME_STATE.FAIL) {
                if (circleC.radius > 0f) {
                    circleC.radius -= 0.1f * circleC.radius * deltaScale
                } else {
                    engine.removeEntity(entity)
                }
            } else {
                if (circleC.radius < 1280f) {
                    circleC.radius += 0.1f * circleC.radius * deltaScale
                }
            }
        }
    }
}
