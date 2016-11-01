package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mygdx.core.Constants
import com.mygdx.core.Main
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.BulletComponent
import com.mygdx.core.entity.components.TransformComponent

class BulletSystem : IteratingSystem {
    constructor() :
    super(Family.all(BulletComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int):
    super(Family.all(BulletComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformC = Mappers.transform.get(entity)
        val bulletC = Mappers.bullet.get(entity)

        bulletC.lifeTimer += deltaTime
        if (bulletC.lifeTimer > bulletC.lifeMax) {
            Main.engine.removeEntity(entity)
        }

        var deltaScale = deltaTime / Constants.STANDARD_FRAMETIME
        if (deltaScale > 2.5f) {
            deltaScale = 1f
        }

        transformC.x += bulletC.dx * deltaScale
        transformC.y += bulletC.dy * deltaScale
    }
}
