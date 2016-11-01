package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.BackgroundComponent
import com.mygdx.core.entity.components.TextureComponent
import com.mygdx.core.entity.components.TransformComponent

class BackgroundSystem : IteratingSystem {
    constructor() :
    super(Family.all(BackgroundComponent::class.java, TextureComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(BackgroundComponent::class.java, TextureComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformC = Mappers.transform.get(entity)
        val textureC = Mappers.texture.get(entity)
        val backgroundC = Mappers.background.get(entity)

        transformC.x -= backgroundC.scrollSpeed

        if (transformC.x <= -textureC.region.regionWidth / 2f) {
            transformC.x = textureC.region.regionWidth * 1.5f
        }
    }
}
