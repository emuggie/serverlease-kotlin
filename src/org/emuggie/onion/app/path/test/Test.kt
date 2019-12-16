package org.emuggie.onion.app.path.test

import org.emuggie.onion.app.path.second
import org.emuggie.onion.app.request
import org.emuggie.onion.handler.RequestHandler


class Test() :RequestHandler {
    override fun get() {
        val it = request
        println(second)
        val response = "ok done"
        it.sendResponseHeaders(200, response.toByteArray().size.toLong() )
        val os = it.responseBody
        os.write(response.toByteArray())
        os.close()
    }
}