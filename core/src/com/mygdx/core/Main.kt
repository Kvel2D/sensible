package com.mygdx.core

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.core.entity.systems.*

class Main : ApplicationAdapter() {
    companion object {
        val game: Game = object : Game() {
            override fun create() {
            }
        }
        val assets = AssetManager()
        val engine = Engine()
        lateinit var batch: SpriteBatch
        lateinit var gameScreen: GameScreen
    }

    override fun create() {
        batch = SpriteBatch()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Texture.setAssetManager(assets)

        // Load assets
        AssetPaths.textures.forEach { Main.assets.load(it, Texture::class.java) }
        AssetPaths.sounds.forEach { Main.assets.load(it, Sound::class.java) }
        for (i in 15..40) {
            Main.assets.load("sounds/$i.wav", Sound::class.java)
        }
        for (i in 0..7) {
            Main.assets.load("boss$i.png", Texture::class.java)
        }
        Main.assets.finishLoading()

        // Set up entity engine
        engine.addSystem(PlayerInputSystem(10))
        engine.addSystem(EnemySystem(12))
        engine.addSystem(BulletSystem(12))
        engine.addSystem(BackgroundSystem(13))
        engine.addSystem(BossSystem(14))
        engine.addSystem(EyeSystem(15))
        engine.addSystem(DarknessSystem(16))
        engine.addSystem(CollisionSystem(20))
        engine.addSystem(TetherSystem(30))
        engine.addSystem(ExplosionSystem(41))
        engine.addSystem(RenderSystem(50, batch))

        gameScreen = GameScreen()
        game.screen = gameScreen;
    }

    override fun render() {
        game.render()
    }

    override fun dispose() {
        batch.dispose()
        assets.dispose()
    }
}