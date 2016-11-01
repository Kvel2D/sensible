package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import com.mygdx.core.*
import com.mygdx.core.entity.EntityFactory
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.BOSS_STATE
import com.mygdx.core.entity.components.BossComponent
import com.mygdx.core.entity.components.CollisionComponent
import com.mygdx.core.entity.components.TransformComponent

class BossSystem : IteratingSystem {
    val bossSounds = Array<Sound>((40 - 15 + 1), { i -> Main.assets.get("sounds/${i + 15}.wav") })
    var soundTimer = 0f
    val hurt1: Sound = Main.assets.get(AssetPaths.HURT1)
    val hurt2: Sound = Main.assets.get(AssetPaths.HURT2)
    val hurt3: Sound = Main.assets.get(AssetPaths.HURT3)
    val hurt4: Sound = Main.assets.get(AssetPaths.HURT4)

    constructor() :
    super(Family.all(BossComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(BossComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformC = Mappers.transform.get(entity)
        val bossC = Mappers.boss.get(entity)

        var deltaScale = deltaTime / Constants.STANDARD_FRAMETIME
        if (deltaScale > 2.5f) {
            deltaScale = 1f
        }

        when (bossC.state) {
            BOSS_STATE.ENTRANCE -> {
                transformC.x -= 1f * deltaScale
                if (transformC.x < 1150f) {
                    bossC.state = BOSS_STATE.QUAD
                }
            }
            BOSS_STATE.QUAD -> {
                val collisionC = Mappers.collision.get(entity)
                if (bossC.hit) {
                    bossC.hit = false

                    when (bossC.currentFrame) {
                        0 -> hurt1.play(0.5f)
                        1 -> hurt2.play(0.5f)
                        2 -> hurt3.play(0.5f)
                        3 -> hurt4.play(0.5f)
                    }

                    // Decrease size
                    val textureC = Mappers.texture.get(entity)
                    if (bossC.currentFrame < 3) {
                        bossC.currentFrame++
                        bossC.minSpeed += 1f
                        bossC.maxSpeed += 1f
                        bossC.moveTimerMax--
                        bossC.moveTimer = 0f
                        bossC.rollSpeed = 0f
                        textureC.region = bossC.frames[bossC.currentFrame]
                        collisionC.set(textureC.region.regionWidth, textureC.region.regionHeight)
                    } else {
                        // Quad phase complete
                        bossC.currentFrame++
                        textureC.region = bossC.frames[bossC.currentFrame]
                        bossC.state = BOSS_STATE.SPIN_UP
                        bossC.stateTimer = 0f
                        return
                    }
                }

                if (bossC.rollTimer > 0f) {
                    bossC.rollTimer -= deltaTime
                } else {
                    bossC.rollTimer = MathUtils.random(bossC.rollTimerMin, bossC.rollTimerMax)

                    bossC.direction = MathUtils.randomSign()
                }

                transformC.angle += bossC.rollSpeed * bossC.direction

                if (bossC.moveTimer > 0f) {
                    bossC.moveTimer -= deltaTime
                } else {
                    bossC.moveTimer = MathUtils.random(bossC.moveTimerMin, bossC.moveTimerMax)
                    // +-(min-max)
                    bossC.dx = MathUtils.randomSign() * MathUtils.random(bossC.minSpeed, bossC.maxSpeed)
                    bossC.dy = MathUtils.randomSign() * MathUtils.random(bossC.minSpeed, bossC.maxSpeed)
                }

                transformC.x += bossC.dx * deltaScale
                transformC.y += bossC.dy * deltaScale

                // Deflect from walls
                if (transformC.x < collisionC.radius) {
                    transformC.x = collisionC.radius
                    bossC.dx = -bossC.dx
                } else if (transformC.x > com.mygdx.core.Constants.VIEWPORT_WIDTH - collisionC.radius) {
                    transformC.x = com.mygdx.core.Constants.VIEWPORT_WIDTH - collisionC.radius
                    bossC.dx = -bossC.dx
                }
                if (transformC.y < collisionC.radius) {
                    transformC.y = collisionC.radius
                    bossC.dy = -bossC.dy
                } else if (transformC.y > com.mygdx.core.Constants.VIEWPORT_HEIGHT - collisionC.radius) {
                    transformC.y = com.mygdx.core.Constants.VIEWPORT_HEIGHT - collisionC.radius
                    bossC.dy = -bossC.dy
                }
            }
            BOSS_STATE.SPIN_UP -> {
                if (bossC.stateTimer <= 1f) {
                    bossC.stateTimer += deltaTime
                    bossC.rollSpeed += bossC.stateTimer
                    transformC.angle += bossC.rollSpeed * deltaScale
                } else {
                    bossC.state = BOSS_STATE.SPIN_DOWN
                    transformC.angle = 0f
                    val textureC = Mappers.texture.get(entity)
                    textureC.region = bossC.frames[5]
                }
            }
            BOSS_STATE.SPIN_DOWN -> {
                bossC.rollSpeed -= 0.5f * deltaScale
                if (bossC.rollSpeed < 2f) {
                    bossC.rollSpeed = 2f
                }
                transformC.angle += bossC.rollSpeed
                if (Math.abs(transformC.angle) % 360 < 2f && bossC.rollSpeed == 2f) {
                    bossC.state = BOSS_STATE.TRIG_EXPANSION
                    bossC.stateTimer = 0f
                    bossC.currentFrame = 1
                }
            }
            BOSS_STATE.TRIG_EXPANSION -> {
                bossC.stateTimer += deltaTime
                if (bossC.currentFrame < 7) {
                    if (bossC.stateTimer > 0.15f) {
                        bossC.stateTimer = 0f
                        bossC.currentFrame++
                        val textureC = Mappers.texture.get(entity)
                        textureC.region = bossC.frames[bossC.currentFrame]
                    }
                } else {
                    EntityFactory.eyes(entity)
                    bossC.state = BOSS_STATE.TRIG
                }
            }
            BOSS_STATE.TRIG -> {
                val playerTransformC = Mappers.transform.get(player)
                val xDiff = playerTransformC.x - transformC.x
                val yDiff = playerTransformC.y - transformC.y

                transformC.x += bossC.trigNormalSpeed * xDiff / (Math.abs(xDiff) + Math.abs(yDiff)) * deltaScale
                transformC.y += bossC.trigNormalSpeed * yDiff / (Math.abs(xDiff) + Math.abs(yDiff)) * deltaScale
                if (bossC.hit) {
                    bossC.hit = false
                    bossHits++
                    if (bossHits > MathUtils.random(6, 10)) {
                        bossC.state = BOSS_STATE.BEGIN_DARK
                        bossC.stateTimer = 0f
                    }
                }

                playBossSound(xDiff, yDiff, deltaTime)
            }
            BOSS_STATE.BEGIN_DARK -> {
                bossC.stateTimer += deltaTime

                if (bossC.stateTimer > 1f) {
                    EntityFactory.darkness(transformC.x, transformC.y)
                    bossC.state = BOSS_STATE.DARK
                    entity.remove(CollisionComponent::class.java)
                }
            }
            BOSS_STATE.DARK -> {
                val playerTransformC = Mappers.transform.get(player)
                val xDiff = playerTransformC.x - transformC.x
                val yDiff = playerTransformC.y - transformC.y

                transformC.x += bossC.trigDarkSpeed * xDiff / (Math.abs(xDiff) + Math.abs(yDiff)) * deltaScale
                transformC.y += bossC.trigDarkSpeed * yDiff / (Math.abs(xDiff) + Math.abs(yDiff)) * deltaScale

                bossC.stateTimer += deltaTime

                if (bossC.stateTimer > 3f) {
                    bossC.state = BOSS_STATE.AIM
                    bossC.stateTimer = 0f
                }

                playBossSound(xDiff, yDiff, deltaTime)
            }
            BOSS_STATE.AIM -> {
                bossC.stateTimer += deltaTime

                if (bossC.stateTimer > 2f) {
                    bossC.state = BOSS_STATE.DARK
                    bossC.stateTimer = 0f
                }
            }
            BOSS_STATE.DEATH -> {
                val playerTransformC = Mappers.transform.get(player)
                val xDiff = playerTransformC.x - transformC.x
                val yDiff = playerTransformC.y - transformC.y

                bossC.stateTimer += deltaTime

                if (bossC.stateTimer < 1f) {
                    bossC.trigDarkSpeed = 1f
                } else if (bossC.stateTimer < 2.5f){
                    if (bossC.trigDarkSpeed > 0f) {
                        bossC.trigDarkSpeed -= 0.03f * deltaScale
                    } else {
                        bossC.trigDarkSpeed = 0f
                    }

                    if (soundTimer > 0f) {
                        soundTimer -= deltaTime
                    } else {
                        val k = MathUtils.random(3)
                                when (k) {
                                    0 -> hurt1.play()
                                    1 -> hurt2.play()
                                    2 -> hurt3.play()
                                    3 -> hurt4.play()
                                }
                        soundTimer = MathUtils.random(0.2f) + (bossC.stateTimer - 1f) / 5f
                    }
                    transformC.scale -= 0.01f * deltaScale
                } else {
                    Main.engine.removeEntity(entity)
                    gameState = GAME_STATE.WIN
                }

                transformC.x += bossC.trigDarkSpeed * xDiff / (Math.abs(xDiff) + Math.abs(yDiff)) * deltaScale
                transformC.y += bossC.trigDarkSpeed * yDiff / (Math.abs(xDiff) + Math.abs(yDiff)) * deltaScale
            }
        }
    }

    fun playBossSound(xDiff: Float, yDiff: Float, deltaTime: Float) {
        if (gameState == GAME_STATE.FAIL) {
            return
        }

        val dist2 = xDiff * xDiff + yDiff * yDiff
        val pitch = 1f - dist2 / (1280f * 1280f)
        if (soundTimer > 0f) {
            soundTimer -= deltaTime
        } else {
            bossSounds[(pitch * (bossSounds.size - 1)).toInt()].play(0.5f)
            soundTimer = 0.3f
        }
    }
}
