package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mygdx.core.Constants
import com.mygdx.core.Main
import com.mygdx.core.boss
import com.mygdx.core.entity.EntityFactory
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.EnemyComponent
import com.mygdx.core.entity.components.TransformComponent

class EnemySystem : IteratingSystem {
    var deltaScale = 1f

    constructor() :
    super(Family.all(EnemyComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(EnemyComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun update(deltaTime: Float) {
        // All enemies dead, boss comes out
        if (entities.size() == 0) {
            this.setProcessing(false)
            Main.engine.getSystem(BackgroundSystem::class.java).setProcessing(false)
            Main.engine.getSystem(PlayerInputSystem::class.java).fullMovement = true
            boss = EntityFactory.boss(1400f, 360f)
        }

        deltaScale = deltaTime / Constants.STANDARD_FRAMETIME
        if (deltaScale > 2.5f) {
            deltaScale = 1f
        }

        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformC = Mappers.transform.get(entity)
        val enemyC = Mappers.enemy.get(entity)

        transformC.x += enemyC.speed * enemyC.direction * deltaScale

        if (transformC.x < enemyC.xMin) {
            enemyC.direction = 1
            transformC.angle = 180f
            Main.engine.removeEntity(entity)
        } else if (transformC.x > enemyC.xMax) {
            enemyC.direction = -1
            transformC.angle = 0f
        }
    }
}
