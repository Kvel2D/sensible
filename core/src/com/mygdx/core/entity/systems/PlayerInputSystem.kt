package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.mygdx.core.AssetPaths
import com.mygdx.core.Constants
import com.mygdx.core.Main
import com.mygdx.core.entity.EntityFactory
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.CollisionComponent
import com.mygdx.core.entity.components.PlayerInputComponent
import com.mygdx.core.entity.components.TransformComponent

class PlayerInputSystem : IteratingSystem {
    object Controls {
        const final val LEFT = Input.Keys.LEFT
        const final val LEFT_ALT = Input.Keys.A
        const final val RIGHT = Input.Keys.RIGHT
        const final val RIGHT_ALT = Input.Keys.D
        const final val DOWN = Input.Keys.DOWN
        const final val DOWN_ALT = Input.Keys.S
        const final val UP = Input.Keys.UP
        const final val UP_ALT = Input.Keys.W
        const final val SHOOT = Input.Keys.SPACE
    }

    var left = false
    var right = false
    var down = false
    var up = false
    var shoot = false

    var fullMovement = false

    constructor() :
    super(Family.all(PlayerInputComponent::class.java, CollisionComponent::class.java, TransformComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(PlayerInputComponent::class.java, CollisionComponent::class.java, TransformComponent::class.java).get(), priority)

    override fun update(deltaTime: Float) {
        left = Gdx.input.isKeyPressed(Controls.LEFT) || Gdx.input.isKeyPressed(Controls.LEFT_ALT)
        right = Gdx.input.isKeyPressed(Controls.RIGHT) || Gdx.input.isKeyPressed(Controls.RIGHT_ALT)
        up = Gdx.input.isKeyPressed(Controls.UP) || Gdx.input.isKeyPressed(Controls.UP_ALT)
        down = Gdx.input.isKeyPressed(Controls.DOWN) || Gdx.input.isKeyPressed(Controls.DOWN_ALT)
        shoot = Gdx.input.isKeyPressed(Controls.SHOOT)

        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformC = Mappers.transform.get(entity)
        val playerInputC = Mappers.input.get(entity)
        val collisionC = Mappers.collision.get(entity)

        var deltaScale = deltaTime / Constants.STANDARD_FRAMETIME
        if (deltaScale > 2.5f) {
            deltaScale = 1f
        }

        if (playerInputC.bulletCharge > 0f) {
            playerInputC.bulletTimer -= deltaTime
        }

        if (fullMovement) {
            if (left && !right) {
                transformC.angle += playerInputC.rotationSpeed * deltaScale
            } else if (right && !left) {
                transformC.angle -= playerInputC.rotationSpeed * deltaScale
            }

            if (down && !up) {
                val angleRad = (transformC.angle * Constants.DEGTORAD).toDouble()
                transformC.x -= playerInputC.movementSpeed * Math.cos(angleRad).toFloat() * deltaScale
                transformC.y -= playerInputC.movementSpeed * Math.sin(angleRad).toFloat() * deltaScale
            } else if (up && !down) {
                val angleRad = (transformC.angle * Constants.DEGTORAD).toDouble()
                transformC.x += playerInputC.movementSpeed * Math.cos(angleRad).toFloat() * deltaScale
                transformC.y += playerInputC.movementSpeed * Math.sin(angleRad).toFloat() * deltaScale
            }
        } else {
            if (down && !up) {
                transformC.y -= playerInputC.movementSpeed * deltaScale
            } else if (up && !down) {
                transformC.y += playerInputC.movementSpeed * deltaScale
            }
        }

        if (transformC.x < collisionC.radius) {
            transformC.x = collisionC.radius
        } else if (transformC.x > Constants.VIEWPORT_WIDTH - collisionC.radius) {
            transformC.x = Constants.VIEWPORT_WIDTH - collisionC.radius
        }
        if (transformC.y < collisionC.radius) {
            transformC.y = collisionC.radius
        } else if (transformC.y > Constants.VIEWPORT_HEIGHT - collisionC.radius) {
            transformC.y = Constants.VIEWPORT_HEIGHT - collisionC.radius
        }

        if (shoot && playerInputC.bulletTimer <= 0f) {
            playerInputC.bulletTimer = playerInputC.bulletCharge
            EntityFactory.bullet(transformC.x, transformC.y, transformC.angle, 20f)
        }
    }
}
