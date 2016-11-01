package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mygdx.core.Main
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.ExplosionComponent
import com.mygdx.core.entity.components.TextureComponent

class ExplosionSystem : IteratingSystem {
    constructor() :
    super(Family.all(ExplosionComponent::class.java, TextureComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(ExplosionComponent::class.java, TextureComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val explosionC = Mappers.explosion.get(entity)

        explosionC.frameTimer += deltaTime

        if (explosionC.frameTimer > explosionC.frameTimerMax) {
            explosionC.frameTimer = 0f
            explosionC.currentFrame++
            if (explosionC.currentFrame > explosionC.frameNum) {
                Main.engine.removeEntity(entity)
            } else {
                val textureC = Mappers.texture.get(entity)

                textureC.region = explosionC.frames[0][explosionC.currentFrame]
            }
        }
    }
}
