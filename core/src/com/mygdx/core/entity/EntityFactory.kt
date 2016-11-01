package com.mygdx.core.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mygdx.core.AssetPaths
import com.mygdx.core.Constants
import com.mygdx.core.Main
import com.mygdx.core.entity.components.*

object EntityFactory {
    fun player(x: Float, y: Float): Entity {
        val e = Entity()

        val transformC = TransformComponent()
        transformC.x = x
        transformC.y = y
        transformC.z = Constants.PLAYER_Z

        val texture: Texture = Main.assets.get(AssetPaths.SHIP)
        val textureRegion = TextureRegion(texture)
        val textureC = TextureComponent(textureRegion)

        val inputC = PlayerInputComponent()

        val playerC = PlayerComponent()

        val collisionC = CollisionComponent(texture.width, texture.height)

        e.add(transformC)
                .add(textureC)
                .add(inputC)
                .add(collisionC)
                .add(playerC)
        Main.engine.addEntity(e)

        return e
    }

    fun enemy(x: Float, y: Float): Entity {
        val e = Entity()

        val transformC = TransformComponent()
        transformC.x = x
        transformC.y = y
        transformC.z = Constants.ENEMY_Z
        transformC.angle = 180f // enemies face left

        val texture: Texture = Main.assets.get(AssetPaths.ENEMY)
        val textureRegion = TextureRegion(texture)
        val textureC = TextureComponent(textureRegion)

        val enemyC = EnemyComponent()

        val collisionC = CollisionComponent(texture.width, texture.height)

        e.add(transformC)
                .add(textureC)
                .add(enemyC)
                .add(collisionC)
        Main.engine.addEntity(e)

        return e
    }

    fun bullet(x: Float, y: Float, angle: Float, speed: Float): Entity {
        val e = Entity()

        val transformC = TransformComponent()
        transformC.x = x
        transformC.y = y
        transformC.z = Constants.BULLET_Z
        transformC.angle = angle

        val texture: Texture = Main.assets.get(AssetPaths.BULLET)
        val textureRegion = TextureRegion(texture)
        val textureC = TextureComponent(textureRegion)

        val collisionC = CollisionComponent(texture.width, texture.height)

        val angleRad = (angle * Constants.DEGTORAD).toDouble()
        val dx = (speed * Math.cos(angleRad)).toFloat()
        val dy = (speed * Math.sin(angleRad)).toFloat()
        val bulletC = BulletComponent(dx, dy)

        e.add(transformC)
                .add(textureC)
                .add(bulletC)
                .add(collisionC)
        Main.engine.addEntity(e)

        return e
    }

    fun explosion(x: Float, y: Float): Entity {
        val e = Entity()

        val transformC = TransformComponent()
        transformC.x = x
        transformC.y = y
        transformC.z = Constants.EXPLOSION_Z

        val explosionSheet: Texture = Main.assets.get(AssetPaths.EXPLOSION)
        val columns = 4
        val rows = 1
        val tmp = TextureRegion.split(explosionSheet, explosionSheet.width / columns, explosionSheet.height / rows);
        val explosionFrames = Array(tmp.size, { i -> Array(columns, { j -> tmp[i][j] }) })

        val textureC = TextureComponent(explosionFrames[0][0])

        val explosionC = ExplosionComponent(explosionFrames)

        e.add(transformC)
                .add(textureC)
                .add(explosionC)
        Main.engine.addEntity(e)

        return e
    }

    fun boss(x: Float, y: Float): Entity {
        val e = Entity()

        val transformC = TransformComponent()
        transformC.x = x
        transformC.y = y
        transformC.z = Constants.BOSS_Z

        val textures = Array<Texture>(8, { i -> Main.assets.get("boss$i.png")})
        val frames = Array<TextureRegion>(8, { i -> TextureRegion(textures[i])})
        val textureC = TextureComponent(frames[0])

        val bossC = BossComponent(frames)

        val collisionC = CollisionComponent(textures[0].width, textures[0].height)

        e.add(transformC)
                .add(textureC)
                .add(collisionC)
                .add(bossC)
        Main.engine.addEntity(e)

        return e
    }
    fun eyes(boss: Entity) {
        val columns = 5
        val rows = 1
        val eyeBigSheet: Texture = Main.assets.get(AssetPaths.EYE_BIG)
        var tmp = TextureRegion.split(eyeBigSheet, eyeBigSheet.width / columns, eyeBigSheet.height / rows);
        val eyeBigFrames = Array(tmp.size, { i -> Array(columns, { j -> tmp[i][j] }) })

        eye(boss, 5f, 60f, 0f, eyeBigFrames[0])
        eye(boss, -80f, -80f, 120f, eyeBigFrames[0])
        eye(boss, 80f, -80f, -120f, eyeBigFrames[0])

        val eyeSmallSheet: Texture = Main.assets.get(AssetPaths.EYE_SMALL)
        tmp = TextureRegion.split(eyeSmallSheet, eyeSmallSheet.width / columns, eyeSmallSheet.height / rows);
        val eyeSmallFrames = Array(tmp.size, { i -> Array(columns, { j -> tmp[i][j] }) })

        eye(boss, -5f, -80f, 180f, eyeSmallFrames[0])
        eye(boss, -40f, -10f, -120f, eyeSmallFrames[0])
        eye(boss, 45f, -10f, 120f, eyeSmallFrames[0])
        eye(boss, 0f, -30f, 0f, eyeSmallFrames[0])
    }

    fun eye(boss: Entity, x: Float, y: Float, angle: Float, frames: Array<TextureRegion>) {
        val e = Entity()

        val transformC = TransformComponent()
        transformC.z = Constants.EYE_Z

        val textureC = TextureComponent(frames[0])

        val tetherC = TetherComponent(boss, x, y, angle, true)

        val eyeC = EyeComponent(frames)

        val collisionC = CollisionComponent((frames[0].regionWidth * 1.4f).toInt(), (frames[0].regionHeight * 1.4f).toInt())

        e.add(transformC)
                .add(textureC)
                .add(tetherC)
                .add(eyeC)
        .add(collisionC)
        Main.engine.addEntity(e)
    }

    fun darkness(x: Float, y: Float): Entity {
        val e = Entity()

        val transformC = TransformComponent()
        transformC.x = x
        transformC.y = y
        transformC.z = Constants.DARKNESS_Z

        val circleC = CircleShapeComponent(1f)

        val colorC = ColorComponent(Color.BLACK)

        e.add(transformC)
                .add(circleC)
                .add(colorC)
        Main.engine.addEntity(e)

        return e
    }

    fun background() {
        run {
            val e = Entity()

            val texture: Texture = Main.assets.get(AssetPaths.BACKGROUND1)
            val region = TextureRegion(texture)
            val textureC = TextureComponent(region)

            val transformC = TransformComponent()
            transformC.x = texture.width / 2f
            transformC.y = texture.height / 2f
            transformC.z = Constants.BACKGROUND_Z

            val backgroundC = BackgroundComponent(1)

            e.add(transformC)
                    .add(textureC)
                    .add(backgroundC)
            Main.engine.addEntity(e)
        }
        run {
            val e = Entity()

            val texture: Texture = Main.assets.get(AssetPaths.BACKGROUND2)
            val region = TextureRegion(texture)
            val textureC = TextureComponent(region)

            val transformC = TransformComponent()
            transformC.x = texture.width * 1.5f
            transformC.y = texture.height / 2f
            transformC.z = Constants.BACKGROUND_Z

            val backgroundC = BackgroundComponent(2)

            e.add(transformC)
                    .add(textureC)
                    .add(backgroundC)
            Main.engine.addEntity(e)
        }
    }
}
