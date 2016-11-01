package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.audio.Sound
import com.mygdx.core.*
import com.mygdx.core.entity.EntityFactory
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.BOSS_STATE
import com.mygdx.core.entity.components.CollisionComponent
import com.mygdx.core.entity.components.EYE_STATE
import com.mygdx.core.entity.components.TransformComponent

class CollisionSystem : IteratingSystem {
    val eyeSound: Sound = Main.assets.get(AssetPaths.EYE_HURT)
    val mobSound: Sound = Main.assets.get(AssetPaths.MOB_HURT)
    val playerSound: Sound = Main.assets.get(AssetPaths.PLAYER_HURT)

    constructor() :
    super(Family.all(CollisionComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(CollisionComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformC = Mappers.transform.get(entity)
        val collisionC = Mappers.collision.get(entity)

        entities.forEach {
            val otherTransformC = Mappers.transform.get(it)
            val otherCollisionC = Mappers.collision.get(it)

            val distance = (otherTransformC.x - transformC.x) * (otherTransformC.x - transformC.x) +
                    (otherTransformC.y - transformC.y) * (otherTransformC.y - transformC.y)

            if (distance < collisionC.radius2 + otherCollisionC.radius2) {
                if (Mappers.player.has(entity)) {
                    // PLAYER
                    if (Mappers.enemy.has(it)) {
                        // WITH ENEMY
                        Main.engine.removeEntity(entity)
                        Main.engine.removeEntity(it)
                        EntityFactory.explosion(transformC.x, transformC.y)
                        gameState = GAME_STATE.FAIL
                        playerSound.play(0.5f)
                    } else if (Mappers.boss.has(it)) {
                        // WITH BOSS
                        Main.engine.removeEntity(entity)
                        EntityFactory.explosion(transformC.x, transformC.y)
                        gameState = GAME_STATE.FAIL
                        playerSound.play(0.5f)
                    }
                    else if (Mappers.eye.has(it)) {
                        // WITH BOSS
                        Main.engine.removeEntity(entity)
                        EntityFactory.explosion(transformC.x, transformC.y)
                        gameState = GAME_STATE.FAIL
                        playerSound.play(0.5f)
                    }
                } else if (Mappers.enemy.has(entity)) {
                    // ENEMY
                    if (Mappers.bullet.has(it) && transformC.x < 1350f) {
                        // WITH BULLET
                        Main.engine.removeEntity(entity)
                        Main.engine.removeEntity(it)
                        EntityFactory.explosion(transformC.x, transformC.y)
                        mobSound.play(0.5f)
                    }
                } else if (Mappers.boss.has(entity)) {
                    // BOSS
                    if (Mappers.bullet.has(it)) {
                        // WITH BULLET
                        val bossC = Mappers.boss.get(entity)
                        if (bossC.state == BOSS_STATE.QUAD || bossC.state == BOSS_STATE.TRIG) {
                            bossC.hit = true
                        }
                        Main.engine.removeEntity(it)
                    }
                } else if (Mappers.eye.has(entity)) {
                    // EYE
                    if (Mappers.bullet.has(it)) {
                        // WITH BULLET
                        val eyeC = Mappers.eye.get(entity)
                        if (eyeC.state == EYE_STATE.AIM && eyeC.currentFrame > 1) {
                            // eye must be aiming and open(0,1 frames are closed)
                            EntityFactory.explosion(transformC.x, transformC.y)
                            Main.engine.removeEntity(it)
                            Main.engine.removeEntity(entity)
                            eyeSound.play(0.5f)
                            eyesKilled++
                            if (eyesKilled == 7) {
                                val bossC = Mappers.boss.get(boss)
                                bossC.state = BOSS_STATE.DEATH
                                bossC.stateTimer = 0f
                            }
                        }
                    }
                }
            }
        }
    }
}

