package org.emuggie.onion.app.path


import org.emuggie.onion.app.request
import org.emuggie.onion.handler.RequestHandler
import org.emuggie.onion.handler.ThreadScoped
import java.util.*

var second by ThreadScoped<String>()

class Test : RequestHandler{
    override fun before():Boolean {
        second = "Test:${Date()}"
        return true
    }

    override fun get(){
        //logger.info("GET invoked")
    }

    override fun after() {
        println("after invoked")
    }
}
