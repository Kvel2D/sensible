package com.mygdx.core.entity

import com.badlogic.ashley.core.ComponentMapper
import com.mygdx.core.entity.components.*

object Mappers {
    val background = ComponentMapper.getFor(BackgroundComponent::class.java)
    val boss = ComponentMapper.getFor(BossComponent::class.java)
    val bullet = ComponentMapper.getFor(BulletComponent::class.java)
    val circleShape = ComponentMapper.getFor(CircleShapeComponent::class.java)
    val collision = ComponentMapper.getFor(CollisionComponent::class.java)
    val color = ComponentMapper.getFor(ColorComponent::class.java)
    val enemy = ComponentMapper.getFor(EnemyComponent::class.java)
    val explosion = ComponentMapper.getFor(ExplosionComponent::class.java)
    val eye = ComponentMapper.getFor(EyeComponent::class.java)
    val input = ComponentMapper.getFor(PlayerInputComponent::class.java)
    val player = ComponentMapper.getFor(PlayerComponent::class.java)
    val transform = ComponentMapper.getFor(TransformComponent::class.java)
    val tether = ComponentMapper.getFor(TetherComponent::class.java)
    val texture = ComponentMapper.getFor(TextureComponent::class.java)
}