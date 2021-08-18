package com.mygdx.game

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.google.inject.*
import org.w3c.dom.Text

class MyGdxGame : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var img: Texture? = null
    internal val engine = Engine()
    private lateinit var injector : Injector

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        injector = Guice.createInjector(GameModule(this))
        injector.getInstance(Systems::class.java).list.map{ injector.getInstance(it) }.forEach { system ->
            engine.addSystem(system)
        }

        createEntities(){
        }
    }

    private fun createEntities(function: () -> Unit) {
        engine.addEntity(Entity().apply{
            add(TextureComponent(img))
            add(TransformComponent(Vector2(0F,0F)))
        })
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        engine.update(Gdx.graphics.deltaTime)
        batch!!.begin()
        batch!!.draw(img, 0f, 0f)
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
    }
}

class TransformComponent(val position : Vector2) : Component {

}


class TextureComponent(val texture: Texture?) : Component {

}
class RenderingSystem : IteratingSystem(Family.all(TransformComponent::class.java,TextureComponent::class.java).get()){
    override fun processEntity( entity:Entity, deltaTime:Float) {


    }
}

class SpamSystem : EntitySystem() {    // one of multiple systems to execute, can be added on systems list.
    override fun update(deltaTime: Float) {
        //println(deltaTime)
        println(1111111111111)
    }

}

class GameModule(private val myGdxGame: MyGdxGame) : Module {
    override fun configure(binder : Binder){
        binder.bind(SpriteBatch::class.java).toInstance(myGdxGame.batch)
    }

    @Provides @Singleton
    fun systems() : Systems {
        return Systems(listOf(
            SpamSystem::class.java
        ))
    }
}
data class Systems(val list: List<Class<out EntitySystem>>)