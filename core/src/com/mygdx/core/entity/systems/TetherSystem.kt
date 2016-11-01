package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.TetherComponent
import com.mygdx.core.entity.components.TransformComponent
import com.mygdx.core.rotateAround

class TetherSystem : IteratingSystem {
    constructor() :
    super(Family.all(TetherComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(TetherComponent::class.java, TransformComponent::class.java).get(), priority)

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val tetherC = Mappers.tether.get(entity)

        val transformC = Mappers.transform.get(entity)
        val parentTransformC = Mappers.transform.get(tetherC.parent)

        if (tetherC.rotated) {
            val position = Vector2(parentTransformC.x + tetherC.xOffset,
                    parentTransformC.y + tetherC.yOffset)
            position.rotateAround(parentTransformC.x, parentTransformC.y, parentTransformC.angle)
            transformC.x = position.x
            transformC.y = position.y
            transformC.angle = parentTransformC.angle + tetherC.angleOffset
        } else {
            transformC.x = parentTransformC.x + tetherC.xOffset
            transformC.y = parentTransformC.y + tetherC.yOffset
            transformC.angle = parentTransformC.angle + tetherC.angleOffset
        }
    }
}