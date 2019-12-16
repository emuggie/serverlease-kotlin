package org.emuggie.onion.handler

import kotlin.reflect.KProperty

interface RequestHandler{
    fun before():Boolean{
        return true
    }
    fun get(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    fun post(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    fun update(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    fun delete(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    fun after(){
    }
}

class ThreadScoped<T> {
    private val value = InheritableThreadLocal<T>()
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return this.value.get()
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value.set(value)
    }
}